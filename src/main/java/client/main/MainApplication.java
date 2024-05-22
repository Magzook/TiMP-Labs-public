package client.main;

import client.api.Client;
import client.controllers.Habitat;
import client.controllers.Statistics;
import client.model.AIJuridical;
import client.model.AIPhysical;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        Label mode = new Label();
        Client.askUserName(mode);
        if (mode.getText().equals("exit")) return;

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/Main.fxml"));
        Parent root = fxmlLoader.load();
        Statistics st = Statistics.getInstance();
        st.setMainController(fxmlLoader.getController());
        st.mainController.setParameters();
        Scene scene = new Scene(root, Habitat.getInstance().getWidth(), Habitat.getInstance().getHeight());
        scene.getRoot().requestFocus();
        stage.setMaximized(false); // Запуск на весь экран или нет?
        stage.setTitle("Картотека налоговой инспекции");
        stage.setScene(scene);
        stage.setOnCloseRequest(t -> { // Остановка приложения по нажатию крестика
            try {
                st.mainController.exitApp();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        AIPhysical.getInstance().start();
        AIJuridical.getInstance().start();
        stage.show();

        Client.connectToServer();
    }
    public static void main(String[] args) {
        launch();
    }
}