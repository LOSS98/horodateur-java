package fr.insa.horodateurjava.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Classe DAO pour gérer l'historique des réservations dans la base de données.
 */
public class HistoryDAO {

    private final Connection connection;

    /**
     * Constructeur de la classe HistoryDAO.
     *
     * @param connection Connexion à la base de données.
     */
    public HistoryDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Ajoute une nouvelle réservation à l'historique.
     *
     * @param numeroReservation Numéro unique de la réservation.
     * @param immatriculation   Immatriculation du véhicule réservé.
     * @param numeroPlace       Numéro de la place réservée.
     * @param dateHeureDebut    Date et heure de début de la réservation.
     * @param duration          Durée de la réservation en heures.
     * @throws SQLException Si une erreur SQL survient lors de l'ajout à la base de données.
     */
    public void addReservationToHistory(int numeroReservation, String immatriculation, int numeroPlace, LocalDateTime dateHeureDebut, int duration) throws SQLException {
        // Calculer la date et heure de fin en ajoutant la durée à la date de début
        LocalDateTime dateHeureFin = dateHeureDebut.plusHours(duration);

        // Requête SQL pour insérer une réservation dans la table HistoriqueReservation
        String query = "INSERT INTO HistoriqueReservation (numeroReservation, immatriculation, numeroPlace, dateHeureDebut, dateHeureFin) " +
                "VALUES (?, ?, ?, ?, ?)";

        // Préparation et exécution de la requête
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, numeroReservation); // Insérer le numéro de réservation
            preparedStatement.setString(2, immatriculation); // Insérer l'immatriculation du véhicule
            preparedStatement.setInt(3, numeroPlace); // Insérer le numéro de place réservée
            preparedStatement.setTimestamp(4, Timestamp.valueOf(dateHeureDebut)); // Insérer la date et heure de début
            preparedStatement.setTimestamp(5, Timestamp.valueOf(dateHeureFin)); // Insérer la date et heure de fin

            preparedStatement.executeUpdate(); // Exécuter la requête
        }
    }
}
