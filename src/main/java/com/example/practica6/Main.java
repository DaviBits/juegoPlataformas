package com.example.practica6;
import java.awt.Toolkit;
import java.awt.Dimension;

import com.example.practica6.Niveles.Nv1;
import com.example.practica6.Niveles.Nv2;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        Nv2 game = new Nv2(screenWidth, screenHeight);
        StackPane root = new StackPane(game.getCanvas());
        Scene scene = new Scene(root);
        game.setupInput(scene);
        primaryStage.setTitle("Juego Plataforma 2D - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.show();
        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}