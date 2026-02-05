package models;

import java.awt.*;

public class Line {
    private Point p1, p2;
    private final Color color = Color.CYAN;
    private final boolean isDotted;

    public Line(Point p1, Point p2, boolean dotted, boolean straight) {
        this.p1 = p1;
        this.p2 = p2;
        this.isDotted = dotted;

        if (!straight) return;

        int dx = p2.getX() - p1.getX();
        int dy = p2.getY() - p1.getY();
        double ratio = Math.abs((double)dy / dx);

        if (ratio < Math.tan(Math.PI / 8)) {
            p2.setY(p1.getY()); // Horizontal
        } else if (ratio > Math.tan(3 * Math.PI / 8)) {
            p2.setX(p1.getX()); // Vertical
        } else {
            // Diagonal
            int len = Math.max(Math.abs(dx), Math.abs(dy));
            p2.setX(p1.getX() + (dx > 0 ? len : -len));
            p2.setY(p1.getY() + (dy > 0 ? len : -len));
        }
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public Color getColor() {
        return color;
    }

    public boolean isDotted() {
        return isDotted;
    }
}
