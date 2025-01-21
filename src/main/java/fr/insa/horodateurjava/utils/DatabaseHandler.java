package fr.insa.horodateurjava.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour gérer les connexions à la base de données SQLite.
 * Cette classe fournit une méthode statique permettant d'obtenir une connexion à la base de données.
 */
public class DatabaseHandler {

    // URL de la base de données SQLite
    private static final String URL = "jdbc:sqlite:src\\main\\resources\\fr\\insa\\horodateurjava\\database.db";

    /**
     * Méthode statique pour obtenir une connexion à la base de données.
     *
     * @return Une instance de connexion à la base de données.
     * @throws SQLException Si une erreur survient lors de la connexion.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
