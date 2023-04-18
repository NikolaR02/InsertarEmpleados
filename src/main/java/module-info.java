module com.example.insertarempleados {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.insertarempleados to javafx.fxml;
    exports com.example.insertarempleados;
}