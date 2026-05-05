package model;

public class Board {
    private int width;
    private int height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= width || y >= height;
    }
}