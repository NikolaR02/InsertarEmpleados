package com.example.insertarempleados;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.WindowMatchers;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class InicioTest {
    Pane mainroot;
    Stage mainstage;

    @Start
    public void start(Stage stage) throws IOException {
        mainroot = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage.setScene(new Scene(mainroot));
        stage.show();
        stage.toFront();
        mainstage = stage;
    }

    @Test
    void abrirVentanaInsertar(FxRobot robot) {
        robot.clickOn("#insertar");
        robot.sleep(1000);
        FxAssert.verifyThat(robot.window("Insertar"), WindowMatchers.isShowing());
    }

    @Test
    void insertar(FxRobot robot) {
        boolean insertado = false;

        robot.clickOn("#insertar");
        robot.sleep(1000);

        robot.write("1111");
        robot.type(KeyCode.TAB);
        robot.write("Rosenov");
        robot.type(KeyCode.TAB);
        robot.write("Nicola");
        robot.type(KeyCode.TAB);
        robot.write("x1234");
        robot.type(KeyCode.TAB);
        robot.write("nicola@ejemplo.falso");
        robot.clickOn("#cboOficina");
        robot.clickOn("NYC");
        robot.clickOn("#cboReportsTo");
        robot.clickOn("Doe, John");
        robot.type(KeyCode.TAB);
        robot.write("Sale Rep");
        robot.clickOn("#altaEmployee");
        robot.sleep(1000);

        for (Employee e : MainControler.ventana.getTabla().getItems()) {
            if (e.getEmployeeNumber() == 1111) {
                insertado = true;
                break;
            }
        }
        assertTrue(insertado);
    }
}