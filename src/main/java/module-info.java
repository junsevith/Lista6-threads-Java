module com.example.lista6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lista6 to javafx.fxml;
    exports com.example.lista6;
}