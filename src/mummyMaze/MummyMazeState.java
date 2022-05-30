package mummyMaze;

import agent.Action;
import agent.State;

import java.util.ArrayList;
import java.util.Arrays;

public class MummyMazeState extends State implements Cloneable {
    public static final int SIZE = 13;

    private char[][] matrix;
    private Cell hero, exit, scorpion;
    private ArrayList<Cell> doors, traps, whiteMummies, redMummies;

    public MummyMazeState(char[][] matrix) {
        this.matrix = new char[SIZE][SIZE];
        hero = exit = scorpion = null;
        doors = traps = whiteMummies = redMummies = null;

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
            case Cell.HERO -> hero = new Cell(i, j, matrix[i][j]);
            case Cell.EXIT -> exit = new Cell(i, j, matrix[i][j]);
            case Cell.SCORPION -> scorpion = new Cell(i, j, matrix[i][j]);
            case Cell.WHITE_MUMMY -> {
                if (whiteMummies == null) {
                    whiteMummies = new ArrayList<>();
                }
                whiteMummies.add(new Cell(i, j, matrix[i][j]));
            }
            case Cell.RED_MUMMY -> {
                if (redMummies == null) {
                    redMummies = new ArrayList<>();
                }
                redMummies.add(new Cell(i, j, matrix[i][j]));
            }
            case Cell.TRAP -> {
                if (traps == null) {
                    traps = new ArrayList<>();
                }
                traps.add(new Cell(i, j, matrix[i][j]));
            }
            case Cell.HORIZ_DOOR_CLOSED, Cell.HORIZ_DOOR_OPEN, Cell.VERT_DOOR_CLOSED, Cell.VERT_DOOR_OPEN -> {
                if (doors == null) {
                    doors = new ArrayList<>();
                }
                doors.add(new Cell(i, j, matrix[i][j]));
            }
        }
    }

    @Override
    public void executeAction(Action action) {
        action.execute(this);
        fireGameChanged();
    }

    private boolean hasWall(int line, int col) {
        return matrix[line][col] == Cell.HORIZ_WALL || matrix[line][col] == Cell.VERT_WALL ||
                matrix[line][col] == Cell.HORIZ_DOOR_CLOSED || matrix[line][col] == Cell.VERT_DOOR_CLOSED;
    }

    private boolean isCellSafe(int line, int col) {
        return matrix[line][col] == Cell.FLOOR || matrix[line][col] == Cell.KEY ||
                matrix[line][col] == Cell.HORIZ_DOOR_OPEN || matrix[line][col] == Cell.VERT_DOOR_OPEN;
    }

    public boolean canMoveUp() {
        return (hero.i > 1 && !hasWall(hero.i - 1, hero.j) && isCellSafe(hero.i - 2, hero.j))
                || (hero.i == 1 && matrix[hero.i - 1][hero.j] == Cell.EXIT);
    }

    public boolean canMoveDown() {
        return (hero.i < SIZE - 2 && !hasWall(hero.i + 1, hero.j) && isCellSafe(hero.i + 2, hero.j))
                || (hero.i == SIZE - 2 && matrix[hero.i + 1][hero.j] == Cell.EXIT);
    }

    public boolean canMoveRight() {
        return (hero.j < SIZE - 2 && !hasWall(hero.i, hero.j + 1) && isCellSafe(hero.i, hero.j + 2))
                || (hero.j == SIZE - 2 && matrix[hero.i][hero.j + 1] == Cell.EXIT);
    }

    public boolean canMoveLeft() {
        return (hero.j > 2 && !hasWall(hero.i, hero.j - 1) && isCellSafe(hero.i, hero.j - 2))
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
        moveEnemies();
    }

    private void moveHorizontally(int positionsToMove, Cell agent) {
        char objectiveCellType = matrix[agent.i][agent.j + positionsToMove];

        matrix[agent.i][agent.j] = Cell.FLOOR;
        matrix[agent.i][agent.j + positionsToMove] = agent.cellType;
        agent.setJ(agent.j + positionsToMove);

        checkDoors(agent, objectiveCellType);
        placeTrapBackInLevel();
    }

    private void moveVertically(int positionsToMove, Cell agent) {
        char objectiveCellType = matrix[agent.i + positionsToMove][agent.j];

        matrix[agent.i][agent.j] = Cell.FLOOR;
        matrix[agent.i + positionsToMove][agent.j] = agent.cellType;
        agent.setI(agent.i + positionsToMove);

        checkDoors(agent, objectiveCellType);
        placeTrapBackInLevel();
    }

    private void checkDoors(Cell agent, char oldCellType) {
        if (agent.cellType == Cell.HERO && oldCellType == Cell.KEY) {
            for (Cell door : doors) {
                switchDoorState(door);
            }
        }
    }

    private void mummiesFight(ArrayList<Cell> mummies) {
        if (mummies == null || mummies.size() < 2) {
            return;
        }

        for (int i = 0; i < mummies.size(); i++) {
            for (int j = 1; j < mummies.size() - 1; j++) {
                if (mummies.get(i).i == mummies.get(j).i && mummies.get(i).j == mummies.get(j).j) {
                    mummies.remove(i);
                }
            }
        }
    }

    private void moveEnemies() {
        if (isGoalReached())
            return;

        moveWhiteMummy();
        moveRedMummy();
        moveScorpion();

        mummiesFight(whiteMummies);
        mummiesFight(redMummies);
    }

    public Cell getHero() {
        return hero;
    }

    private void moveWhiteMummy() {
        if (whiteMummies == null || whiteMummies.size() == 0)
            return;

        for (Cell whiteMummy : whiteMummies) {
            if (whiteMummy.j != hero.j) {
                moveEnemyHorizontally(whiteMummy);
            } else {
                moveEnemyVertically(whiteMummy);
            }

            // Se a mumia matar o heroi, passar o heroi para a posição 0 (fora do nível)
            isEnemyGoalReached(whiteMummy);
        }
    }

    private void moveRedMummy() {
        if (redMummies == null || redMummies.size() == 0)
            return;

        for (Cell redMummy : redMummies) {
            if (redMummy.i != hero.i) {
                moveEnemyVertically(redMummy);
            } else {
                moveEnemyHorizontally(redMummy);
            }

            // Se a mumia matar o heroi, passar o heroi para a posição 0 (fora do nível)
            isEnemyGoalReached(redMummy);
        }
    }

    private void moveScorpion() {
        if (scorpion == null)
            return;

        if (scorpion.j != hero.j) {
            moveEnemyHorizontally(scorpion);
        } else {
            moveEnemyVertically(scorpion);
        }

        isEnemyGoalReached(scorpion);
    }

    private void moveEnemyVertically(Cell enemy) {
        boolean isMummy = enemy.cellType == Cell.WHITE_MUMMY || enemy.cellType == Cell.RED_MUMMY;
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

    private void moveEnemyHorizontally(Cell enemy) {
        boolean isMummy = enemy.cellType == Cell.WHITE_MUMMY || enemy.cellType == Cell.RED_MUMMY;
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

    public void switchDoorState(Cell doorCell) {
        if (doorCell == null)
            return;

        char doorType = doorCell.cellType;
        if (doorType != Cell.HORIZ_DOOR_CLOSED && doorType != Cell.HORIZ_DOOR_OPEN
                && doorType != Cell.VERT_DOOR_CLOSED && doorType != Cell.VERT_DOOR_OPEN) {
            return;
        }

        switch (doorType) {
            case Cell.HORIZ_DOOR_CLOSED -> matrix[doorCell.i][doorCell.j] = Cell.HORIZ_DOOR_OPEN;
            case Cell.HORIZ_DOOR_OPEN -> matrix[doorCell.i][doorCell.j] = Cell.HORIZ_DOOR_CLOSED;
            case Cell.VERT_DOOR_CLOSED -> matrix[doorCell.i][doorCell.j] = Cell.VERT_DOOR_OPEN;
            case Cell.VERT_DOOR_OPEN -> matrix[doorCell.i][doorCell.j] = Cell.VERT_DOOR_CLOSED;
        }
    }

    private void placeTrapBackInLevel(){
        if (traps == null || traps.size() == 0)
            return;

        for (Cell trap : traps) {
            if (matrix[trap.i][trap.j] == Cell.FLOOR){
                this.matrix[trap.i][trap.j] = Cell.TRAP;
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
        int h = 0;
        return h;
    }

    public double computeEnemyDistances() {
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
    private transient ArrayList<MummyMazeListener> listeners = new ArrayList<>(3);

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

    public void fireGameChanged() {
        for (MummyMazeListener listener : listeners) {
            listener.gameChanged(null);
        }
    }
}
