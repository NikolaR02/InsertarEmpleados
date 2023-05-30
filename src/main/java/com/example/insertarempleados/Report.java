package com.example.insertarempleados;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
//import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Report extends JFrame {

    //@Serial
    private static final long serialVersionUID = 1L;
    /*
     * imagen
     * numero de lágina
     * tolaizadores
     * */
    public void showReportSimple() throws JRException, ClassNotFoundException, SQLException {

        String servidor = "jdbc:mariadb://localhost:5555/noinch?useSSL=false";
        String usuario = "adminer";
        String passwd = "adminer";

        Connection conexionBBDD;
        // Nos conectamos
        conexionBBDD = DriverManager.getConnection(servidor, usuario, passwd);

        //  Block of code to try
        String reportSrcFile = "src/main/resources/com/example/insertarempleados/empleado.jrxml";

        // First, compile jrxml file.
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);

        // Ejemplo de definición de parámetros para el informe

        HashMap<String, Object> parameters = new HashMap<>();

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conexionBBDD);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(1200, 1000);
        this.setVisible(true);
        System.out.print("Done!");
    }
}
