package com.example.practica6.Entorno;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Corazon {
    private double x, y, width, height;
    private Image sprite;

    public Corazon(double x, double y, double width, double height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;

        try {
            sprite =new Image(getClass().getResourceAsStream("/corazon.png"));

        }catch (Exception e){
            sprite=null;
        }
    }

    public void draw(GraphicsContext gc) {
        if (sprite != null) gc.drawImage(sprite, x, y, width, height);
        else {
            gc.setFill(Color.ORANGE);
            gc.fillOval(x,y,width,height);
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public void setY(double Y){this.y=Y;}

    public double getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }
}
