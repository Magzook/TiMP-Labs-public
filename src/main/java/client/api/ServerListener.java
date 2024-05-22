package client.api;

import client.controllers.Statistics;
import dto.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class ServerListener extends Thread {
    private ObjectInputStream ois;
    public ServerListener(ObjectInputStream ois) {
        this.ois = ois;
    }
    public void run() {
        try {
            while (true) {
                // Слушаем сообщения от сервера
                DTO input = (DTO) ois.readObject();
                if (input instanceof UserlistDTO dto) {
                    // Сервер прислал список клиентов
                    Client.createUserList(dto.getUserList());
                }
                else if (input instanceof UsernameDTO dto) {
                    // Сервер прислал уведомление о подключении или отключении другого клиента
                    if (dto.getStatus()) {
                        Client.addUserToList(dto.getUserName());
                    }
                    else {
                        Client.removeUserFromList(dto.getUserName());
                    }
                }
                else if (input instanceof SettingsDTO dto) {
                    // Сервер переслал настройки вероятностей от другого клиента
                    Client.setSettings(dto.getUserName(), dto.getP1(), dto.getP2());
                }
            }
        } catch (SocketException e) {
            System.out.println("Сервер закрыл соединение");
            Statistics.getInstance().mainController.labelOfflineMode.setVisible(true);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
