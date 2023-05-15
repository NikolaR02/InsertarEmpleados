package com.example.insertarempleados;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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
    private TextField tfOfficeCode;
    @FXML
    private TextField tfReportsTo;
    @FXML
    private TextField tfJobTitle;

    public void initialize() {
        tfEmployeeNumber.setText(String.valueOf(MainControler.empleadoSeleccionado.getEmployeeNumber()));
        tfLastName.setText(MainControler.empleadoSeleccionado.getLastName());
        tfFirstName.setText(MainControler.empleadoSeleccionado.getFirstName());
        tfExtension.setText(MainControler.empleadoSeleccionado.getExtension());
        tfEmail.setText(MainControler.empleadoSeleccionado.getEmail());
        tfOfficeCode.setText(MainControler.empleadoSeleccionado.getOfficeCode());
        tfReportsTo.setText(String.valueOf(MainControler.empleadoSeleccionado.getReportsTo()));
        tfJobTitle.setText(MainControler.empleadoSeleccionado.getJobTitle());
    }

    @FXML
    public void actualizarEmployee() {
        try {
            String servidor = "jdbc:mariadb://localhost:5555/noinch?useSSL=false";
            String usuario = "root";
            String passwd = "adminer";
            Connection conexionBBDD = DriverManager.getConnection(servidor, usuario, passwd);

            String SQL = "UPDATE employees "
                        + " SET employeeNumber = ? ,"
                        + " lastName = ? ,"
                        + " firstName = ? ,"
                        + " extension = ? ,"
                        + " email = ? ,"
                        + " officeCode = ? ,"
                        + " reportsTo = ? ,"
                        + " jobTitle = ? "
                        + " WHERE employeeNumber = " + MainControler.idSeleccionado;

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
