package fr.insa.horodateurjava.database.dao;

import fr.insa.horodateurjava.models.Parking;
import fr.insa.horodateurjava.utils.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingDAO {
    private final Connection connection;

    public ParkingDAO(Connection connection) {
        this.connection = connection;
    }

    public List<String> getAllParkingNames() throws SQLException {
        List<String> parkingNames = new ArrayList<>();
        String query = "SELECT nomDuParking FROM Parking";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                parkingNames.add(resultSet.getString("nomDuParking"));
            }
        }

        return parkingNames;
    }

    public List<String> getAllParkingNamesWithIds() throws SQLException {
        List<String> parkingNames = new ArrayList<>();
        String query = "SELECT idParking, nomDuParking FROM Parking";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("idParking");
                String name = resultSet.getString("nomDuParking");
                parkingNames.add(id + " - " + name);
            }
        }
        return parkingNames;
    }

    public boolean updateParking(int idParking, String nom, String adresse, Integer nombrePlaces) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE Parking SET ");
        boolean hasField = false;

        if (nom != null && !nom.isEmpty()) {
            query.append("nomDuParking = ?");
            hasField = true;
        }
        if (adresse != null && !adresse.isEmpty()) {
            if (hasField) query.append(", ");
            query.append("adresseDuParking = ?");
            hasField = true;
        }
        if (nombrePlaces != null) {
            if (hasField) query.append(", ");
            query.append("nombrePlaces = ?");
            hasField = true;
        }

        if (!hasField) {
            throw new IllegalArgumentException("Aucun champ à mettre à jour.");
        }

        query.append(" WHERE idParking = ?");

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            int index = 1;
            if (nom != null && !nom.isEmpty()) {
                statement.setString(index++, nom);
            }
            if (adresse != null && !adresse.isEmpty()) {
                statement.setString(index++, adresse);
            }
            if (nombrePlaces != null) {
                statement.setInt(index++, nombrePlaces);
            }
            statement.setInt(index, idParking);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean addParking(String name, String address, int places) throws SQLException {
        String query = "INSERT INTO Parking (nomDuParking, adresseDuParking, nombrePlaces) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, address);
            statement.setInt(3, places);

            return statement.executeUpdate() > 0;
        }
    }


    public boolean deleteParking(int idParking) throws SQLException {
        String query = "DELETE FROM Parking WHERE idParking = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idParking);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean parkingExists(int idParking) throws SQLException {
        String query = "SELECT 1 FROM Parking WHERE idParking = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idParking);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
    public int getParkingCurrentPlacesCount(int idParking) throws SQLException {
        String query = "SELECT count(*) FROM Place WHERE idParking = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idParking);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return -1;
    }

    public int getParkingCapacity(int idParking) throws SQLException {
        String query = "SELECT nombrePlaces FROM Parking WHERE idParking = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idParking);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("nombrePlaces");
                }
            }
        }
        return -1;
    }

    public List<Parking> getAllParkingsWithPlacesCount() throws SQLException {
        String query = """
        SELECT p.idParking, p.nomDuParking, p.adresseDuParking, p.nombrePlaces, 
               COUNT(pl.numero) AS placesDeclarees
        FROM Parking p
        LEFT JOIN Place pl ON p.idParking = pl.idParking
        GROUP BY p.idParking, p.nomDuParking, p.adresseDuParking, p.nombrePlaces
        """;

        List<Parking> parkings = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int idParking = resultSet.getInt("idParking");
                String nom = resultSet.getString("nomDuParking");
                String adresse = resultSet.getString("adresseDuParking");
                int capacite = resultSet.getInt("nombrePlaces");
                int placesDeclarees = resultSet.getInt("placesDeclarees");

                Parking parking = new Parking(idParking, nom, adresse, capacite, placesDeclarees);
                parkings.add(parking);
            }
        }

        return parkings;
    }

    public int getTotalPlacesCount() {
        int totalPlaces = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseHandler.getConnection();
            String query = "SELECT SUM(nombrePlaces) AS total_places FROM Parking";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalPlaces = resultSet.getInt("total_places");
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

        return totalPlaces;
    }
}
