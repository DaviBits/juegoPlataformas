package com.example.practica6;

import com.example.practica6.Niveles.Nv1;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Nv1 game = new Nv1(1600, 900);
        StackPane root = new StackPane(game.getCanvas());
        Scene scene = new Scene(root);
        game.setupInput(scene);
        primaryStage.setTitle("Juego Plataforma 2D - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1600);
        primaryStage.setHeight(900);
        primaryStage.show();
        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
