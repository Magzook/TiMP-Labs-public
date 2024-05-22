package client.api;

import client.controllers.Controller;
import client.controllers.Statistics;
import dto.SettingsDTO;
import dto.UsernameDTO;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static String userName;
    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static int labelConcurrentThreads = 0;

    public static void askUserName(Label mode) {
        Label label = new Label("Введите ваше имя");
        label.setFont(new Font("Arial Bold", 30));
        TextField textField = new TextField();
        textField.setFont(new Font("System", 30));
        Button button = new Button("Войти");
        button.setFont(new Font("Consolas", 30));
        BorderPane root = new BorderPane();
        root.setTop(label);
        root.setCenter(textField);
        root.setBottom(button);
        BorderPane.setAlignment(label, Pos.CENTER);
        BorderPane.setAlignment(textField, Pos.CENTER);
        BorderPane.setAlignment(button, Pos.CENTER);
        Stage stage = new Stage();
        button.setOnAction(event -> {
            if (!textField.getText().isEmpty()) {
                userName = textField.getText();
                stage.close();
                mode.setText("launch");
            }
        });
        stage.setOnCloseRequest(event -> mode.setText("exit"));
        stage.setScene(new Scene(root, 300, 200));
        stage.setTitle("Картотека");
        stage.showAndWait();
    }

    public static void createUserList(String[] info) {
        StringBuilder sb = new StringBuilder();
        sb.append(userName).append(" (Вы)").append("\n");
        Controller ct = Statistics.getInstance().mainController;

        Platform.runLater(() -> {
            ct.syncSettingsWithBox.getItems().add("Никто");
            for (var userName : info) {
                if (!userName.equals(Client.userName)) {
                    sb.append(userName).append("\n");
                    ct.syncSettingsWithBox.getItems().add(userName);
                }
            }
            ct.userListBox.setText(String.valueOf(sb));
            ct.syncSettingsWithBox.setValue("Никто");
        });
    }

    public static void addUserToList(String userName) {
        Controller ct = Statistics.getInstance().mainController;
        Platform.runLater(() -> {
            ct.userListBox.appendText(userName + "\n");
            ct.syncSettingsWithBox.getItems().add(userName);
        });
    }

    public static void removeUserFromList(String userName) {
        Controller ct = Statistics.getInstance().mainController;
        Platform.runLater(() -> {
            ct.userListBox.setText(ct.userListBox.getText().replace(userName+ "\n", ""));
            if (ct.syncSettingsWithBox.getValue().equals(userName)) {
                ct.syncSettingsWithBox.setValue("Никто");
            }
            ct.syncSettingsWithBox.getItems().remove(userName);
        });
    }

    public static void connectToServer() {
        try {
            socket = new Socket(InetAddress.getByName("localhost"), 8030);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            ServerListener listener = new ServerListener(ois);
            listener.start();
            // Отправить серверу имя клиента
            UsernameDTO output = new UsernameDTO(userName);
            oos.writeObject(output);

        } catch (ConnectException e) {
            Statistics.getInstance().mainController.labelOfflineMode.setVisible(true);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void sendSettingsToUser(String target, String p1, String p2) throws IOException {
        if (target.equals("Никто")) return;
        SettingsDTO output = new SettingsDTO(target, p1, p2);
        oos.writeObject(output);
    }

    public static void disconnectFromServer() throws IOException {
        ois.close();
        oos.close();
        socket.close();
    }
    public static void setSettings(String source, String p1, String p2) {
        Controller ct = Statistics.getInstance().mainController;
        // Установка вероятностей
        Platform.runLater(() -> {
            ct.boxP1.setValue(p1);
            ct.boxP2.setValue(p2);
        });
        // Включение уведомления на 3 секунды
        new Thread(() -> {
            labelConcurrentThreads++;
            Platform.runLater(() -> {
                ct.labelSettingsSource.setText("Вероятности от: " + source);
                ct.labelSettingsSource.setVisible(true);
            });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (labelConcurrentThreads == 1) Platform.runLater(() -> ct.labelSettingsSource.setVisible(false));
            labelConcurrentThreads--;
        }).start();
    }
}
