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
        hab = new Habitat();
        Habitat.setInstance(hab);
        hab.setParamPhysicalPerson(1, 1);
        hab.setParamJuridicalPerson(1, 2);
        Statistics stats = new Statistics(fxmlLoader.getController());
        Statistics.setInstance(stats);

        Scene scene = new Scene(root, Habitat.getInstance().getWidth(), Habitat.getInstance().getHeight());
        scene.getRoot().requestFocus();
        stage.setMaximized(false); // Запуск на весь экран или нет?
        stage.setTitle("Картотека налоговой инспекции (a.k.a laba 1)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}