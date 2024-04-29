package com.example.timp_labs.console;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyConsole {
    public MyConsole() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/timp_labs/Console.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        Stage stage = new Stage();
        stage.setTitle("Console");
        stage.setScene(scene);
        stage.show();
    }
}
