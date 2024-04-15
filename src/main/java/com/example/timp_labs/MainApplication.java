package com.example.timp_labs;

import com.example.timp_labs.model.AIJuridical;
import com.example.timp_labs.model.AIPhysical;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApplication extends Application {
    Habitat hab;
    Statistics stats;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Main.fxml"));
        Parent root = fxmlLoader.load();

        Statistics.getInstance().setMainController(fxmlLoader.getController());

        Scene scene = new Scene(root, Habitat.getInstance().getWidth(), Habitat.getInstance().getHeight());
        scene.getRoot().requestFocus();
        stage.setMaximized(false); // Запуск на весь экран или нет?
        stage.setTitle("Картотека налоговой инспекции");
        stage.setScene(scene);
        stage.setOnCloseRequest(t -> System.exit(0)); // Остановка приложения по нажатию крестика

        AIPhysical.getInstance().start();
        AIJuridical.getInstance().start();

        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}