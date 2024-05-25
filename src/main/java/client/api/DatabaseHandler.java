package client.api;

import client.controllers.Habitat;
import client.controllers.Statistics;
import client.model.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import java.sql.*;
import java.util.HashMap;
import java.util.Optional;
import java.util.Vector;

public class DatabaseHandler {
    private static final String url = "jdbc:postgresql://localhost:5432/PersonsDB";
    private static final String user = "postgres";
    private static final String password = "password";

    public static void saveToDB(String mode) {
        Connection connection = null;
        PreparedStatement statement = null, statement2 = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

            String[] sqls = new String[] {
                    "DELETE FROM \"personsTable\"",
                    "DELETE FROM \"personsAdditionalInfo\"",
                    "ALTER SEQUENCE \"personsTable_id_seq\" RESTART WITH 1"
            };
            for (var sql : sqls) {
                statement = connection.prepareStatement(sql);
                statement.executeUpdate();
            }

            String sql = "INSERT INTO \"personsTable\" (personType, bornTime, innerID, positionX, positionY, destinationX, destinationY, hasToTravel) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            String sql2 = "INSERT INTO \"personsAdditionalInfo\" (spawnedPhy, spawnedJur, currTime) VALUES (?, ?, ?)";
            statement2 = connection.prepareStatement(sql2);

            Vector<Person> objCollection = Habitat.getInstance().getObjCollection();
            HashMap<Integer, Integer> bornCollection = Habitat.getInstance().getBornCollection();
            synchronized (objCollection) {
                int personType = -1;
                for (var obj : objCollection) {
                    if (obj instanceof PhysicalPerson) personType = 0;
                    else if (obj instanceof JuridicalPerson) personType = 1;
                    if (mode.equals("all") || (personType == 0 && mode.equals("phy")) || (personType == 1 && mode.equals("jur"))) {
                        statement.setInt(1, personType);
                        statement.setInt(2, bornCollection.get(obj.getId()));
                        statement.setInt(3, obj.getId());
                        statement.setDouble(4, obj.getX());
                        statement.setDouble(5, obj.getY());
                        statement.setDouble(6, obj.getDestinationX());
                        statement.setDouble(7, obj.getDestinationY());
                        statement.setInt(8, obj.getHasToTravel());
                        statement.executeUpdate();
                    }
                }
                statement2.setInt(1, PhysicalPerson.spawnedCount);
                statement2.setInt(2, JuridicalPerson.spawnedCount);
                statement2.setInt(3, Statistics.getInstance().getTime());
                statement2.executeUpdate();
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (statement2 != null) statement2.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void loadFromDB(String mode) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

            Habitat hab = Habitat.getInstance();
            Statistics st = Statistics.getInstance();
            // Остановить симуляцию с возможностью продолжения, очистить старые объекты
            st.pauseAndClear();
            // Прочитать новые объекты
            String sql = "SELECT * FROM \"personsTable\"";
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            while (rs.next()) {
                int personType = rs.getInt("personType");
                if (!(mode.equals("all") || (personType == 0 && mode.equals("phy")) || (personType == 1 && mode.equals("jur"))))
                    continue;
                int bornTime = rs.getInt("bornTime");
                int id = rs.getInt("innerID");
                double posX = rs.getDouble("positionX");
                double posY = rs.getDouble("positionY");
                double destX = rs.getDouble("destinationX");
                double destY = rs.getDouble("destinationY");
                int hasToTravel = rs.getInt("hasToTravel");
                Person obj = null;
                if (personType == 0) obj = new PhysicalPerson(id, posX, posY, destX, destY, hasToTravel);
                else if (personType == 1) obj = new JuridicalPerson(id, posX, posY, destX, destY, hasToTravel);
                assert obj != null;
                if (obj.getHasToTravel() == 1) BaseAI.calculateShifting(obj);

                hab.getObjCollection().add(obj);
                hab.getBornCollection().put(obj.getId(), bornTime);
                hab.getIdCollection().add(obj.getId());
            }
            // Прочитать дополнительную информацию
            sql = "SELECT * FROM \"personsAdditionalInfo\"";
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            rs.next();
            PhysicalPerson.spawnedCount = rs.getInt("spawnedPhy");
            JuridicalPerson.spawnedCount = rs.getInt("spawnedJur");
            st.prepareTimer(rs.getInt("currTime"));

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String askOption(String saveOrLoad) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        if (saveOrLoad.equals("save")) {
            alert.setTitle("Сохранение в базу данных");
            alert.setHeaderText("Что вы хотите сохранить в базу данных?");
        }
        else if (saveOrLoad.equals("load")) {
            alert.setTitle("Загрузка из базы данных");
            alert.setHeaderText("Что вы хотите загрузить из базы данных?");
        }
        ButtonType buttonPhy = new ButtonType("Физические лица");
        ButtonType buttonJur = new ButtonType("Юридические лица");
        ButtonType buttonAll = new ButtonType("Всё");
        ButtonType buttonCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonPhy, buttonJur, buttonAll, buttonCancel);
        Optional<ButtonType> result = alert.showAndWait();
        String mode;
        if (result.get() == buttonPhy) mode = "phy";
        else if (result.get() == buttonJur) mode = "jur";
        else if (result.get() == buttonAll) mode = "all";
        else mode = "none";
        return mode;
    }
}
