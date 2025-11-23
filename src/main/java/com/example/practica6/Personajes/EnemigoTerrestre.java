package com.example.practica6.Personajes;

import com.example.practica6.Animacion.Animacion;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class EnemigoTerrestre extends Enemigo {
    private Image sprite;
    private Animacion walkAnim;
    private double velXAnterior;
    protected boolean flipX = false;

    public EnemigoTerrestre(double x, double y, double width, double height, double velX, int vida) {
        super(x,y,width,height,velX, vida);
        cargarAnimacion();
        this.velXAnterior = velX;

    }

    public EnemigoTerrestre(double x, double y, double width, double height, double velX, int vida, double daño) {
        super(x,y,width,height,velX, vida, daño);

        cargarAnimacion();

        this.velXAnterior = velX;


    }
    private void cargarAnimacion() {
        try {
            Image idleSheet = new Image(getClass().getResourceAsStream(
                    "/EnemigoTerrestre/SkeletonWalk.png"));
            walkAnim = new Animacion(idleSheet, 22, 33, 13, 0.30);
        } catch (Exception e) {
            walkAnim = null;
        }
    }


    @Override
    public void update() {
        x += velX;
        if (x < 0 || x + width > 800) velX *= -1;
    }
    @Override
    public void update(double delta){
        x += velX;
        if (x < 0 || x + width > 2000) velX *= -1;
    }


    public void update(int x1, int x2) {

        // Guardamos la velocidad ANTES del movimiento
        double velXAnteriorFrame = velX;

        // Mover
        x += velX;

        boolean cambio = false;

        // Límite izquierdo
        if (x < x1) {
            x = x1;
            velX *= -1;
            cambio = true;
        }

        // Límite derecho
        if (x + width > x2) {
            x = x2 - width;
            velX *= -1;
            cambio = true;
        }

        // Si rebotó y el signo cambió → voltear sprite
        if (cambio && Math.signum(velX) != Math.signum(velXAnteriorFrame)) {
            flipX = !flipX;
        }

        // Ahora sí, actualizar velXAnterior
        velXAnterior = velX;
    }
    public void update(int x1, int x2, double delta) {

        walkAnim.update(delta);

        // Guardamos la velocidad ANTES del movimiento
        double velXAnteriorFrame = velX;

        x += velX;

        boolean cambio = false;

        if (x < x1) {
            x = x1;
            velX *= -1;
            cambio = true;
        }

        if (x + width > x2) {
            x = x2 - width;
            velX *= -1;
            cambio = true;
        }

        if (cambio && Math.signum(velX) != Math.signum(velXAnteriorFrame)) {
            flipX = !flipX;
        }

        velXAnterior = velX;
    }


    @Override
    public void draw(GraphicsContext gc) {
        walkAnim.draw(gc, x, y, width, height, flipX);
    }
}
