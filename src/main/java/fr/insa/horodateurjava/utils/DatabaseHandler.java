package fr.insa.horodateurjava.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {

    private static final String URL = "jdbc:sqlite:src\\main\\resources\\fr\\insa\\horodateurjava\\database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
