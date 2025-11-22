package com.example.practica6.Personajes;

public abstract class Enemigo extends Entidad {
    protected double velX = 0;
    protected int puntaje=20;
    protected double daño;

    public Enemigo(double x, double y, double width, double height, double velX, int vida) {
        super(x,y,width,height, vida);
        this.velX = velX;
    }

    public Enemigo(double x, double y, double width, double height, double velX, int vida, double daño) {
        super(x,y,width,height, vida);
        this.velX = velX;
        this.daño=daño;
    }


    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
    public int getPuntaje() {
        return puntaje;
    }
    public void setDaño(double daño) {
        this.daño=daño;
    }
    public double getDaño(){
        return daño;
    }

    public double getCorazones(){return vida;}

    public void setCorazones(double corazones){this.vida=corazones;}

}
