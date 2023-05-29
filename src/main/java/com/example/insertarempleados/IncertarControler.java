package com.example.insertarempleados;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
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
    private TextField tfJobTitle;
    @FXML
    private ChoiceBox<String> cboOficina;
    @FXML
    private ChoiceBox<String> cboReportsTo;

    HashMap<String, String> hmOficinas = new HashMap<>();
    HashMap<String, String> hmEmployees = new HashMap<>();

    public void initialize() {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            String SQL = "SELECT city, officeCode FROM offices ORDER BY officeCode";
            ResultSet resultadoConsulta = c.createStatement().executeQuery(SQL);
            while (resultadoConsulta.next()) {
                // Agregar elementos al desplegable
                cboOficina.getItems().add(resultadoConsulta.getString("city"));
                // Agregar elementos al mapa
                hmOficinas.put(resultadoConsulta.getString("city"), resultadoConsulta.getString("officeCode"));
            }

            String SQL2 = "SELECT employeeNumber, CONCAT(firstName, ' ', lastName) AS name FROM employees ORDER BY employeeNumber";
            ResultSet resultadoConsulta2 = c.createStatement().executeQuery(SQL2);
            while (resultadoConsulta2.next()) {
                // Agregar elementos al desplegable
                cboReportsTo.getItems().add(resultadoConsulta.getString("name"));
                // Agregar elementos al mapa
                hmEmployees.put(resultadoConsulta.getString("name"), resultadoConsulta.getString("employeeNumber"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e);
        } finally {
            DBConnection.closeConnection(c);
        }

        // Establecer un valor predeterminado
        cboOficina.setValue("Elige una oficina");

        // Establecer un valor predeterminado
        cboReportsTo.setValue("Elige un supervisor");
    }

    @FXML
    public void altaEmployee() {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
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

            PreparedStatement st = c.prepareStatement(SQL);
            st.setInt(1, Integer.parseInt(tfEmployeeNumber.getText()));
            st.setString(2, tfLastName.getText());
            st.setString(3, tfFirstName.getText());
            st.setString(4, tfExtension.getText());
            st.setString(5, tfEmail.getText());
            st.setString(6, hmOficinas.get(cboOficina.getValue()));
            st.setInt(7, Integer.parseInt(hmEmployees.get(cboReportsTo.getValue())));
            st.setString(8, tfJobTitle.getText());

            // Ejecutamos la consulta preparada con los empleados de seguridad y velocidad en el servidor de BBDD
            // nos devuelve el número de registros afectados. Al ser un Insert nos debe devolver 1 si se ha hecho correctamente
            st.executeUpdate();
            st.close();
            c.close();
            MainControler.ventana.cargar();
            volver();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error:" + e);
        } finally {
            DBConnection.closeConnection(c);
        }
    }

    @FXML
    protected void volver() {
        Stage stageAct = (Stage) this.tfEmployeeNumber.getScene().getWindow();
        stageAct.close();
    }
}

