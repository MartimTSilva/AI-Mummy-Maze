package mummyMaze;

public class Agent extends Cell {
    boolean isAlive;

    public Agent(int i, int j, char cellType) {
        super(i, j, cellType);
        this.isAlive = true;
    }

    public void killAgent() {
        this.isAlive = false;
    }
}