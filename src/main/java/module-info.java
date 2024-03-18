module com.example.timp_labs {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jfr;


    opens com.example.timp_labs to javafx.fxml;
    exports com.example.timp_labs;
    exports com.example.timp_labs.model;
    opens com.example.timp_labs.model to javafx.fxml;
}