package com.example.insertarempleados;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Incertar {

    private final String servidor = "jdbc:mariadb://localhost:5555/noinch?useSSL=false";
    private final String usuario = "root";
    private final String passwd = "adminer";
    private Connection conexionBBDD;
    @FXML
    private TextField tfEmployeeNumber;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfExtension;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfOfficeCode;
    @FXML
    private TextField tfReportsTo;
    @FXML
    private TextField tfJobTitle;

    @FXML
    public void altaEmployee() {
        try {
            conexionBBDD = DriverManager.getConnection(servidor, usuario, passwd);
            String SQL = "INSERT INTO employees ("
                    + " employeeNumber ,"
                    + " lastName ,"
                    + " firstName ,"
                    + " extension ,"
                    + " email ,"
                    + " officeCode ,"
                    + " reportsTo ,"
                    + " jobTitle)"
                    + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement st = conexionBBDD.prepareStatement(SQL);
            st.setInt(1, Integer.parseInt(tfEmployeeNumber.getText()));
            st.setString(2, tfLastName.getText());
            st.setString(3, tfFirstName.getText());
            st.setString(4, tfExtension.getText());
            st.setString(5, tfEmail.getText());
            st.setString(6, tfOfficeCode.getText());
            st.setInt(7, Integer.parseInt(tfReportsTo.getText()));
            st.setString(8, tfJobTitle.getText());

            // Ejecutamos la consulta preparada con los empleados de seguridad y velocidad en el servidor de BBDD
            // nos devuelve el número de registros afectados. Al ser un Insert nos debe devolver 1 si se ha hecho correctamente
            st.executeUpdate();
            st.close();
            conexionBBDD.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error:" + e);
        }
    }

    @FXML
    protected void volver() {
        Stage stageAct = (Stage) this.tfEmployeeNumber.getScene().getWindow();
        stageAct.close();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Inicio.class.getResource("main.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 755, 600);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Empleados");
        stage.setMinWidth(650.0);
        stage.setMinHeight(400.0);
        stage.setScene(scene);
        stage.show();
    }
}

