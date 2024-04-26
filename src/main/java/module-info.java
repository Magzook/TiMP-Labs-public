module com.example.timp_labs {
    requires javafx.controls;
    requires javafx.fxml;
    //requires jdk.jfr;


    opens com.example.timp_labs to javafx.fxml;
    exports com.example.timp_labs.model;
    exports com.example.timp_labs.main;
    exports com.example.timp_labs.controllers;
    opens com.example.timp_labs.main to javafx.fxml;
    opens com.example.timp_labs.controllers to javafx.fxml;
}