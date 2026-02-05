package models;

import utils.Config;

public final class Point {
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = Math.clamp(x, 0, Config.windowSize.x - 1);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = Math.clamp(y, 0, Config.windowSize.y - 1);
    }

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point other) {
        this.x = other.x;
        this.y = other.y;
    }
}
