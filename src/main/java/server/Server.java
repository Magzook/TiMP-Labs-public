package server;

import dto.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Server {
    private static ServerSocket serverSocket;
    private static ArrayList<String> userList;
    private static HashMap<String, ObjectOutputStream> oosOfEveryUser;
    public Server() throws IOException {
        serverSocket = new ServerSocket(8030);
        userList = new ArrayList<>();
        oosOfEveryUser = new HashMap<>();
        new Thread(() -> {
            // Остановить сервер при вводе команды "stop" в консоль
            Scanner scanner = new Scanner(System.in);
            while (!scanner.nextLine().equals("stop")) {}
            try {
                serverSocket.close();
                System.out.println("Сервер остановлен");
                System.exit(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    public void launch() {
        try {
            System.out.println("Сервер запущен");
            while (true) {
                // Сервер ожидает подключения клиента
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                // Сервер создаёт поток для подключенного клиента
                new ClientHandler(socket, ois, oos).start();
            }
        } catch (SocketException e) {
            System.out.println("Сервер остановлен");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void userlistUnicast(ObjectOutputStream oos) throws IOException {
        // Отправить новому клиенту список клиентов
        UserlistDTO output = new UserlistDTO(userList.toArray(new String[0]));
        oos.writeObject(output);
    }
    public static void userJoinedOrLeftBroadcast(String userName, boolean status) throws IOException {
        // Оповестить всех клиентов о подключении или отключении клиента
        // status == true - клиент подключился
        // status == false - клиент отключился
        UsernameDTO output = new UsernameDTO(userName, status);
        for (var user : userList) {
            if (!user.equals(userName)) {
                oosOfEveryUser.get(user).writeObject(output);
            }
        }
    }
    public static void settingsUnicast(SettingsDTO dto, String source) throws IOException {
        SettingsDTO output = new SettingsDTO(source, dto.getP1(), dto.getP2());
        oosOfEveryUser.get(dto.getUserName()).writeObject(output);
    }

    public static void addUserToList(String userName, ObjectOutputStream userOos) {
        userList.add(userName);
        oosOfEveryUser.put(userName, userOos);
    }
    public static void removeUserFromList(String userName, ObjectOutputStream userOos) {
        userList.remove(userName);
        oosOfEveryUser.remove(userName, userOos);
    }
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.launch();
    }
}
