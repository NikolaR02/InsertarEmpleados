package com.example.insertarempleados;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Inicio extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Inicio.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 755, 600);
        stage.setTitle("Empleados");
        stage.setMinWidth(650.0);
        stage.setMinHeight(400.0);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}