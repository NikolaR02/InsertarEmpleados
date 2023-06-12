package com.example.insertarempleados;

import com.example.insertarempleados.DBConnection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Connection;
import java.sql.Statement;

public class TestUnitario {
    @Test
    public void insertar() {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            String SQL = "INSERT INTO employees ("
                    + "employeeNumber ,"
                    + "lastName ,"
                    + "firstName ,"
                    + "extension ,"
                    + "email ,"
                    + "officeCode ,"
                    + "reportsTo ,"
                    + "jobTitle)"
                    + "VALUES (1234, 'Doe', 'John', 'x123', 'johndoe@example.com', '1', 1337, 'Manager')";

            Statement st = c.createStatement();
            assertEquals(1,st.executeUpdate(SQL));
            st.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e);
        } finally {
            DBConnection.closeConnection(c);
        }
    }

    @Test
    public void borrar() {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            String SQL = "DELETE FROM employees "
                    + "WHERE employeeNumber = 1234";

            Statement st = c.createStatement();
            assertEquals(1,st.executeUpdate(SQL));
            st.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e);
        } finally {
            DBConnection.closeConnection(c);
        }
    }
}
