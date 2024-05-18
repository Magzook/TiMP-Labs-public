module client {
    requires javafx.controls;
    requires javafx.fxml;
    //requires jdk.jfr;


    //opens client to javafx.fxml;
    exports client.model;
    exports client.main;
    exports client.controllers;
    opens client.main to javafx.fxml;
    opens client.controllers to javafx.fxml;
}