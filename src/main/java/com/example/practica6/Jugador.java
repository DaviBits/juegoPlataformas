package com.example.practica6;

 import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.Set;

public class Jugador extends Entidad {

    private static final int CAMINANDO=4;
    private static final int DASHEANDO=3;
    private static final int ATACANDO=2;
    private static final int SALTANDO=1;
    private static final int IDLE=0;
    private static final  double SCALE=1.5;




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

    private ArmaCuerpoCuerpo arma;
    private boolean atacando =false;
    //estados para las animaciones
    private String [] estados={"idle", "saltando", "atacando", "dasheando", "caminando"};
    private String estado;

    private Animacion idleAnim;
    private Animacion caminandoAnim;
    private Animacion saltarAnim;
    private Animacion atacarAnim;
    private Animacion dashAnim;

    public Jugador(double x, double y, double width, double height) {
        super(x,y,width*SCALE,height*SCALE);

        arma= new ArmaCuerpoCuerpo(x+width, y+width, 20, 15);
        this. estado=estados[0];
        //idle
        try{
           Image idleSheet = new Image(getClass().getResourceAsStream("/Sprites/Samurai/idle.png"));

            idleAnim=new Animacion(idleSheet, 30, 22, 3, 0.15);
        }catch (Exception e){sprite=null;}
        //moviendose
        try{
            Image caminandoSheet =new Image(getClass().getResourceAsStream("/Sprites/Samurai/run-sword.png"));

            caminandoAnim=new Animacion(caminandoSheet, 32, 32, 10, 0.15);
        }catch (Exception e){sprite=null;}

        //salto
        try{
            Image saltoSheet =new Image(getClass().getResourceAsStream("/Sprites/Samurai/jump.png"));

            saltarAnim=new Animacion(saltoSheet, 22, 22, 4, 0.15);
        }catch (Exception e){sprite=null;}

        //sash
        try{
            Image dashSheet =new Image(getClass().getResourceAsStream("/Sprites/Samurai/light-attack.png"));

            dashAnim=new Animacion(dashSheet, 44, 22, 7, 0.10);
        }catch (Exception e){sprite=null;}



        try {
            sprite = new Image("file:assets/images/player.png");
        } catch (Exception e) { sprite = null; }
    }

    public void dibujarArma(GraphicsContext gc){
       if(atacando){
           arma.draw(gc);
            atacando=false;
       }
    }
    public boolean verificarColisionArma(Rectangle2D enemigoBounds) {
        return arma.getBounds().intersects(enemigoBounds);
    }


    public boolean verificarColisionArma(double enX,double enY ){
        if(enX>=arma.getX()&&enX<= arma.getX()+ arma.getWidth()){
            System.out.println("Colision arma-enemigo");
            return true;
        }
        return false;
    }

    public void actualizarArma(){
        atacando=true;
        if(izquierda){
            arma.setX(x-width/2);
            arma.setY(y+width/2);
        }
        if(derecha){
            arma.setX(x+width);
            arma.setY(y+width/2);
        }
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
        estado=estados[SALTANDO];

    }


    public void applyGravity() {
        if (!haciendoDash) {
            velY += 0.5;
        }

        y += velY;

        if (y > 1000) { vivo = false; }
    }



    public void dash() {
        if (!puedeDashear || haciendoDash) return;

        haciendoDash = true;
        dashTiempo = duracionDash;

        velY = 0;

        velX = direccion * dashVelocidad;
        estado= estados[DASHEANDO];
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

    public void downAtack(){
        if(!enSuelo){
            velY+=15;
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

        if (izquierda || derecha) {
            estado = estados[CAMINANDO];
        } else {
            estado = estados[IDLE];
        }

    }

    @Override
    public void update() {
        // could add animations
        if (!haciendoDash && enSuelo) {
            if (izquierda || derecha) {
                estado = estados[CAMINANDO];
            } else {
                estado = estados[IDLE];
            }
        }
         double delta=0.032;

        switch (estado) {
            case "idle":
                if(idleAnim!=null){
                    idleAnim.update(delta);
                }
                break;
            case "saltando":
                break;
            case "atacando":
                break;
            case "dasheando":
                break;
            case "caminando":
                if(caminandoAnim!=null){
                    caminandoAnim.update(delta);
                }
                break;
        }
    }
    @Override
    public void update(double delta) {

        // NO sobreescribir estados importantes
        if (!estado.equals("saltando") &&
                !estado.equals("atacando") &&
                !estado.equals("dasheando")) {

            if (enSuelo) {
                if (izquierda || derecha) {
                    estado = estados[CAMINANDO];
                } else {
                    estado = estados[IDLE];
                }
            }
        }

        // actualizar animación según estado
        switch (estado) {
            case "idle":
                if(idleAnim != null) idleAnim.update(delta);
                break;
            case "caminando":
                if(caminandoAnim != null) caminandoAnim.update(delta);
                break;
            case "saltando":
                if(saltarAnim != null) saltarAnim.update(delta);
                break;
            case "atacando":
                if(atacarAnim != null) atacarAnim.update(delta);
                break;
            case "dasheando":
                if(dashAnim != null) dashAnim.update(delta);
                break;
        }
    }



    @Override
    public void draw(GraphicsContext gc) {

        boolean flipX = izquierda; // si mira a la izquierda, voltea sprite

        switch (estado) {
            case "idle":
                if (idleAnim != null) {
                    idleAnim.draw(gc, x, y, width, height, flipX);
                    return;
                }
                break;

            case "caminando":
                if (caminandoAnim != null) {
                    caminandoAnim.draw(gc, x, y, width, height, flipX);
                    return;
                }
                break;

            case "saltando":
                if (saltarAnim != null) {
                    saltarAnim.draw(gc, x, y, width, height, flipX);
                    return;
                }
                break;

            case "atacando":
                if (atacarAnim != null) {
                    atacarAnim.draw(gc, x, y, width, height, flipX);
                    return;
                }
                break;

            case "dasheando":
                if (dashAnim != null) {
                    dashAnim.draw(gc, x, y, width, height, flipX);
                    return;
                }
                break;
        }

        // si no hay animación, dibuja un rectángulo (fallback)
        gc.setFill(Color.BLUE);
        gc.fillRect(x, y, width, height);
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
