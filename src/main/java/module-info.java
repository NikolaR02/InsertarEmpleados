module com.example.insertarempleados {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires java.desktop;


    opens com.example.insertarempleados to javafx.fxml;
    exports com.example.insertarempleados;
}