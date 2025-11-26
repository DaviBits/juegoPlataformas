package com.example.practica6.GUI;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class MenuInicio extends StackPane {
    private Label nombreJuego;
    private Button iniciarJuego;
    private ImageView fondo;
    private Label miNombre;

    public MenuInicio() {
        // Fuente retro estilo pixel
        Font pressStart = Font.loadFont(
                getClass().getResourceAsStream("/PressStart2P-Regular.ttf"),
                28
        );
        Font pressStartGrande = Font.loadFont(
                getClass().getResourceAsStream("/PressStart2P-Regular.ttf"),
                60
        );

        // --- Imagen de fondo ---
        fondo = new ImageView(new Image(getClass().getResourceAsStream("/fondoSamurai.png")));
        fondo.setFitWidth(1500);
        fondo.setFitHeight(800);
        fondo.setPreserveRatio(false);

        miNombre=new Label("By DaviBits & ArtemioLabs");
        miNombre.setFont(pressStart);
        miNombre.setStyle(
                "-fx-text-fill: #ffffff;" +
                        "-fx-effect: dropshadow(gaussian, black, 8, 0.5, 0, 0);"
        );

        // --- Título del juego ---
        nombreJuego = new Label("JUEGO POO");
        nombreJuego.setFont(pressStartGrande);
        nombreJuego.setStyle(
                "-fx-text-fill: #ffffff;" +
                        "-fx-effect: dropshadow(gaussian, black, 8, 0.5, 0, 0);"
        );


        //animacion de pulso
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1), nombreJuego);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        //boton estado inicial
        iniciarJuego = new Button("JUGAR!");
        iniciarJuego.setFont(pressStart);
        iniciarJuego.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #142259, #0b1440);" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 14 28 14 28;" +
                        "-fx-border-color: #ffffff;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 12;" +
                        "-fx-cursor: hand;"
        );

        //Hover del mouse-cuando el mouse esta sobre el boton
        iniciarJuego.setOnMouseEntered(e -> {
            iniciarJuego.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #142259, #000000);" +
                            "-fx-text-fill: #351a75;" +
                            "-fx-background-radius: 12;" +
                            "-fx-padding: 14 28 14 28;" +
                            "-fx-border-color: #351a75;" +
                            "-fx-border-width: 3;" +
                            "-fx-border-radius: 12;" +
                            "-fx-cursor: hand;"
            );
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), iniciarJuego);
            scaleUp.setToX(1.1);
            scaleUp.setToY(1.1);
            scaleUp.play();
        });

        //regresar al boton normal
        iniciarJuego.setOnMouseExited(e -> {
            iniciarJuego.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #142259, #0b1440);" +
                            "-fx-text-fill: #ffffff;" +
                            "-fx-background-radius: 12;" +
                            "-fx-padding: 14 28 14 28;" +
                            "-fx-border-color: #ffffff;" +
                            "-fx-border-width: 3;" +
                            "-fx-border-radius: 12;" +
                            "-fx-cursor: hand;"
            );
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), iniciarJuego);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
        });

        VBox contenido = new VBox( nombreJuego, iniciarJuego, miNombre);
        contenido.setSpacing(120);
        contenido.setAlignment(Pos.CENTER);

        this.getChildren().addAll(fondo, contenido);
    }

    public Button getIniciarJuego(){return iniciarJuego;}
}
