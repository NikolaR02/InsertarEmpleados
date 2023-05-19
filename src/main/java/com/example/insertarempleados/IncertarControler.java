package com.example.insertarempleados;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class IncertarControler {

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
    private TextField tfReportsTo;
    @FXML
    private TextField tfJobTitle;
    @FXML
    private ChoiceBox<String> cboOficina;

    HashMap<String, String> hmOficinas = new HashMap<String, String>();
    String servidor = "jdbc:mariadb://localhost:5555/noinch?useSSL=false";
    String usuario = "root";
    String passwd = "adminer";

    public void initialize() {

        try {
            Connection conexionBBDD = DriverManager.getConnection(servidor, usuario, passwd);
            String SQL = "SELECT city, officeCode "
                    + " FROM offices "
                    + " ORDER By officeCode";


            ResultSet resultadoConsulta = conexionBBDD.createStatement().executeQuery(SQL);
            while (resultadoConsulta.next()) {
                // Agregar elementos al desplegable
                cboOficina.getItems().add(resultadoConsulta.getString("city"));
                // Agregar elementos al mapa
                hmOficinas.put(resultadoConsulta.getString("city"), resultadoConsulta.getString("officeCode"));

            }
            conexionBBDD.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error:" + e);
        }

        // Establecer un valor predeterminado
        cboOficina.setValue("Elige una oficina");
    }

    @FXML
    public void altaEmployee() {
        try {
            String servidor = "jdbc:mariadb://localhost:5555/noinch?useSSL=false";
            String usuario = "root";
            String passwd = "adminer";
            Connection conexionBBDD = DriverManager.getConnection(servidor, usuario, passwd);
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
            st.setString(6, hmOficinas.get(cboOficina.getValue()));
            st.setInt(7, Integer.parseInt(tfReportsTo.getText()));
            st.setString(8, tfJobTitle.getText());

            // Ejecutamos la consulta preparada con los empleados de seguridad y velocidad en el servidor de BBDD
            // nos devuelve el número de registros afectados. Al ser un Insert nos debe devolver 1 si se ha hecho correctamente
            st.executeUpdate();
            st.close();
            conexionBBDD.close();
            MainControler.ventana.cargar();
            volver();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error:" + e);
        }
    }

    @FXML
    protected void volver() {
        Stage stageAct = (Stage) this.tfEmployeeNumber.getScene().getWindow();
        stageAct.close();
    }
}

