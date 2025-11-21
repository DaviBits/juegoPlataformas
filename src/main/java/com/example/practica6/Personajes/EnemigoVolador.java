package com.example.practica6.Personajes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class EnemigoVolador extends Enemigo {
    private double baseY;
    private Image sprite;
    public EnemigoVolador(double x, double y, double width, double height, double velX, int vida) {
        super(x,y,width,height,velX, vida);
        this.baseY = y;
        try { sprite = new Image("file:assets/images/enemy_fly.png"); } catch (Exception e) { sprite = null; }
    }
    public EnemigoVolador(double x, double y, double width, double height, double velX, int vida,double daño) {
        super(x,y,width,height,velX, vida);
        this.baseY = y;
        try { sprite = new Image("file:assets/images/enemy_fly.png"); } catch (Exception e) { sprite = null; }
    }

    @Override
    public void update() {
        x += velX;
        y = baseY + Math.sin(x * 0.05) * 20;
        if (x < -50) x = 850;
        if (x > 850) x = -50;
    }
    @Override
    public void update(double delta){
        x += velX;
        y = baseY + Math.sin(x * 0.05) * 20;
        if (x < -50) x = 850;
        if (x > 850) x = -50;

    }

    @Override
    public void draw(GraphicsContext gc) {
        if (sprite != null) gc.drawImage(sprite, x, y, width, height);
        else {
            gc.setFill(Color.ORANGE);
            gc.fillOval(x,y,width,height);
        }
    }
}
