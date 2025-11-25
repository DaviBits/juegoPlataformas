package com.example.practica6.Niveles;

import com.example.practica6.ArchivoJuego;
import com.example.practica6.Entorno.ObjetosDecoracion;
import com.example.practica6.Entorno.Plataforma;
import com.example.practica6.Entorno.Suelo;
import com.example.practica6.Entorno.Techo;
import com.example.practica6.Personajes.*;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
    private List<ObjetosDecoracion> decoracions;
    private Set<KeyCode> keys = new HashSet<>();
    private ArchivoJuego archivoJuego;

    private boolean saltoProcesado = false;
    private boolean ataqueProcesado = false;
    private boolean dashProcesado = false;

    private AnimationTimer loop;
    private double cameraX;
    private double cameraY;

    //imagenes del fondo
    double scale = 2.5;

    private Image sun;
    private Image sky;
    private Image ruins;
    private Image mountains;

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
        decoracions=new ArrayList<>();


        jugador = new Jugador(50, 500, 40, 60, 100);
        entidades.add(jugador);

        cameraX = jugador.getX() - width / 2.0;
        cameraY = jugador.getY() - height / 2.0;

        // Plataformas (suelo + dos plataformas elevadas)
        cargarFondos();
        generarObjetos();


        // Enemigos




        // Setup loop
        loop = new AnimationTimer() {
            private long last = 0;
            @Override
            public void handle(long now) {
                if (last == 0) last = now;
                double delta = (now - last) / 1e9;
                dibujar();
                actualizar(delta);
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


    public void cargarFondos(){
        sky = new Image(getClass().getResourceAsStream("/Fondo/BG-sky.png"));
        sun = new Image(getClass().getResourceAsStream("/Fondo/BG-sun.png"));
        mountains = new Image(getClass().getResourceAsStream("/Fondo/BG-mountains.png"));
        ruins = new Image(getClass().getResourceAsStream("/Fondo/BG-ruins.png"));
    }

    public void setupInput(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            keys.add(e.getCode());
            if (e.getCode() == KeyCode.S) {
                guardar();
            }
            if (e.getCode() == KeyCode.SPACE) {
                if (!saltoProcesado) {        // <--- SOLO UNA VEZ
                    jugador.saltar();
                    saltoProcesado = true;
                }
            }

            if (e.getCode() == KeyCode.X) {
                if (!dashProcesado) {
                    jugador.dash();
                    dashProcesado = true;
                    saltoProcesado=true;
                }
            }

            if(e.getCode() == KeyCode.C){
                jugador.downAtack();
            }



            if (e.getCode() == KeyCode.RIGHT) jugador.setDireccion(1);
            if (e.getCode() == KeyCode.LEFT) jugador.setDireccion(-1);


        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            keys.remove(e.getCode());

            if (e.getCode() == KeyCode.SPACE) saltoProcesado = false;
            if (e.getCode() == KeyCode.X) dashProcesado = false;
            if (e.getCode() == KeyCode.C) ataqueProcesado = false;
        });
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
        for (Entidad en : entidades){
            if(! (en instanceof EnemigoTerrestre)){
                en.update(delta);
            }else{
                ((EnemigoTerrestre) en).update(4050, 4250, delta);
            }
        }

        // gravedad & plataformas collision for player


        jugador.updateDash(delta, plataformas);


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
        for (Entidad en : entidades) en.actualizarInvulnerabilidad(delta);


        // collisions with enemies
        for (Entidad en : entidades) {
            if (en instanceof Enemigo) {
                ((Enemigo) en).actualizarInvulnerabilidad(delta);
                if (jugador.getBounds().intersects(en.getBounds()) && jugador.puedeRecibirDaño()) {
                    jugador.setVida((Double) (jugador.getVida() - ((Enemigo) en).getDaño()));
                    jugador.activarInvulnerabilidad();
                    System.out.println(jugador.getVida());
                    if (jugador.getVida() <= 0) {
                        jugador.setVivo(false);
                    }
                }
                if(jugador.verificarColisionArma(en.getBounds())){
                    Enemigo enemigo = (Enemigo)en;
                    if(enemigo.puedeRecibirDaño()){
                        enemigo.setVida((double) (enemigo.getVida()-1));
                        enemigo.activarInvulnerabilidad();
                        if(enemigo.getVida() <= 0) {
                            jugador.setPuntaje(enemigo.getPuntaje());
                            entidades.remove(enemigo);
                        }
                        System.out.println("Corazones restantes: "+ enemigo.getVida());
                    }

                }
            }
        }
        //++++++++CALCULO DE LA POSICION DE LA CAMARA+++++
        cameraX = jugador.getX() - width / 2.0;
        cameraY = jugador.getY() - height / 2.0;

        // Limitar cámara a los bordes del mundo
        if (cameraX < 0 ) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        //colision con el arma
        //colision con el arma


        // remove dead or collected items if any (not implemented but placeholder)
    }

    private void generarObjetos() {
        // ============================
        //  PLATAFORMAS
        // ============================

        plataformas.add(new Suelo(0, 800, 800, 100));
        plataformas.add(new Suelo(900, 700, 200, 50));
        plataformas.add(new Suelo(1250, 800, 400, 500));
        plataformas.add(new Suelo(1850, 800, 400, 500));
        plataformas.add(new Suelo(2250, 600, 835, 500));
        plataformas.add(new Suelo(3250, 200, 800, 800));
        plataformas.add(new Suelo(3050, 0, 50, 400));
        plataformas.add(new Techo(3050, 390, 51, 30));
        plataformas.add(new Suelo(4050, 800, 800, 100));

        entidades.add(new EnemigoTerrestre(4150, 720, 40, 80, 1.5, 5, 10));

        // ============================
        //  DECORACIONES (Y ALINEADAS)
        // ============================

        // Suelo: y = 800
        decoracions.add(new ObjetosDecoracion(100, 800, 40, 40, 0));  // ROCKS1
        decoracions.add(new ObjetosDecoracion(240, 800, 50, 50, 2));  // ROCKS3
        decoracions.add(new ObjetosDecoracion(320, 800, 65, 65, 4));  // ROCKS5
        decoracions.add(new ObjetosDecoracion(500, 800, 90, 120, 6)); // TREE1
        decoracions.add(new ObjetosDecoracion(650, 800, 110, 150, 7)); // TREE2

        // Plataforma: y = 700
        decoracions.add(new ObjetosDecoracion(910, 700 - 60, 60, 60, 1)); // ROCKS2
        decoracions.add(new ObjetosDecoracion(980, 700 - 130, 100, 130, 8)); // TREE3

        // Suelo: y = 800 (zona media)
        decoracions.add(new ObjetosDecoracion(1350, 800 - 55, 55, 55, 5));  // ROCKS6
        decoracions.add(new ObjetosDecoracion(1500, 800 - 150, 110, 150, 6)); // TREE1
        decoracions.add(new ObjetosDecoracion(1650, 800 - 45, 45, 45, 3)); // ROCKS4

        // Suelo: y = 800 (zona media 2)
        decoracions.add(new ObjetosDecoracion(1880, 800 - 60, 60, 60, 0)); // ROCKS1
        decoracions.add(new ObjetosDecoracion(2000, 800 - 160, 120, 160, 9)); // TREE4

        // Plataforma: y = 600
        decoracions.add(new ObjetosDecoracion(2300, 600 - 50, 50, 50, 4)); // ROCKS5
        decoracions.add(new ObjetosDecoracion(2450, 600 - 140, 100, 140, 6)); // TREE1
        decoracions.add(new ObjetosDecoracion(2600, 600 - 40, 40, 40, 1)); // ROCKS2

        // Plataforma alta: y = 200
        decoracions.add(new ObjetosDecoracion(3300, 200 - 55, 55, 55, 5)); // ROCKS6
        decoracions.add(new ObjetosDecoracion(3450, 200 - 140, 100, 140, 8)); // TREE3

        // Suelo final: y = 800
        decoracions.add(new ObjetosDecoracion(4100, 800 - 180, 130, 180, 9)); // TREE4
        decoracions.add(new ObjetosDecoracion(4300, 800 - 50, 50, 50, 0));   // ROCKS1
        decoracions.add(new ObjetosDecoracion(4450, 800 - 45, 45, 45, 3));   // ROCKS4
    }

    private void dibujarFondos(GraphicsContext gc) {

        double camX = cameraX;
        double camY = cameraY;

        // Escala del fondo (hazlo más grande si quieres)
        double scale = 2.5;

        Image[] capas = { sky, mountains, ruins };
        double[] vel =  { 0.15,  0.35,     0.6  };

        for (int i = 0; i < capas.length; i++) {


            Image img = capas[i];

            double parallax = vel[i];

            double x = -camX * parallax;
            double y = 0;


             double w = img.getWidth() * scale;
            double h = img.getHeight() * scale;

            // Repetir horizontalmente
            for (double dx = x - w; dx < x + width + w; dx += w) {
                gc.drawImage(img, dx, y, w, h);
            }
        }

        // ☀ Sol (también escalado)
        double sunScale = 3.0;
        gc.drawImage(
                sun,
                -camX * 0.1 + 300,
                50,
                sun.getWidth() * sunScale,
                sun.getHeight() * sunScale
        );
    }




    private void dibujar() {
        // clear
        gc.setFill(Color.web("#19121E"));
        gc.fillRect(0, canvas.getHeight() - 350, canvas.getWidth(), 350);




//++++++++DIBUJA  LA CAMARA+++++
        dibujarFondos(gc);
        gc.save();
        gc.translate(-cameraX, -cameraY);



        // draw platforms

        gc.setFill(Color.SADDLEBROWN);
        for (Plataforma p : plataformas) {
            p.draw(gc);
        }

        for (ObjetosDecoracion e : decoracions) {
            double alto = e.getHeight();
            double ySuelo = buscarSueloDebajo(e.getX());
            e.setY(ySuelo - alto);
            e.draw(gc);
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
    private double buscarSueloDebajo(double x) {
        for (Plataforma p : plataformas) {
            if (p instanceof Suelo) {
                if (x >= p.getX() && x <= p.getX() + p.getWidth()) {
                    return p.getY();
                }
            }
        }
        return 1000; // fallback
    }


    private void guardar() {
        try {
            archivoJuego.guardar(new ArchivoJuego.Progreso(jugador.getPuntaje(), "player"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
