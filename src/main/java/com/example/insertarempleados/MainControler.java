package com.example.insertarempleados;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainControler {
    @FXML
    private TableColumn<Object, Object> tcCodigo;
    @FXML
    private TableColumn<Object, Object> tcApellido;
    @FXML
    private TableColumn<Object, Object> tcNombre;
    @FXML
    private TableColumn<Object, Object> tcExtencion;
    @FXML
    private TableColumn<Object, Object> tcEmail;
    @FXML
    private TableColumn<Object, Object> tcOficina;
    @FXML
    private TableColumn<Object, Object> tcSuperior;
    @FXML
    private TableColumn<Object, Object> tcTitulo;
    @FXML
    private TableView<Employee> tabla;
    @FXML
    private TextField tfCodigo;

    static int idSeleccionado=-1;
    public static Employee empleadoSeleccionado;
    public static MainControler ventana;


    public void initialize() {
        ventana = this;
        cargar();
        tabla.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    idSeleccionado = row.getItem().getEmployeeNumber();
                    empleadoSeleccionado = row.getItem();
                    if (event.getClickCount() == 2) actualizar();
                }
            });
            return row;
        });
    }

    @FXML
    public void cargar() {
        tcCodigo.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        tcApellido.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tcExtencion.setCellValueFactory(new PropertyValueFactory<>("extension"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcOficina.setCellValueFactory(new PropertyValueFactory<>("officeCode"));
        tcSuperior.setCellValueFactory(new PropertyValueFactory<>("reportsTo"));
        tcTitulo.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));

        ObservableList<Employee> datosResultadoConsulta = FXCollections.observableArrayList();
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            String SQL = "SELECT e.employeeNumber, e.lastName, e.firstName, e.extension, e.email, " +
                    "o.city AS officeCity, r.lastName AS superiorLastName, e.jobTitle " +
                    "FROM employees e " +
                    "JOIN offices o ON e.officeCode = o.officeCode " +
                    "LEFT JOIN employees r ON e.reportsTo = r.employeeNumber ";
            if (!tfCodigo.getText().equals("")) {
                SQL += "WHERE e.employeeNumber LIKE '%" + tfCodigo.getText() + "%' " +
                        "OR e.lastName LIKE '%" + tfCodigo.getText() + "%' " +
                        "OR e.firstName LIKE '%" + tfCodigo.getText() + "%' ";
            }
            SQL += "ORDER BY o.city";

            ResultSet resultadoConsulta = c.createStatement().executeQuery(SQL);

            while (resultadoConsulta.next()) {
                int employeeNumber = resultadoConsulta.getInt("employeeNumber");
                String lastName = resultadoConsulta.getString("lastName");
                String firstName = resultadoConsulta.getString("firstName");
                String extension = resultadoConsulta.getString("extension");
                String email = resultadoConsulta.getString("email");
                String officeCity = resultadoConsulta.getString("officeCity");
                String superiorLastName = resultadoConsulta.getString("superiorLastName");
                String jobTitle = resultadoConsulta.getString("jobTitle");

                datosResultadoConsulta.add(new Employee(employeeNumber, lastName, firstName, extension, email, officeCity, superiorLastName, jobTitle));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error:" + e);
        } finally {
            DBConnection.closeConnection(c);
        }

        tabla.setItems(datosResultadoConsulta);
    }


    @FXML
    protected void insertar() {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Inicio.class.getResource("incertar.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 420, 540);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Insertar");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }


    @FXML
    protected void borrar() {

        Alert alert;
        if (idSeleccionado != -1) {

            alert = new Alert(Alert.AlertType.CONFIRMATION, "¿ Desea borrar el empleado con el código '"
                    + idSeleccionado + "' ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                boolean incertado = false;

                // --- DAO
                PreparedStatement st;
                Connection c = null;
                try {
                    c = DBConnection.getConnection();

                    String SQL = "DELETE FROM employees "
                            + " WHERE employeeNumber = ? ";

                    st = c.prepareStatement(SQL);
                    st.setString(1, String.valueOf(idSeleccionado));

                    incertado = st.executeUpdate() == 1;

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error:" + e);
                } finally {
                    DBConnection.closeConnection(c);
                }
                // ---

                if (incertado) {
                    idSeleccionado = -1;
                    cargar();
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION, "No se ha encontrado el empleado seleccionado '"
                            + idSeleccionado + "' o no se puede borrar.", ButtonType.OK);
                    alert.showAndWait();
                }
            }

        } else {
            alert = new Alert(Alert.AlertType.INFORMATION, "Debe seleccionar un empleado con click sobre la tabla para borrar.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void actualizar() {

        if (idSeleccionado != -1) {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Inicio.class.getResource("actualizar.fxml"));
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load(), 420, 540);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setTitle("Actualizar");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Debe seleccionar un empleado con click sobre la tabla para actualizar.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    public void informe() {
        try {
            // --- Show Jasper Report on click-----
            new Report().showReportSimple();
        } catch (ClassNotFoundException | JRException | SQLException e1) {
            e1.printStackTrace();
        }
    }
}