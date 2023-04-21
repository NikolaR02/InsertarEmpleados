module com.example.insertarempleados {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.insertarempleados to javafx.fxml;
    exports com.example.insertarempleados;
}