package client.api;

import client.controllers.Controller;
import client.controllers.Statistics;
import dto.SettingsDTO;
import dto.UsernameDTO;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
public class Client {
    private static String userName;
    private static String serverIP;
    private static int serverPort;
    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static int labelConcurrentThreads = 0;

    public static void askUserName(Label mode) {
        Label labelName = new Label("Введите ваше имя");
        labelName.setFont(new Font("Arial Bold", 30));
        TextField fieldName = new TextField();
        fieldName.setFont(new Font("System", 30));

        Label labelIP = new Label("Введите IP-адрес сервера");
        labelIP.setFont(new Font("Arial Bold", 30));
        TextField fieldIP = new TextField();
        fieldIP.setFont(new Font("System", 30));

        Label labelPort = new Label("Введите порт");
        labelPort.setFont(new Font("Arial Bold", 30));
        TextField fieldPort = new TextField();
        fieldPort.setFont(new Font("System", 30));

        Button button = new Button("Войти");
        button.setFont(new Font("Consolas", 30));
        VBox root = new VBox(labelName, fieldName, labelIP, fieldIP, labelPort, fieldPort, button);

        Stage stage = new Stage();
        button.setOnAction(event -> {
            if (!fieldName.getText().isEmpty() && !fieldIP.getText().isEmpty() && !fieldPort.getText().isEmpty()) {
                userName = fieldName.getText();
                serverIP = fieldIP.getText();
                serverPort = Integer.parseInt(fieldPort.getText());
                stage.close();
                mode.setText("launch");
            }
        });
        stage.setOnCloseRequest(event -> mode.setText("exit"));
        stage.setScene(new Scene(root, 400, 400));
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
            socket = new Socket(serverIP, serverPort);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            ServerListener listener = new ServerListener(ois);
            listener.start();
            // Отправить серверу имя клиента
            UsernameDTO output = new UsernameDTO(userName);
            oos.writeObject(output);
            Statistics.getInstance().mainController.labelConnectionInfo.setText("IP: " + serverIP + " Порт: " + serverPort);

        } catch (ConnectException e) {
            Statistics.getInstance().mainController.labelConnectionInfo.setText("[Нет соединения]");
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
