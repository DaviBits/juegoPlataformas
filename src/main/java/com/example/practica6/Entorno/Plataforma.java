package com.example.practica6.Entorno;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Plataforma {
    protected double x, y, width, height;

    public Plataforma(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }
    public boolean intersecta(Rectangle2D r) {
        return getBounds().intersects(r);
    }
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, width, height);

    }

    public boolean intersectaHorizontal(Rectangle2D r) {
        if (!intersecta(r)) return false;

        double dxLeft  = Math.abs((r.getMinX() + r.getWidth()) - this.x);
        double dxRight = Math.abs(r.getMinX() - (this.x + this.width));

        return dxLeft < r.getWidth() || dxRight < r.getWidth();
    }

    public boolean intersectaVertical(Rectangle2D r) {
        if (!intersecta(r)) return false;

        double dyTop    = Math.abs((r.getMinY() + r.getHeight()) - this.y);
        double dyBottom = Math.abs(r.getMinY() - (this.y + this.height));

        return dyTop < r.getHeight() || dyBottom < r.getHeight();
    }
    public boolean esPared(Rectangle2D r) {
        if (!intersecta(r)) return false;

        double overlapX = Math.min(
                Math.abs((r.getMinX() + r.getWidth()) - this.x),
                Math.abs(r.getMinX() - (this.x + this.width))
        );

        double overlapY = Math.min(
                Math.abs((r.getMinY() + r.getHeight()) - this.y),
                Math.abs(r.getMinY() - (this.y + this.height))
        );

        return overlapX < overlapY;
    }

    public boolean esPiso(Rectangle2D r) {
        if (!intersecta(r)) return false;

        double dy = Math.abs((r.getMinY() + r.getHeight()) - this.y);
        return dy < 10;
    }


    public double getY() { return y; }
    public double getX() { return x; }
    public double getHeight() { return height; }
    public double getWidth() { return width; }
}
