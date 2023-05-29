package com.example.insertarempleados;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class ActualizarControler {
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
    HashMap<String, Integer> hmEmployees = new HashMap<>();

    public void initialize() {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            String officeQuery = "SELECT city, officeCode FROM offices ORDER BY officeCode";
            String employeesQuery = "SELECT employeeNumber, firstName, lastName FROM employees ORDER BY lastName";

            ResultSet officeResult = c.createStatement().executeQuery(officeQuery);
            while (officeResult.next()) {
                cboOficina.getItems().add(officeResult.getString("city"));
                hmOficinas.put(officeResult.getString("city"), officeResult.getString("officeCode"));
            }

            ResultSet employeesResult = c.createStatement().executeQuery(employeesQuery);
            while (employeesResult.next()) {
                String name = employeesResult.getString("firstName") + " " + employeesResult.getString("lastName");
                String number = employeesResult.getString("employeeNumber");
                cboReportsTo.getItems().add(name);
                hmEmployees.put(name, Integer.parseInt(number));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e);
        } finally {
            DBConnection.closeConnection(c);
        }

        tfEmployeeNumber.setText(String.valueOf(MainControler.empleadoSeleccionado.getEmployeeNumber()));
        tfLastName.setText(MainControler.empleadoSeleccionado.getLastName());
        tfFirstName.setText(MainControler.empleadoSeleccionado.getFirstName());
        tfExtension.setText(MainControler.empleadoSeleccionado.getExtension());
        tfEmail.setText(MainControler.empleadoSeleccionado.getEmail());
        cboOficina.setValue(MainControler.empleadoSeleccionado.getOfficeCode());
        cboReportsTo.setValue(MainControler.empleadoSeleccionado.getReportsTo());
        tfJobTitle.setText(MainControler.empleadoSeleccionado.getJobTitle());
    }

    @FXML
    public void actualizarEmployee() {
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            String SQL = "UPDATE employees "
                    + "SET employeeNumber = ?,"
                    + "lastName = ?,"
                    + "firstName = ?,"
                    + "extension = ?,"
                    + "email = ?,"
                    + "officeCode = ?,"
                    + "reportsTo = ?,"
                    + "jobTitle = ? "
                    + "WHERE employeeNumber = " + MainControler.idSeleccionado;

            PreparedStatement st = connection.prepareStatement(SQL);
            st.setInt(1, Integer.parseInt(tfEmployeeNumber.getText()));
            st.setString(2, tfLastName.getText());
            st.setString(3, tfFirstName.getText());
            st.setString(4, tfExtension.getText());
            st.setString(5, tfEmail.getText());
            st.setString(6, hmOficinas.get(cboOficina.getValue()));
            st.setInt(7, hmEmployees.get(cboReportsTo.getValue()));
            st.setString(8, tfJobTitle.getText());

            st.executeUpdate();
            st.close();
            DBConnection.closeConnection(connection);
            MainControler.ventana.cargar();
            volver();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e);
        } finally {
            DBConnection.closeConnection(connection);
        }
    }

    @FXML
    protected void volver() {
        Stage stageAct = (Stage) tfEmployeeNumber.getScene().getWindow();
        stageAct.close();
    }
}
