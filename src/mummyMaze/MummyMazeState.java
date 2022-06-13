package mummyMaze;

import agent.Action;
import agent.State;

import java.util.ArrayList;
import java.util.Arrays;

public class MummyMazeState extends State implements Cloneable {
    public static final int SIZE = 13;

    private char[][] matrix;
    private Agent hero;
    private Cell exit;
    private ArrayList<Cell> doors, keys, traps;
    private ArrayList<Agent> whiteMummies, redMummies, scorpions;

    public MummyMazeState(char[][] matrix, Cell exit, ArrayList<Cell> doors, ArrayList<Cell> keys, ArrayList<Cell> traps) {
        this.matrix = new char[SIZE][SIZE];
        hero = null;
        whiteMummies = redMummies = scorpions = null;

        if (matrix == null)
            return;

        //Deep copy da matriz
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];
                this.getElementPosition(i, j);
            }
        }

        this.exit = exit;
        this.doors = doors;
        this.keys = keys;
        this.traps = traps;
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
            case Cell.HERO -> hero = new Agent(i, j, matrix[i][j]);
            case Cell.WHITE_MUMMY -> {
                if (whiteMummies == null) {
                    whiteMummies = new ArrayList<>();
                }
                whiteMummies.add(new Agent(i, j, matrix[i][j]));
            }
            case Cell.RED_MUMMY -> {
                if (redMummies == null) {
                    redMummies = new ArrayList<>();
                }
                redMummies.add(new Agent(i, j, matrix[i][j]));
            }
            case Cell.SCORPION -> {
                if (scorpions == null) {
                    scorpions = new ArrayList<>();
                }
                scorpions.add(new Agent(i, j, matrix[i][j]));
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

    private void moveHorizontally(int positionsToMove, Agent agent) {
        char objectiveCellType = matrix[agent.i][agent.j + positionsToMove];

        if (agent.cellType != Cell.HERO)
            if (fightEnemies(agent, objectiveCellType))
                return;

        matrix[agent.i][agent.j] = Cell.FLOOR;
        matrix[agent.i][agent.j + positionsToMove] = agent.cellType;
        agent.setJ(agent.j + positionsToMove);

        checkDoors(objectiveCellType);
        placeElementBackInLevel(traps, Cell.TRAP);
        placeElementBackInLevel(keys, Cell.KEY);
    }

    private void moveVertically(int positionsToMove, Agent agent) {
        char objectiveCellType = matrix[agent.i + positionsToMove][agent.j];

        if (agent.cellType != Cell.HERO)
            if (fightEnemies(agent, objectiveCellType))
                return;

        matrix[agent.i][agent.j] = Cell.FLOOR;
        matrix[agent.i + positionsToMove][agent.j] = agent.cellType;
        agent.setI(agent.i + positionsToMove);

        checkDoors(objectiveCellType);
        placeElementBackInLevel(traps, Cell.TRAP);
        placeElementBackInLevel(keys, Cell.KEY);
    }

    private boolean fightEnemies(Agent enemy, char objectiveCellType) {
        if (objectiveCellType == Cell.WHITE_MUMMY || objectiveCellType == Cell.RED_MUMMY || objectiveCellType == Cell.SCORPION) {
            matrix[enemy.i][enemy.j] = Cell.FLOOR;
            enemy.killAgent();
            return true;
        }
        return false;
    }

    private void checkDoors(char objectiveCellType) {
        if (objectiveCellType == Cell.KEY) {
            for (Cell door : doors) {
                switchDoorState(door);
            }
        }
    }

    private void moveEnemies() {
        if (isGoalReached() || isHeroDead())
            return;

        moveWhiteMummies();
        moveRedMummies();
        moveScorpions();

        removeDeadEnemies();
    }

    private void removeDeadEnemies() {
        if (whiteMummies != null && whiteMummies.size() > 0)
            whiteMummies.removeIf(enemy -> !enemy.isAlive);

        if (redMummies != null && redMummies.size() > 0)
            redMummies.removeIf(enemy -> !enemy.isAlive);

        if (scorpions != null && scorpions.size() > 0)
            scorpions.removeIf(enemy -> !enemy.isAlive);
    }

    private void moveWhiteMummies() {
        if (whiteMummies == null || whiteMummies.size() == 0)
            return;

        boolean heroDead = false;
        for (Agent whiteMummy : whiteMummies) {
            for (int i = 0; i < 2; i++) {
                moveEnemyHorizontally(whiteMummy);

                // Se a mumia matar o heroi, passar o heroi para a posição 0 (fora do nível)
                heroDead = isEnemyGoalReached(whiteMummy);

                if (heroDead)
                    break;
            }
            if (heroDead)
                break;
        }
    }

    private void moveRedMummies() {
        if (redMummies == null || redMummies.size() == 0)
            return;

        boolean heroDead = false;
        for (Agent redMummy : redMummies) {
            for (int i = 0; i < 2; i++) {
                moveEnemyVertically(redMummy);

                // Se a mumia matar o heroi, passar o heroi para a posição 0 (fora do nível)
                heroDead = isEnemyGoalReached(redMummy);
                if (heroDead)
                    break;
            }
            if (heroDead)
                break;
        }
    }

    private void moveScorpions() {
        if (scorpions == null || scorpions.size() == 0)
            return;

        for (Agent scorpion : scorpions) {
            moveEnemyHorizontally(scorpion);

            // Se a mumia matar o heroi, passar o heroi para a posição 0 (fora do nível)
            if (isEnemyGoalReached(scorpion)) {
                break;
            }
        }
    }

    private void moveEnemyHorizontally(Agent enemy) {
        boolean isRedMummy = enemy.cellType == Cell.RED_MUMMY;

        int diff = hero.j - enemy.j;
        if (diff < 0 && !hasWall(enemy.i, enemy.j - 1) && enemy.j > 2) {
            moveHorizontally(-2, enemy);
        } else if (diff > 0 && !hasWall(enemy.i, enemy.j + 1) && enemy.j < SIZE - 2) {
            moveHorizontally(2, enemy);
        } else if (!isRedMummy) {
            moveEnemyVertically(enemy);
        }
    }

    private void moveEnemyVertically(Agent enemy) {
        boolean isRedMummy = enemy.cellType == Cell.RED_MUMMY;
        int diff = hero.i - enemy.i;
        if (diff < 0 && !hasWall(enemy.i - 1, enemy.j) && enemy.i > 2) {
            moveVertically(-2, enemy);
        } else if (diff > 0 && !hasWall(enemy.i + 1, enemy.j) && enemy.i < SIZE - 2) {
            moveVertically(2, enemy);
        } else if (isRedMummy) {
            moveEnemyHorizontally(enemy);
        }
    }

    private void switchDoorState(Cell doorCell) {
        if (doorCell == null)
            return;

        switch (matrix[doorCell.i][doorCell.j]) {
            case Cell.HORIZ_DOOR_CLOSED -> matrix[doorCell.i][doorCell.j] = Cell.HORIZ_DOOR_OPEN;
            case Cell.HORIZ_DOOR_OPEN -> matrix[doorCell.i][doorCell.j] = Cell.HORIZ_DOOR_CLOSED;
            case Cell.VERT_DOOR_CLOSED -> matrix[doorCell.i][doorCell.j] = Cell.VERT_DOOR_OPEN;
            case Cell.VERT_DOOR_OPEN -> matrix[doorCell.i][doorCell.j] = Cell.VERT_DOOR_CLOSED;
        }
    }

    private void placeElementBackInLevel(ArrayList<Cell> cells, char elementChar){
        if (cells == null || cells.size() == 0)
            return;

        for (Cell cell : cells) {
            if (matrix[cell.i][cell.j] == Cell.FLOOR) {
                matrix[cell.i][cell.j] = elementChar;
            }
        }
    }

    private boolean isEnemyGoalReached(Cell enemy) {
        if (enemy == null)
            return false;

        if (enemy.i == hero.i && enemy.j == hero.j) {
            hero.setI(0);
            hero.setJ(0);
            hero.killAgent();
            return true;
        }
        return false;
    }

    public boolean isGoalReached() {
        return hero.i == exit.i && hero.j == exit.j;
    }

    private boolean isHeroDead() {
        return (hero.j == 0 && hero.i == 0) || !hero.isAlive;
    }

    public Cell getHero() {
        return hero;
    }

    private double distanceToHero(Cell cell) {
        return Math.abs(hero.j - cell.j) + Math.abs(hero.i - cell.i);
    }

    public double computeGoalDistance() {
        if (isHeroDead())
            return Double.MAX_VALUE;

        return distanceToHero(exit);
    }

    public double computeEnemyDistances() {
        if (isHeroDead())
            return Double.MAX_VALUE;

        if (isGoalReached())
            return 0;

        int maxDistance = (SIZE - 3) * 2; //Começar a contar numa posição (-2) e não contar parte de fora do maze (-1)
        double h = maxDistance;
        double aux = Double.MAX_VALUE;

        if (whiteMummies != null) {
            for (Agent wMummy : whiteMummies) {
                if (!wMummy.isAlive)
                    continue;

                // Verificar se o inimigo está mais perto que o anterior
                aux = distanceToHero(wMummy);
                if (aux < h)
                    h = aux;    //Se a distancia para o inimigo atual for menor que o anterior, h passa a ter esse valor
            }
        }

        if (redMummies != null) {
            for (Agent rMummy : redMummies) {
                if (!rMummy.isAlive)
                    continue;

                aux = distanceToHero(rMummy);
                if (aux < h)
                    h = aux;
            }
        }

        if (scorpions != null) {
            for (Agent scorpion : scorpions) {
                if (!scorpion.isAlive)
                    continue;

                aux = distanceToHero(scorpion);
                if (aux < h)
                    h = aux;
            }
        }

        //Se (aux > max) quer dizer que aux está com valor inf porque não havia enemigos, logo devolve 0
        return aux > maxDistance ? 0 : maxDistance - h;
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
        return new MummyMazeState(matrix, exit, doors, keys, traps);
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
