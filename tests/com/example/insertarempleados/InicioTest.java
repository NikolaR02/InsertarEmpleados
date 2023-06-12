package com.example.insertarempleados;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ApplicationExtension.class)
class InicioTest {
    Pane mainroot;
    Stage mainstage;

    @Start
    public void start(Stage stage) throws IOException {
        mainroot = (Pane) FXMLLoader.load(getClass().getResource("main.fxml"));
        mainstage = stage;
        stage.setScene(new Scene(mainroot));
        stage.show();
        stage.toFront();
    }

    @Order(1)
    @Test
    void al_pulsar_boton_se_abre_una_nueva_ventana(FxRobot robot) {

        robot.clickOn("#insertar");
        robot.sleep(1000);
        FxAssert.verifyThat(robot.window("Insertar"), WindowMatchers.isShowing());

    }

    @Order(2)
    @Test
    void prueba_insercion() {
        // Código de inserción aquí
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Establecer conexión a la base de datos
            connection = DBConnection.getConnection();

            // Crear el SQL para la inserción de un nuevo empleado
            String SQL = "INSERT INTO employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            // Crear el objeto PreparedStatement
            statement = connection.prepareStatement(SQL);

            // Establecer los valores para el nuevo empleado
            statement.setInt(1, 12345);
            statement.setString(2, "Smith");
            statement.setString(3, "John");
            statement.setString(4, "123");
            statement.setString(5, "jsmith@example.com");
            statement.setInt(6, 1);
            statement.setInt(7, 9876);
            statement.setString(8, "Manager");

            // Ejecutar la inserción
            int rowsAffected = statement.executeUpdate();

            // Verificar que la inserción se realizó correctamente
            assertEquals(1, rowsAffected, "La inserción no se realizó correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error al ejecutar la inserción");
        } finally {
            // Cerrar la conexión
            DBConnection.closeConnection(connection);
        }
    }

    @Order(3)
    @Test
    void prueba_modificacion() {
        // Código de modificación aquí
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Establecer conexión a la base de datos
            connection = DBConnection.getConnection();

            // Crear el SQL para la modificación de un empleado existente
            String SQL = "UPDATE employees SET email = ? WHERE employeeNumber = ?";

            // Crear el objeto PreparedStatement
            statement = connection.prepareStatement(SQL);

            // Establecer los valores para la modificación
            statement.setString(1, "newemail@example.com");
            statement.setInt(2, 12345);

            // Ejecutar la modificación
            int rowsAffected = statement.executeUpdate();

            // Verificar que la modificación se realizó correctamente
            assertEquals(1, rowsAffected, "La modificación no se realizó correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error al ejecutar la modificación");
        } finally {
            // Cerrar la conexión
            DBConnection.closeConnection(connection);
        }
    }


    @Order(4)
    @Test
    void prueba_borrado() {
        // Código de borrado aquí
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Establecer conexión a la base de datos
            connection = DBConnection.getConnection();

            // Crear el SQL para el borrado de un empleado existente
            String SQL = "DELETE FROM employees WHERE employeeNumber = ?";

            // Crear el objeto PreparedStatement
            statement = connection.prepareStatement(SQL);

            // Establecer el valor para el empleado a borrar
            statement.setInt(1, 12345);

            // Ejecutar el borrado
            int rowsAffected = statement.executeUpdate();

            // Verificar que el borrado se realizó correctamente
            assertEquals(1, rowsAffected, "El borrado no se realizó correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error al ejecutar el borrado");
        } finally {
            // Cerrar la conexión
            DBConnection.closeConnection(connection);
        }
    }

}