package com.example.practica6.Entorno;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Pared extends Plataforma {


    public Pared(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public Rectangle2D getBounds() { return new Rectangle2D(x,y,width,height); }
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x,y,width,height);
    }
    public double getY() { return y; }


}
