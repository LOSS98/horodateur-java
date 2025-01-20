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

public class ReportDAO {
    private final Connection connection;

    public ReportDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertRapport(double tauxOccupation, double totalRecettes, String emailAdmin,
                              String dateTimeDebut, String dateTimeFin) throws SQLException {
        String query = "INSERT INTO Rapport (tauxOccupation, totalRecettes, emailAdmin, " +
                "dateGeneration, dateTimeDebut, dateTimeFin) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, tauxOccupation);
            statement.setDouble(2, totalRecettes);
            statement.setString(3, emailAdmin);
            statement.setString(4, dateTimeDebut);
            statement.setString(5, dateTimeFin);
            statement.executeUpdate();
        }
    }

    public List<Rapport> getAllReports() throws SQLException {
        List<Rapport> rapports = new ArrayList<>();
        String query = "SELECT * FROM Rapport";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s");
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

