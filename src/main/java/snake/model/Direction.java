package model;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;
 
    public int getDx() {
        if (this == LEFT) return -1;
        if (this == RIGHT) return 1;
        return 0;
    }
 
    public int getDy() {
        if (this == UP) return -1;
        if (this == DOWN) return 1;
        return 0;
    }
 
    public boolean isOpposite(Direction other) {
        if (this == UP && other == DOWN) return true;
        if (this == DOWN && other == UP) return true;
        if (this == LEFT && other == RIGHT) return true;
        if (this == RIGHT && other == LEFT) return true;
        return false;
    }
}
