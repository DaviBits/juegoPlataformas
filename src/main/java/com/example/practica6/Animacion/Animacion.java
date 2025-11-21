package com.example.practica6.Animacion;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Animacion {
    private Image sheet;
    private int frameWidth, frameHeight, frames;
    private int frame = 0;
    private double timer = 0;
    private double speed;

    private final double SCALE=1.5;

    public Animacion(Image sheet, int fw, int fh, int frames, double speed) {
        this.sheet = sheet;
        this.frameWidth = fw;
        this.frameHeight = fh;
        this.frames = frames;
        this.speed = speed;
    }

    public void update(double delta) {
        timer += delta;
        if (timer >= speed) {
            frame = (frame + 1) % frames;
            timer = 0;
        }
    }

    public void draw(GraphicsContext gc, double x, double y, double w, double h, boolean flipX) {
        int sx = frame * frameWidth;
        int sy = 0;

        if (!flipX) {
            gc.drawImage(sheet,
                    sx, sy, frameWidth, frameHeight,  // recorte
                    x, y, w, h                        // destino
            );
        } else {
            gc.drawImage(sheet,
                    sx, sy, frameWidth, frameHeight,
                    x + w, y, -w, h                  // flip horizontal
            );
        }
    }
}
