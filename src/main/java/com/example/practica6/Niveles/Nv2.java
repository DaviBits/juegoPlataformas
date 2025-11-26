package com.example.practica6.Niveles;

import com.example.practica6.ArchivoJuego;
import com.example.practica6.Entorno.*;
import com.example.practica6.Personajes.*;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Nv2 extends Pane {
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

    private ArrayList <Corazon> corazones;

    //imagenes del fondo
    double scale = 2.5;

    private Image sun;
    private Image sky;
    private Image ruins;
    private Image mountains;

    private Font pressStart;


    public Nv2(int width, int height) {
        this.width = width;
        this.height = height;
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        corazones=new ArrayList<>();
        pressStart = Font.loadFont(
                getClass().getResourceAsStream("/PressStart2P-Regular.ttf"),
                16
        );
        this.getChildren().add(canvas);
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
                ((EnemigoTerrestre) en).update(4050, 4250);
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
                        System.out.println("ten un salto");
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
                en.actualizarInvulnerabilidad(delta);
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

        for(Corazon e :corazones){
            if(e.getBounds().intersects(jugador.getBounds())){
                double vidaNew= jugador.getVida()+10;
                jugador.setVida(vidaNew);
                corazones.remove(e);
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

        // ============================================
        //     PLATAFORMAS — NIVEL 2
        // ============================================

        plataformas.add(new Suelo(0, 800, 1000, 100));
        plataformas.add(new Suelo(1100, 700, 250, 50));
        plataformas.add(new Suelo(1450, 650, 200, 50));
        plataformas.add(new Suelo(1800, 800, 500, 100));
        plataformas.add(new Suelo(2500, 550, 220, 40));
        plataformas.add(new Suelo(2800, 350, 180, 40));
        plataformas.add(new Suelo(3100, 800, 200, 400));
        plataformas.add(new Suelo(3350, 600, 150, 120));
        plataformas.add(new Suelo(3600, 450, 160, 40));
        plataformas.add(new Suelo(4000, 800, 1400, 100));

        // ============================================
        //         DECORACIONES NIVEL 2
        // ============================================

        decoracions.add(new ObjetosDecoracion(100, 800, 40, 40, 0));
        decoracions.add(new ObjetosDecoracion(300, 800, 70, 90, 7));
        decoracions.add(new ObjetosDecoracion(450, 800, 55, 55, 4));

        decoracions.add(new ObjetosDecoracion(1120, 700 - 60, 60, 60, 1));
        decoracions.add(new ObjetosDecoracion(1300, 700 - 140, 110, 140, 6));

        decoracions.add(new ObjetosDecoracion(2600, 550 - 50, 50, 50, 5));
        decoracions.add(new ObjetosDecoracion(2680, 550 - 150, 120, 150, 9));

        decoracions.add(new ObjetosDecoracion(2820, 350 - 40, 40, 40, 3));

        decoracions.add(new ObjetosDecoracion(3150, 800 - 150, 130, 150, 9));
        decoracions.add(new ObjetosDecoracion(3450, 600 - 60, 60, 60, 2));
        decoracions.add(new ObjetosDecoracion(3620, 450 - 100, 100, 100, 8));

        decoracions.add(new ObjetosDecoracion(4200, 800 - 160, 120, 160, 7));
        decoracions.add(new ObjetosDecoracion(4450, 800 - 50, 50, 50, 0));
        decoracions.add(new ObjetosDecoracion(4600, 800 - 45, 45, 45, 3));

        // ============================================
        //                ENEMIGOS TERRESTRES
        // ============================================

        entidades.add(new EnemigoTerrestre(500, 720, 40, 80, 1.2, 5, 15));
        entidades.add(new EnemigoTerrestre(1500, 620, 40, 80, 1.3, 5, 15));
        entidades.add(new EnemigoTerrestre(2600, 500, 40, 80, 1.4, 5, 15));
        entidades.add(new EnemigoTerrestre(3500, 550, 40, 80, 1.1, 5, 20));
        entidades.add(new EnemigoTerrestre(4300, 720, 40, 80, 1.6, 5, 20));

        // ============================================
        //            ENEMIGOS VOLADORES
        // ============================================

        entidades.add(new EnemigoVolador(300, 600, 60, 40, 1.5, 4, 10));
        entidades.add(new EnemigoVolador(1900, 650, 60, 40, 2.0, 4, 10));
        entidades.add(new EnemigoVolador(2550, 450, 60, 40, 1.7, 4, 12));
        entidades.add(new EnemigoVolador(2800, 250, 60, 40, 1.4, 4, 12));
        entidades.add(new EnemigoVolador(3400, 350, 60, 40, 1.8, 5, 15));
        entidades.add(new EnemigoVolador(4200, 600, 60, 40, 2.2, 6, 15));

        // ============================================
        //                  CORAZONES
        // ============================================

        corazones.add(new Corazon(100, 750, 40, 40));     // suelo inicial
        corazones.add(new Corazon(1350, 600, 40, 40));    // subida escalonada
        corazones.add(new Corazon(2000, 760, 40, 40));    // salto ancho
        corazones.add(new Corazon(2550, 500, 40, 40));    // plataforma media
        corazones.add(new Corazon(2850, 300, 40, 40));    // plataforma alta (riesgo)
        corazones.add(new Corazon(3300, 750, 40, 40));    // torre baja
        corazones.add(new Corazon(3600, 400, 40, 40));    // torre alta
        corazones.add(new Corazon(4100, 760, 40, 40));    // inicio zona final
        corazones.add(new Corazon(4700, 780, 40, 40));    // final antes meta
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

        for(Corazon e : corazones){
            double alto = e.getHeight();
            double ySuelo = buscarSueloDebajo(e.getX());
            e.setY(ySuelo - alto);
            e.draw(gc);
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
        gc.setFont(pressStart);
        gc.setFill(Color.BLACK);
        gc.fillText("Vida: "+jugador.getVida(), 25, 39);

        gc.setFill(Color.WHITE);
        gc.fillText("Vida: "+jugador.getVida(), 23, 37);

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
        gc.setFill(Color.color(0, 0, 0, 0.2));
        gc.fillRect(0, 0, 50000, 50000);
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
