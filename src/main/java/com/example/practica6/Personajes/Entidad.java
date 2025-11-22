package com.example.practica6.Personajes;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Entidad {
    protected double x, y, width, height;
    protected double vida;
    protected double tiempoInvulnerable=0;


    public Entidad(double x, double y, double width, double height) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.vida =5;
    }

    public Entidad(double x, double y, double width, double height, int vida) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.vida =vida;
    }
    public boolean puedeRecibirDaño(){
        return tiempoInvulnerable<=0;
    }

    public void activarInvulnerabilidad(){
        tiempoInvulnerable=1;
    }

    public void actualizarInvulnerabilidad(double delta) {
        if (tiempoInvulnerable > 0)
            tiempoInvulnerable -= delta;
    }


    public double getVida(){return vida;}
    public void setVida(Double vida){this.vida = vida;}

    public abstract void update();
    public abstract void update(double delta);
    public abstract void draw(GraphicsContext gc);

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    // getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    //setters
    public void setX(double x){this.x=x;}

    public void setY(double y){this.y=y;}
}
