package com.example.practica6;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.Set;

public class Jugador extends Entidad {
    private double velY = 0;
    private double velX = 0;
    private boolean enSuelo = false;
    private int contadorSaltos = 2;
    private boolean dobleSalto = true;
    private int puntaje = 0;
    private boolean vivo = true;
    private Image sprite;
    private boolean izquierda = false;
    private boolean derecha = true;

    //cosas para hacer el dash
    private boolean haciendoDash=false;
    private final double duracionDash=0.15;
    private double dashTiempo=0;
    private final double  dashVelocidad=12;
    private boolean puedeDashear=true;
    private int direccion = 1;


    public Jugador(double x, double y, double width, double height) {
        super(x,y,width,height);
        try {
            sprite = new Image("file:assets/images/player.png");
        } catch (Exception e) { sprite = null; }
    }

    public void moverIzquierda() {
        izquierda=true;
        derecha=false;
        x -= 5;
        if (x < 0){
            x = 0;
        }
    }
    public void moverDerecha() {
        izquierda=false;
        derecha=true;
        x += 5;
        if (x + width > 800){
            x = 800 - width;
        }
    }

    public void saltar() {

        if(contadorSaltos!=0){
            velY-=10;
            enSuelo=false;
            contadorSaltos--;
        }

    }


    public void applyGravity() {
        if (!haciendoDash) {
            velY += 0.5;
        }

        y += velY;

        if (y > 1000) { vivo = false; }
    }


    public void acelerate(){
        velX-=50;
        x+=velX;

    }

    public void dash() {
        if (!puedeDashear || haciendoDash) return;

        haciendoDash = true;
        dashTiempo = duracionDash;

        velY = 0;

        velX = direccion * dashVelocidad;

        puedeDashear = false;
    }


    public void updateDash(double delta) {
        if (haciendoDash) {
            dashTiempo -= delta;

            x += velX;

            if (dashTiempo <= 0) {
                haciendoDash = false;
                velX = 0;
            }
        }
    }



    public void landOn(Plataforma p) {
        // simple landing: place on top
        y = p.getY() - height;
        velY = 0;
        contadorSaltos=2;
        enSuelo = true;
        dobleSalto=true;
        puedeDashear=true;
    }

    @Override
    public void update() {
        // could add animations
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (sprite != null) {
            gc.drawImage(sprite, x, y, width, height);
        } else {
            gc.setFill(Color.BLUE);
            gc.fillRect(x,y,width,height);
        }
    }

    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int p) { this.puntaje = p; }
    public void addPuntaje(int v) { this.puntaje += v; }

    public void setEnSuelo(boolean v) { this.enSuelo = v; }
    public boolean isEnSuelo() { return enSuelo; }

    public void setVivo(boolean v) { this.vivo = v; }
    public boolean isVivo() { return vivo; }

    public void setVelY(double v) { this.velY = v; }

    public void setDireccion(int i) {
        this.direccion=i;
    }
}
