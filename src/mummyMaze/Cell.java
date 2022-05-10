package mummyMaze;

import java.util.Objects;

public class Cell {
    int i, j;
    char floor;

    public Cell(int i, int j, char floor) {
        this.i = i;
        this.j = j;
        this.floor = floor;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }
}