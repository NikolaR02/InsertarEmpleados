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

import java.io.IOException;
import java.sql.*;

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

    private final String servidor = "jdbc:mariadb://localhost:5555/noinch?useSSL=false";
    private final String usuario = "root";
    private final String passwd = "adminer";
    private Connection conexionBBDD;

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


        //  ---DAO
        ObservableList<Employee> datosResultadoConsulta = FXCollections.observableArrayList();
        try {
            conexionBBDD = DriverManager.getConnection(servidor, usuario, passwd);
            String SQL = "SELECT * "
                    + " FROM employees ";
            if (!tfCodigo.getText().equals("")) {
                SQL += " WHERE employeeNumber LIKE '%" + tfCodigo.getText() + "%' " +
                        " OR lastName LIKE '%" + tfCodigo.getText() + "%'" +
                        " OR firstName LIKE '%" + tfCodigo.getText() + "%' ";
            }
            SQL += " ORDER By lastName";


            // Ejecutamos la consulta y nos devuelve una matriz de filas (registros) y columnas (datos)
            ResultSet resultadoConsulta = conexionBBDD.createStatement().executeQuery(SQL);

            // Debemos cargar los datos en el ObservableList (Que es un ArrayList especial)
            // Los datos que devuelve la consulta no son directamente cargables en nuestro objeto
            while (resultadoConsulta.next()) {
                datosResultadoConsulta.add(new Employee(
                        resultadoConsulta.getInt("employeeNumber"),
                        resultadoConsulta.getString("lastName"),
                        resultadoConsulta.getString("firstName"),
                        resultadoConsulta.getString("extension"),
                        resultadoConsulta.getString("email"),
                        resultadoConsulta.getString("officeCode"),
                        resultadoConsulta.getInt("reportsTo"),
                        resultadoConsulta.getString("jobTitle"))
                );
                System.out.println("Row [1] added " + resultadoConsulta);
            }
            conexionBBDD.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error:" + e);
        }
        // ---

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
                try {

                    conexionBBDD = DriverManager.getConnection(servidor, usuario, passwd);

                    String SQL = "DELETE FROM employees "
                            + " WHERE employeeNumber = ? ";

                    st = conexionBBDD.prepareStatement(SQL);
                    st.setString(1, String.valueOf(idSeleccionado));

                    incertado = st.executeUpdate() == 1;


                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error:" + e);
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
}