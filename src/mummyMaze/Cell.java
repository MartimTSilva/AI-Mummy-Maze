package mummyMaze;

public class Cell {
    int i, j;
    final char cellType;
    final static int FLOOR = '.';
    final static int HERO = 'H';
    final static int EXIT = 'S';
    final static int WHITE_MUMMY = 'M';
    final static int RED_MUMMY = 'V';
    final static int TRAP = 'A';
    final static int SCORPION = 'E';
    final static int KEY = 'C';
    final static int HORIZ_DOOR_OPEN = '_';
    final static int HORIZ_DOOR_CLOSED = '=';
    final static int VERT_DOOR_OPEN = '"';
    final static int VERT_DOOR_CLOSED = '(';
    final static int VERT_WALL = '|';
    final static int HORIZ_WALL = '-';


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