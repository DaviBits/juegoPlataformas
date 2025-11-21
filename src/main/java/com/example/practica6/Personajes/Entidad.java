package com.example.practica6.Personajes;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Entidad {
    protected double x, y, width, height;
    protected int corazones;
    protected double tiempoInvulnerable=0;


    public Entidad(double x, double y, double width, double height) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.corazones=5;
    }

    public Entidad(double x, double y, double width, double height, int corazones) {
        this.x = x; this.y = y; this.width = width; this.height = height;
        this.corazones=corazones;
    }
    public boolean puedeRecibirDaño(){
        return tiempoInvulnerable>=0;
    }

    public void activarInvulnerabilidad(){
        tiempoInvulnerable=0.3;
    }

    public void actualizarInvulnerabilidad(double delta) {
        if (tiempoInvulnerable > 0)
            tiempoInvulnerable -= delta;
    }


    public int getCorazones(){return corazones;}
    public void setCorazones(int corazones){this.corazones=corazones;}

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
}
