package fr.insa.horodateurjava.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class HistoryDAO {

    private final Connection connection;

    public HistoryDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Ajoute une nouvelle réservation à l'historique.
     *
     * @param numeroReservation Numéro de la réservation.
     * @param immatriculation   Immatriculation du véhicule.
     * @param numeroPlace       Numéro de la place réservée.
     * @param dateHeureDebut    Date et heure de début de la réservation.
     * @param duration          Durée de la réservation en heures.
     * @throws SQLException Si une erreur SQL survient.
     */
    public void addReservationToHistory(int numeroReservation, String immatriculation, int numeroPlace, LocalDateTime dateHeureDebut, int duration) throws SQLException {
        // Calcul de la date et heure de fin
        LocalDateTime dateHeureFin = dateHeureDebut.plusHours(duration);

        String query = "INSERT INTO HistoriqueReservation (numeroReservation, immatriculation, numeroPlace, dateHeureDebut, dateHeureFin) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, numeroReservation);
            preparedStatement.setString(2, immatriculation);
            preparedStatement.setInt(3, numeroPlace);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(dateHeureDebut));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(dateHeureFin));

            preparedStatement.executeUpdate();
        }
    }
}
