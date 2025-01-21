package fr.insa.horodateurjava.database.dao;

import fr.insa.horodateurjava.models.Reservation;
import fr.insa.horodateurjava.utils.DatabaseHandler;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO pour gérer les réservations dans la base de données.
 */
public class ReservationDAO {

    private final Connection connection;

    /**
     * Constructeur de la classe ReservationDAO.
     *
     * @param connection Connexion à la base de données.
     */
    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Récupère le nombre total de places distinctes ayant été réservées.
     *
     * @return Nombre total de places distinctes.
     */
    public int getDistinctPlacesCount() {
        int distinctPlacesCount = 0;
        String query = "SELECT COUNT(DISTINCT numeroPlace) AS distinct_places FROM Reservation";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                distinctPlacesCount = resultSet.getInt("distinct_places");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return distinctPlacesCount;
    }

    /**
     * Récupère le total des revenus générés par les réservations.
     *
     * @return Le total des revenus.
     */
    public double getTotalRevenue() {
        double totalRevenue = 0.0;
        String query = "SELECT SUM(prix) AS total_revenue FROM Reservation";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                totalRevenue = resultSet.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRevenue;
    }

    /**
     * Récupère le nombre total de réservations.
     *
     * @return Nombre total de réservations.
     */
    public int getTotalReservations() {
        int totalReservations = 0;
        String query = "SELECT COUNT(*) AS total_reservations FROM Reservation";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                totalReservations = resultSet.getInt("total_reservations");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalReservations;
    }

    /**
     * Récupère toutes les réservations dans une période donnée.
     *
     * @param dateHeureDebut Date et heure de début.
     * @param dateHeureFin   Date et heure de fin.
     * @return Liste des réservations sous forme de map.
     * @throws SQLException En cas d'erreur SQL.
     */
    public List<Map<String, Object>> getReservationsBetween(String dateHeureDebut, String dateHeureFin) throws SQLException {
        String query = """
                SELECT numeroReservation, immatriculation, numeroPlace, dateHeureDebut, dateHeureFin, prix
                FROM Reservation
                WHERE dateHeureDebut >= ? AND dateHeureFin <= ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dateHeureDebut);
            statement.setString(2, dateHeureFin);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Map<String, Object>> reservations = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, Object> reservation = new HashMap<>();
                    reservation.put("numeroReservation", resultSet.getInt("numeroReservation"));
                    reservation.put("immatriculation", resultSet.getString("immatriculation"));
                    reservation.put("numeroPlace", resultSet.getInt("numeroPlace"));
                    reservation.put("dateHeureDebut", resultSet.getString("dateHeureDebut"));
                    reservation.put("dateHeureFin", resultSet.getString("dateHeureFin"));
                    reservation.put("prix", resultSet.getDouble("prix"));
                    reservations.add(reservation);
                }
                return reservations;
            }
        }
    }

    /**
     * Récupère le nombre de places distinctes ayant été réservées dans une période donnée.
     *
     * @param startDateTime Date et heure de début.
     * @param endDateTime   Date et heure de fin.
     * @return Nombre de places distinctes réservées.
     */
    public int getDistinctPlacesCountBetween(String startDateTime, String endDateTime) {
        int distinctPlacesCount = 0;
        String query = """
                SELECT COUNT(DISTINCT numeroPlace) AS distinct_places
                FROM Reservation
                WHERE dateHeureDebut >= ? AND dateHeureFin <= ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, startDateTime);
            statement.setString(2, endDateTime);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    distinctPlacesCount = resultSet.getInt("distinct_places");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return distinctPlacesCount;
    }

    /**
     * Récupère toutes les réservations.
     *
     * @return Liste des réservations.
     */
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM Reservation";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("numeroReservation"),
                        resultSet.getString("immatriculation"),
                        resultSet.getInt("numeroPlace"),
                        LocalDateTime.parse(resultSet.getString("dateHeureDebut"), formatter),
                        LocalDateTime.parse(resultSet.getString("dateHeureFin"), formatter),
                        resultSet.getInt("idParking"),
                        resultSet.getDouble("prix")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }
}
