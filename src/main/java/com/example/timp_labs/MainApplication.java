package com.example.timp_labs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    Habitat hab;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Main.fxml"));
        Parent root = fxmlLoader.load();
        hab = new Habitat(fxmlLoader.getController());
        Habitat.setInstance(hab);
        Scene scene = new Scene(root, Habitat.getWidth(), Habitat.getHeight());
        scene.getRoot().requestFocus();
        stage.setMaximized(true);
        hab.setParamCapital(0.66F, 2);
        hab.setParamWooden(0.35F, 3);
        stage.setTitle("Лабораторная №1");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}