package fr.insa.horodateurjava.database.dao;

import fr.insa.horodateurjava.models.Reservation;
import fr.insa.horodateurjava.utils.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationDAO {

    private final Connection connection;

    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    public int getDistinctPlacesCount() {
        int distinctPlacesCount = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            connection = DatabaseHandler.getConnection();
            String query = "SELECT COUNT(DISTINCT numeroPlace) AS distinct_places FROM Reservation";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                distinctPlacesCount = resultSet.getInt("distinct_places");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return distinctPlacesCount;
    }

    public double getTotalRevenue() {
        double totalRevenue = 0.0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            connection = DatabaseHandler.getConnection();
            String query = "SELECT SUM(prix) AS total_revenue FROM Reservation";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalRevenue = resultSet.getDouble("total_revenue");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return totalRevenue;
    }

    public int getTotalReservations() {
        int totalReservations = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            connection = DatabaseHandler.getConnection();
            String query = "SELECT COUNT(*) AS total_reservations FROM Reservation";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalReservations = resultSet.getInt("total_reservations");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return totalReservations;
    }
    public List<Map<String, Object>> getReservationsBetween(String dateHeureDebut, String dateHeureFin) throws SQLException, SQLException {
        String query = "SELECT numeroReservation, immatriculation, numeroPlace, dateHeureDebut, dateHeureFin, prix " +
                "FROM Reservation WHERE dateHeureDebut >= ? AND dateHeureFin <= ?";
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

    public int getDistinctPlacesCountBetween(String startDateTime, String endDateTime) {
        int distinctPlacesCount = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            connection = DatabaseHandler.getConnection();
            String query = "SELECT COUNT(DISTINCT numeroPlace) AS distinct_places " +
                    "FROM Reservation " +
                    "WHERE dateHeureDebut >= ? AND dateHeureFin <= ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, startDateTime);
            statement.setString(2, endDateTime);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                distinctPlacesCount = resultSet.getInt("distinct_places");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return distinctPlacesCount;
    }


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
