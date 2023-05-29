package com.example.insertarempleados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String SERVER = "jdbc:mariadb://localhost:5555/noinch?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "adminer";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(SERVER, USER, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
