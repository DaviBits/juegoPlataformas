module com.example.practica6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.practica6 to javafx.fxml;
    exports com.example.practica6;
    exports com.example.practica6.Personajes;
    opens com.example.practica6.Personajes to javafx.fxml;
    exports com.example.practica6.Entorno;
    opens com.example.practica6.Entorno to javafx.fxml;
    exports com.example.practica6.Animacion;
    opens com.example.practica6.Animacion to javafx.fxml;
    exports com.example.practica6.Otros;
    opens com.example.practica6.Otros to javafx.fxml;
}