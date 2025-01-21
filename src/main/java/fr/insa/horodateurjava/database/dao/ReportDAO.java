package fr.insa.horodateurjava.database.dao;

import fr.insa.horodateurjava.models.Rapport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour gérer les rapports générés dans l'application.
 */
public class ReportDAO {
    private final Connection connection;

    /**
     * Constructeur de la classe ReportDAO.
     *
     * @param connection Connexion à la base de données.
     */
    public ReportDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Insère un nouveau rapport dans la table Rapport.
     *
     * @param tauxOccupation Le taux d'occupation calculé.
     * @param totalRecettes  Le total des recettes générées.
     * @param emailAdmin     L'email de l'administrateur ayant généré le rapport.
     * @param dateTimeDebut  La date et l'heure de début de la période du rapport.
     * @param dateTimeFin    La date et l'heure de fin de la période du rapport.
     * @throws SQLException En cas d'erreur SQL.
     */
    public void insertRapport(double tauxOccupation, double totalRecettes, String emailAdmin,
                              String dateTimeDebut, String dateTimeFin) throws SQLException {
        String query = """
                INSERT INTO Rapport (tauxOccupation, totalRecettes, emailAdmin, dateGeneration, dateTimeDebut, dateTimeFin)
                VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, tauxOccupation);
            statement.setDouble(2, totalRecettes);
            statement.setString(3, emailAdmin);
            statement.setString(4, dateTimeDebut);
            statement.setString(5, dateTimeFin);

            statement.executeUpdate();
        }
    }

    /**
     * Récupère tous les rapports de la table Rapport.
     *
     * @return Une liste de rapports {@link Rapport}.
     * @throws SQLException En cas d'erreur SQL.
     */
    public List<Rapport> getAllReports() throws SQLException {
        List<Rapport> rapports = new ArrayList<>();
        String query = "SELECT * FROM Rapport";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");

            while (resultSet.next()) {
                Rapport rapport = new Rapport(
                        resultSet.getInt("idRapport"),
                        resultSet.getDouble("tauxOccupation"),
                        resultSet.getDouble("totalRecettes"),
                        LocalDateTime.parse(resultSet.getString("dateGeneration"), formatter),
                        resultSet.getString("emailAdmin"),
                        LocalDateTime.parse(resultSet.getString("dateTimeDebut"), formatter),
                        LocalDateTime.parse(resultSet.getString("dateTimeFin"), formatter)
                );
                rapports.add(rapport);
            }
        }

        return rapports;
    }
}
