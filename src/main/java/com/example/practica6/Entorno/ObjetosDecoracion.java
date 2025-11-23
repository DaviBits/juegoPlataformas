package com.example.practica6.Entorno;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ObjetosDecoracion {

    protected double x, y, width, height;

    private static final Image ROCKS1 =
            new Image(ObjetosDecoracion.class.getResourceAsStream("/DecoracionesEntorno/rocks1.png"));
    private static final Image ROCKS2 =
            new Image(ObjetosDecoracion.class.getResourceAsStream("/DecoracionesEntorno/rocks2.png"));
    private static final Image ROCKS3 =
            new Image(ObjetosDecoracion.class.getResourceAsStream("/DecoracionesEntorno/rocks3.png"));
    private static final Image ROCKS4 =
            new Image(ObjetosDecoracion.class.getResourceAsStream("/DecoracionesEntorno/rocks4.png"));
    private static final Image ROCKS5 =
            new Image(ObjetosDecoracion.class.getResourceAsStream("/DecoracionesEntorno/rocks5.png"));
    private static final Image ROCKS6 =
            new Image(ObjetosDecoracion.class.getResourceAsStream("/DecoracionesEntorno/rocks6.png"));

    private static final Image TREE1 =
            new Image(ObjetosDecoracion.class.getResourceAsStream("/DecoracionesEntorno/tree01.png"));
    private static final Image TREE2 =
            new Image(ObjetosDecoracion.class.getResourceAsStream("/DecoracionesEntorno/tree02.png"));
    private static final Image TREE3 =
            new Image(ObjetosDecoracion.class.getResourceAsStream("/DecoracionesEntorno/tree03.png"));
    private static final Image TREE4 =
            new Image(ObjetosDecoracion.class.getResourceAsStream("/DecoracionesEntorno/tree04.png"));


    private static final Image[] OBJETOS = {
            ROCKS1, ROCKS2, ROCKS3, ROCKS4, ROCKS5, ROCKS6,
            TREE1, TREE2, TREE3, TREE4
    };

    private final Image imagen;
    private final static int SCALE=1;

    public ObjetosDecoracion(double x, double y, double width, double height, int id) {
        this.x = x;
        this.y = y;
        this.width = width*SCALE;
        this.height = height*SCALE;

        this.imagen = OBJETOS[id];   // ← AQUÍ RECIBES LA IMAGEN REAL
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(imagen, x, y, width, height);
    }

    public double getWidth(){return width;}
    public double getHeight(){return height;}
    public double getY(){return y;}

    public void setY(double y){this.y=y;}

    public double getX() {return x;
    }
}
