package com.example.practica6.Niveles;

import com.example.practica6.ArchivoJuego;
import com.example.practica6.Entorno.Plataforma;
import com.example.practica6.Entorno.Suelo;
import com.example.practica6.Entorno.Techo;
import com.example.practica6.Personajes.*;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Nv1 {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final int width;
    private final int height;

    private Jugador jugador;
    private List<Entidad> entidades;
    private List<Plataforma> plataformas;
    private Set<KeyCode> keys = new HashSet<>();
    private ArchivoJuego archivoJuego;

    private AnimationTimer loop;
    private double cameraX;

    public Nv1(int width, int height) {
        this.width = width;
        this.height = height;
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        init();
    }

    public Canvas getCanvas() { return canvas; }

    private void init() {
        archivoJuego = new ArchivoJuego("datos/progreso.txt");
        entidades = new ArrayList<>();
        plataformas = new ArrayList<>();

        jugador = new Jugador(50, 500, 40, 60);
        entidades.add(jugador);

        // Plataformas (suelo + dos plataformas elevadas)
        generarObjetos();


        // Enemigos




        // Setup loop
        loop = new AnimationTimer() {
            private long last = 0;
            @Override
            public void handle(long now) {
                if (last == 0) last = now;
                double delta = (now - last) / 1e9;
                actualizar(delta);
                dibujar();
                last = now;
            }
        };

        // try to load previous progress
        try {
            ArchivoJuego.Progreso p = archivoJuego.cargar();
            if (p != null) {
                jugador.setPuntaje(p.puntaje);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public void setupInput(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            keys.add(e.getCode());
            if (e.getCode() == KeyCode.S) {
                guardar();
            }
            if (e.getCode() == KeyCode.SPACE) {
                jugador.saltar();   // <-- SOLO SE DISPARA UNA VEZ
            }
            if (e.getCode() == KeyCode.X) {
                jugador.dash();
            }

            if(e.getCode() == KeyCode.C){
                jugador.downAtack();
            }



            if (e.getCode() == KeyCode.RIGHT) jugador.setDireccion(1);
            if (e.getCode() == KeyCode.LEFT) jugador.setDireccion(-1);


        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> keys.remove(e.getCode()));
    }

    //EL SALTO DEBE ESTAR EN OTRO METODO POR EL DOBLE SALTO, NO MOVER

    // SPACIO->SALTO
    //FLECHA DERECHA -> DERECHA
    //FLECHA IZQUIERDA -> IZQUIERDA
    //X -> DASH
    //C -> DOWN ATACK
    public void start() { loop.start(); }

    private void actualizar(double delta) {
        // input
        if (keys.contains(KeyCode.LEFT)) jugador.moverIzquierda();
        if (keys.contains(KeyCode.RIGHT)) jugador.moverDerecha();
        if(keys.contains(KeyCode.Z)) jugador.actualizarArma();



        boolean tocandoPared = false;

        // update entities
        for (Entidad en : entidades) en.update(delta);

        // gravedad & plataformas collision for player


        jugador.updateDash(delta);


        //colision con las plataformas
        boolean onPlatform = false;
        for (Plataforma p : plataformas) {

            if (p instanceof Suelo) {

                if (jugador.getBounds().intersects(p.getBounds())) {

                    boolean desdeArriba =
                            jugador.getY() + jugador.getHeight() <= p.getY()+20 ;

                    // Solo si viene cayendo Y viene por arriba
                    if (jugador.getVelY() > 0 && desdeArriba) {

                        // Acomoda exactamente sobre el suelo
                        jugador.setY(p.getY() - jugador.getHeight());
                        jugador.setVelY(0);

                        jugador.setEnSuelo(true);
                        jugador.aterrizo();
                        onPlatform = true;

                    } else {
                        jugador.detenerHorizontal();
                        tocandoPared = true;
                        jugador.darSaltos();
                    }
                }
            }

            if(p instanceof Techo){
                if(jugador.getBounds().intersects(p.getBounds())){
                    jugador.detenerVertical();

                }
            }
        }

        if (!onPlatform) jugador.setEnSuelo(false);
        jugador.aplicarGravedadSegunEstado(tocandoPared);

        // collisions with enemies
        for (Entidad en : entidades) {
            if (en instanceof Enemigo) {
                ((Enemigo) en).actualizarInvulnerabilidad(delta);
                if (jugador.getBounds().intersects(en.getBounds())) {
                    jugador.setVivo(false);
                }
                if(jugador.verificarColisionArma(en.getBounds())){
                    Enemigo enemigo = (Enemigo)en;
                    if(enemigo.puedeRecibirDaño()){
                        enemigo.setCorazones(enemigo.getCorazones()-1);
                        enemigo.activarInvulnerabilidad();
                        System.out.println("Corazones restantes: "+ enemigo.getCorazones());
                    }

                }
            }
        }
        //++++++++CALCULO DE LA POSICION DE LA CAMARA+++++
        cameraX = jugador.getX() - width /2;
        if (cameraX < 0) cameraX = 0;
        //colision con el arma


        // remove dead or collected items if any (not implemented but placeholder)
    }

    private void generarObjetos(){
        plataformas.add(new Suelo(0, 800, 800, 100));

        plataformas.add(new Suelo(900, 700, 200, 50));

        plataformas.add(new Suelo(1250, 800, 400, 500));

        plataformas.add(new Suelo(1850, 800, 400, 500));

        plataformas.add(new Suelo(2250, 600, 835, 500));

        plataformas.add(new Suelo(3250, 200, 800, 800));

        plataformas.add(new Suelo(3050, 0, 50, 400));

        plataformas.add(new Techo(3050, 390, 51, 30 ));

        plataformas.add(new Suelo(4050, 800, 800, 100));

        entidades.add(new EnemigoTerrestre(4250, 750, 50, 50, 1));



    }

    private void dibujar() {
        // clear
        gc.setFill(Color.web("#1e1e1e"));
        gc.fillRect(0, 0, width, height);
//++++++++CDIBUJA  LA CAMARA+++++
        gc.save();
        gc.translate(-cameraX, 0);
        // draw platforms

        gc.setFill(Color.SADDLEBROWN);
        for (Plataforma p : plataformas) {
            p.draw(gc);
        }

        // draw entities
        for (Entidad e : entidades){
            e.draw(gc);
            if(e instanceof Jugador){
                ((Jugador) e).dibujarArma(gc);
            }
        }
        gc.restore();

        // HUD
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(18));
        gc.fillText("Puntaje: " + jugador.getPuntaje(), 20, 30);
        gc.fillText("Presiona 'S' para guardar", 20, 55);

        if (!jugador.isVivo()) {
            gc.setFill(Color.color(0,0,0,0.6));
            gc.fillRect(0, 0, width, height);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(36));
            gc.fillText("¡Has perdido!", width/2 - 100, height/2);
        }
    }

    private void guardar() {
        try {
            archivoJuego.guardar(new ArchivoJuego.Progreso(jugador.getPuntaje(), "player"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
