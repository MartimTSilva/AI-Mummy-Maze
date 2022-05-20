package mummyMaze;

import agent.Action;
import agent.State;

import java.util.ArrayList;
import java.util.Arrays;

public class MummyMazeState extends State implements Cloneable {
    public static final int SIZE = 13;

    private char[][] matrix;
    private Cell hero, exit, whiteMummy, redMummy, scorpion;
    private ArrayList<Cell> doors, traps;

    public MummyMazeState(char[][] matrix) {
        this.matrix = new char[SIZE][SIZE];
        hero = exit = whiteMummy = redMummy = scorpion = null;
        doors = traps = null;

        if (matrix == null)
            return;

        //Deep copy da matriz
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];
                this.getElementPosition(i, j);
            }
        }
    }

    public String getMatrix() {
        StringBuilder state = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                state.append(matrix[i][j]);
            }
            state.append("\n");
        }
        return state.toString();
    }

    private void getElementPosition(int i, int j) {
        switch (matrix[i][j]) {
            case 'H':
                hero = new Cell(i, j, matrix[i][j]);
                break;
            case 'S':
                exit = new Cell(i, j, matrix[i][j]);
                break;
            case 'M':
                whiteMummy = new Cell(i, j, matrix[i][j]);
                break;
            case 'V':
                redMummy = new Cell(i, j, matrix[i][j]);
                break;
            case 'E':
                scorpion = new Cell(i, j, matrix[i][j]);
                break;
            case 'A':
                if (traps == null) {
                    traps = new ArrayList<>();
                }
                traps.add(new Cell(i, j, matrix[i][j]));
                break;
            case '=':
            case '_':
            case '"':
            case ')':
                if (doors == null) {
                    doors = new ArrayList<>();
                }
                doors.add(new Cell(i, j, matrix[i][j]));
                break;
        }
    }

    @Override
    public void executeAction(Action action) {
        action.execute(this);
        fireGameChanged(null);
    }

    private boolean hasWall(int line, int col) {
        return matrix[line][col] == '-' || matrix[line][col] == '|';
    }

    public boolean canMoveUp() {
        return (hero.i > 1 && !hasWall(hero.i - 1, hero.j) && matrix[hero.i - 2][hero.j] == '.')
                || (hero.i == 1 && matrix[hero.i - 1][hero.j] == 'S');
    }

    public boolean canMoveDown() {
        return (hero.i < SIZE - 2 && !hasWall(hero.i + 1, hero.j) && matrix[hero.i + 2][hero.j] == '.')
                || (hero.i == SIZE - 2 && matrix[hero.i + 1][hero.j] == 'S');
    }

    public boolean canMoveRight() {
        return (hero.j < SIZE - 2 && !hasWall(hero.i, hero.j + 1) && matrix[hero.i][hero.j + 2] == '.')
                || (hero.j == SIZE - 2 && matrix[hero.i][hero.j + 1] == 'S');
    }

    public boolean canMoveLeft() {
        return (hero.j > 2 && !hasWall(hero.i, hero.j - 1) && matrix[hero.i][hero.j - 2] == '.')
                || (hero.j == 1 && matrix[hero.i][hero.j - 1] == 'S');
    }

    public void moveUp() {
        moveVertically(hero.i == 1 ? -1 : -2, hero);
        moveEnemies();
    }

    public void moveRight() {
        moveHorizontally(hero.j == SIZE - 2 ? 1 : 2, hero);
        moveEnemies();
    }

    public void moveDown() {
        moveVertically(hero.i == SIZE - 2 ? 1 : 2, hero);
        moveEnemies();
    }

    public void moveLeft() {
        moveHorizontally(hero.j == 1 ? -1 : -2, hero);
        moveEnemies();
    }

    public void dontMove() {
        //TODO
    }

    private void moveHorizontally(int positionsToMove, Cell agent) {
        matrix[agent.i][agent.j] = '.';
        matrix[agent.i][agent.j + positionsToMove] = agent.floor;

        agent.setJ(agent.j + positionsToMove);
    }

    private void moveVertically(int positionsToMove, Cell agent) {
        matrix[agent.i][agent.j] = '.';
        matrix[agent.i + positionsToMove][agent.j] = agent.floor;

        agent.setI(agent.i + positionsToMove);
    }

    private void moveEnemies() {
        if (isGoalReached())
            return;

        moveWhiteMummy();
        moveRedMummy();
        moveScorpion();
    }

    public Cell getHero() {
        return hero;
    }

    private void moveWhiteMummy() {
        if (whiteMummy == null)
            return;

        if (whiteMummy.j != hero.j) {
            int diff = hero.j - whiteMummy.j;
            if (diff < 0) {
                if (!hasWall(whiteMummy.i, whiteMummy.j - 1)) {
                    moveHorizontally(-2, whiteMummy);
                    if (Math.abs(diff) > 2 && !hasWall(whiteMummy.i, whiteMummy.j - 1)) {
                        moveHorizontally(-2, whiteMummy);
                    }
                }
            } else {
                if (!hasWall(whiteMummy.i, whiteMummy.j + 1)) {
                    moveHorizontally(2, whiteMummy);
                    if (Math.abs(diff) > 2 && !hasWall(whiteMummy.i, whiteMummy.j + 1)) {
                        moveHorizontally(2, whiteMummy);
                    }
                }
            }
        } else {
            int diff = hero.i - whiteMummy.i;
            if (diff < 0) {
                if (!hasWall(whiteMummy.i - 1, whiteMummy.j)) {
                    moveVertically(-2, whiteMummy);
                    if (Math.abs(diff) > 2 && !hasWall(whiteMummy.i - 1, whiteMummy.j)) {
                        moveVertically(-2, whiteMummy);
                    }
                }
            } else {
                if (!hasWall(whiteMummy.i + 1, whiteMummy.j)) {
                    moveVertically(2, whiteMummy);
                    if (Math.abs(diff) > 2 && !hasWall(whiteMummy.i + 1, whiteMummy.j)) {
                        moveVertically(2, whiteMummy);
                    }
                }
            }
        }
        // Se a mumia matar o heroi, passar o heroi para a posição 0 (fora do nível)
        isHeroDead(whiteMummy);
    }

    private void moveRedMummy(){
        if (redMummy == null)
            return;

        if (redMummy.i != hero.i) {
            int diff = hero.i - redMummy.i;
            if (diff < 0) {  // Múmia encontra-se abaixo do herói (tem que subir -n casas)
                if (!hasWall(redMummy.i - 1, redMummy.j)) {
                    moveVertically(-2, redMummy);
                    if (Math.abs(diff) > 2 && !hasWall(redMummy.i - 1, redMummy.j)) {
                        moveVertically(-2, redMummy);
                    }
                }
            } else {
                if (!hasWall(redMummy.i + 1, redMummy.j)) {
                    moveVertically(2, redMummy);
                    if (Math.abs(diff) > 2 && !hasWall(redMummy.i + 1, redMummy.j)) {
                        moveVertically(2, redMummy);
                    }
                }
            }
        } else {
            int diff = hero.j - redMummy.j;
            if (diff < 0) { // Múmia encontra-se à direita do herói (tem que andar -n casas para a esquerda)
                if (!hasWall(redMummy.i, redMummy.j - 1)) {
                    moveHorizontally(-2, redMummy);
                    if (Math.abs(diff) > 2 && !hasWall(redMummy.i, redMummy.j - 1)) {
                        moveHorizontally(-2, redMummy);
                    }
                }
            } else {
                if (!hasWall(redMummy.i, redMummy.j + 1)) {
                    moveHorizontally(2, redMummy);
                    if (Math.abs(diff) > 2 && !hasWall(redMummy.i, redMummy.j + 1)) {
                        moveHorizontally(2, redMummy);
                    }
                }
            }
        }
        // Se a mumia matar o heroi, passar o heroi para a posição 0 (fora do nível)
        isHeroDead(redMummy);
    }

    private void moveScorpion(){
        if (scorpion == null)
            return;

        if (scorpion.j != hero.j) {
            int diff = hero.j - scorpion.j;
            if (diff < 0) {
                if (!hasWall(scorpion.i, scorpion.j - 1)) {
                    moveHorizontally(-2, scorpion);
                }
            } else {
                if (!hasWall(scorpion.i, scorpion.j + 1)) {
                    moveHorizontally(2, scorpion);
                }
            }
        } else {
            int diff = hero.i - scorpion.i;
            if (diff < 0) {
                if (!hasWall(scorpion.i - 1, scorpion.j)) {
                    moveVertically(-2, scorpion);
                }
            } else {
                if (!hasWall(scorpion.i + 1, scorpion.j)) {
                    moveVertically(2, scorpion);
                }
            }
        }
        isHeroDead(scorpion);
    }

    private void isHeroDead(Cell enemy) {
        // Se a mumia matar o heroi, passa-se o heroi para a posição 0 (fora do nível)
        if (isEnemyGoalReached(enemy)){
            hero.i = 0;
            hero.j = 0;
        }
    }

    public boolean isEnemyGoalReached(Cell enemy) {
        if (enemy != null) {
            if (enemy.i == hero.i && enemy.j == hero.j)
                return true;
        }
        return false;
    }

    public boolean isGoalReached() {
        return hero.i == exit.i && hero.j == exit.j;
    }

    public double computeGoalDistance() {
        //TODO
        int h = 0;
        return 0;
    }

    public int getNumLines() {
        return matrix.length;
    }

    public int getNumColumns() {
        return matrix[0].length;
    }

    public int getTileValue(int line, int column) {
        if (!isValidPosition(line, column)) {
            throw new IndexOutOfBoundsException("Invalid position!");
        }
        return matrix[line][column];
    }

    public boolean isValidPosition(int line, int column) {
        return line >= 0 && line < matrix.length && column >= 0 && column < matrix[0].length;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MummyMazeState)) {
            return false;
        }

        MummyMazeState o = (MummyMazeState) other;
        if (matrix.length != o.matrix.length) {
            return false;
        }

        return Arrays.deepEquals(matrix, o.matrix);
    }

    @Override
    public int hashCode() {
        return 97 * 7 + Arrays.deepHashCode(this.matrix);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            buffer.append('\n');
            for (int j = 0; j < matrix.length; j++) {
                buffer.append(matrix[i][j]);
                buffer.append(' ');
            }
        }
        return buffer.toString();
    }

    @Override
    public MummyMazeState clone() {
        return new MummyMazeState(matrix);
    }

    //Listeners
    private transient ArrayList<MummyMazeListener> listeners = new ArrayList<MummyMazeListener>(3);

    public synchronized void removeListener(MummyMazeListener l) {
        if (listeners != null && listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    public synchronized void addListener(MummyMazeListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void fireGameChanged(MummyMazeListener pe) {
        for (MummyMazeListener listener : listeners) {
            listener.gameChanged(null);
        }
    }
}
