module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    exports client.model;
    exports client.main;
    exports client.controllers;
    opens client.main to javafx.fxml;
    opens client.controllers to javafx.fxml;
}