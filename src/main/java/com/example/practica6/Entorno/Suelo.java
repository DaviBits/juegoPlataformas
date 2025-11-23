package com.example.practica6.Entorno;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Suelo extends Plataforma {

    // Textura base (piedra o tierra)
    private static final Image TILE =
            new Image(Suelo.class.getResourceAsStream("/Plataformas/platforms10.png"));

    // Textura superior (césped)
    private static final Image GRASS =
            new Image(Suelo.class.getResourceAsStream("/Plataformas/grass1.png"));

    public Suelo(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    @Override
    public void draw(GraphicsContext gc) {

        gc.setFill(Color.web("#19121E"));
        gc.fillRect(x,y,width,height);

        double tileW = TILE.getWidth()*3;
        double tileH = TILE.getHeight()*3;

        double grassW = GRASS.getWidth()*3;
        double grassH = GRASS.getHeight()*3;

        // ===== 1. Dibujar capa de piedra / base =====
        for (double yy = 0; yy < height; yy += tileH) {
            for (double xx = 0; xx < width; xx += tileW) {
                gc.drawImage(TILE, x + xx, y + yy, tileW, tileH);
            }
        }

        // ===== 2. Dibujar una línea de césped encima =====
        for (double xx = 0; xx < width; xx += grassW) {
            gc.drawImage(GRASS, x + xx, y - grassH + 2, grassW, grassH);
        }
    }

    public double getY() { return y; }
}

/*
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x,y,width,height);
 */
