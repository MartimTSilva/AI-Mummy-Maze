package mummyMaze;

public class Cell {
    int i, j;
    char cellType;
    final static char FLOOR = '.';
    final static char HERO = 'H';
    final static char EXIT = 'S';
    final static char WHITE_MUMMY = 'M';
    final static char RED_MUMMY = 'V';
    final static char TRAP = 'A';
    final static char SCORPION = 'E';
    final static char KEY = 'C';
    final static char HORIZ_DOOR_OPEN = '_';
    final static char HORIZ_DOOR_CLOSED = '=';
    final static char VERT_DOOR_OPEN = ')';
    final static char VERT_DOOR_CLOSED = '"';
    final static char VERT_WALL = '|';
    final static char HORIZ_WALL = '-';

    public Cell(int i, int j, char cellType) {
        this.i = i;
        this.j = j;
        this.cellType = cellType;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }
}