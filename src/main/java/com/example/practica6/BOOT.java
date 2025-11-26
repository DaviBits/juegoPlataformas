package com.example.practica6;

import com.example.practica6.GUI.MenuInicio;
import com.example.practica6.Niveles.Nv2; // <-- CAMBIA ESTE IMPORT SI TU NIVEL TIENE OTRO NOMBRE
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class BOOT extends Application {

    private Stage ventanaPrincipal;

    @Override
    public void start(Stage stage) {
        this.ventanaPrincipal = stage;


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        MenuInicio menu = new MenuInicio();
        Scene menuScene = new Scene(menu, 1500, 800);


        menu.getIniciarJuego().setOnAction(e -> iniciarNivel2(screenWidth, screenHeight));


        
        ventanaPrincipal.setScene(menuScene);
        ventanaPrincipal.setTitle("Samurai Adventure");
        ventanaPrincipal.show();
    }


    private void iniciarNivel2(int x, int y) {

        Nv2 nivel2 = new Nv2(x, y);
        Scene nivel2Scene = new Scene(nivel2, x, y);

        ventanaPrincipal.setScene(nivel2Scene);

        nivel2.setupInput(nivel2Scene);
        nivel2.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
