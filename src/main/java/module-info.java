module com.example.timp_labs {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.timp_labs to javafx.fxml;
    exports com.example.timp_labs;
}