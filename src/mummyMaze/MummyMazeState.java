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
            case Cell.HERO:
                hero = new Cell(i, j, matrix[i][j]);
                break;
            case Cell.EXIT:
                exit = new Cell(i, j, matrix[i][j]);
                break;
            case Cell.WHITE_MUMMY:
                whiteMummy = new Cell(i, j, matrix[i][j]);
                break;
            case Cell.RED_MUMMY:
                redMummy = new Cell(i, j, matrix[i][j]);
                break;
            case Cell.SCORPION:
                scorpion = new Cell(i, j, matrix[i][j]);
                break;
            case Cell.TRAP:
                if (traps == null) {
                    traps = new ArrayList<>();
                }
                traps.add(new Cell(i, j, matrix[i][j]));
                break;
            case Cell.HORIZ_DOOR_CLOSED:
            case Cell.HORIZ_DOOR_OPEN:
            case Cell.VERT_DOOR_CLOSED:
            case Cell.VERT_DOOR_OPEN:
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
        return matrix[line][col] == Cell.HORIZ_WALL || matrix[line][col] == Cell.VERT_WALL;
    }

    public boolean canMoveUp() {
        return (hero.i > 1 && !hasWall(hero.i - 1, hero.j) && matrix[hero.i - 2][hero.j] == Cell.FLOOR)
                || (hero.i == 1 && matrix[hero.i - 1][hero.j] == Cell.EXIT);
    }

    public boolean canMoveDown() {
        return (hero.i < SIZE - 2 && !hasWall(hero.i + 1, hero.j) && matrix[hero.i + 2][hero.j] == Cell.FLOOR)
                || (hero.i == SIZE - 2 && matrix[hero.i + 1][hero.j] == Cell.EXIT);
    }

    public boolean canMoveRight() {
        return (hero.j < SIZE - 2 && !hasWall(hero.i, hero.j + 1) && matrix[hero.i][hero.j + 2] == Cell.FLOOR)
                || (hero.j == SIZE - 2 && matrix[hero.i][hero.j + 1] == Cell.EXIT);
    }

    public boolean canMoveLeft() {
        return (hero.j > 2 && !hasWall(hero.i, hero.j - 1) && matrix[hero.i][hero.j - 2] == Cell.FLOOR)
                || (hero.j == 1 && matrix[hero.i][hero.j - 1] == Cell.EXIT);
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
        matrix[agent.i][agent.j] = Cell.FLOOR;
        matrix[agent.i][agent.j + positionsToMove] = agent.cellType;

        agent.setJ(agent.j + positionsToMove);
    }

    private void moveVertically(int positionsToMove, Cell agent) {
        matrix[agent.i][agent.j] = Cell.FLOOR;
        matrix[agent.i + positionsToMove][agent.j] = agent.cellType;

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
            moveEnemyHorizontally(whiteMummy, true);
        } else {
            moveEnemyVertically(whiteMummy, true);
        }
        // Se a mumia matar o heroi, passar o heroi para a posição 0 (fora do nível)
        isEnemyGoalReached(whiteMummy);
    }

    private void moveRedMummy() {
        if (redMummy == null)
            return;

        if (redMummy.i != hero.i) {
            moveEnemyVertically(redMummy, true);
        } else {
            moveEnemyHorizontally(redMummy, true);
        }

        // Se a mumia matar o heroi, passar o heroi para a posição 0 (fora do nível)
        isEnemyGoalReached(redMummy);
    }

    private void moveScorpion() {
        if (scorpion == null)
            return;

        if (scorpion.j != hero.j) {
            moveEnemyHorizontally(scorpion, false);
        } else {
            moveEnemyVertically(scorpion, false);
        }

        isEnemyGoalReached(scorpion);
    }

    private void moveEnemyVertically(Cell enemy, boolean isMummy) {
        int diff = hero.i - enemy.i;
        if (diff < 0) {
            if (!hasWall(enemy.i - 1, enemy.j)) {
                moveVertically(-2, enemy);
                if (isMummy && Math.abs(diff) > 2 && !hasWall(enemy.i - 1, enemy.j)) {
                    moveVertically(-2, enemy);
                }
            }
        } else {
            if (!hasWall(enemy.i + 1, enemy.j)) {
                moveVertically(2, enemy);
                if (isMummy && Math.abs(diff) > 2 && !hasWall(enemy.i + 1, enemy.j)) {
                    moveVertically(2, enemy);
                }
            }
        }
    }

    private void moveEnemyHorizontally(Cell enemy, boolean isMummy) {
        int diff = hero.j - enemy.j;
        if (diff < 0) {
            if (!hasWall(enemy.i, enemy.j - 1)) {
                moveHorizontally(-2, enemy);
                if (isMummy && Math.abs(diff) > 2 && !hasWall(enemy.i, enemy.j - 1)) {
                    moveHorizontally(-2, enemy);
                }
            }
        } else {
            if (!hasWall(enemy.i, enemy.j + 1)) {
                moveHorizontally(2, enemy);
                if (isMummy && Math.abs(diff) > 2 && !hasWall(enemy.i, enemy.j + 1)) {
                    moveHorizontally(2, enemy);
                }
            }
        }
    }

    public void isEnemyGoalReached(Cell enemy) {
        if (enemy == null)
            return;

        if (enemy.i == hero.i && enemy.j == hero.j) {
            hero.setI(0);
            hero.setJ(0);
        }
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
