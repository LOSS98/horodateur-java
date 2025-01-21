package fr.insa.horodateurjava.database.dao;

import fr.insa.horodateurjava.models.Parking;
import fr.insa.horodateurjava.utils.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour gérer les opérations sur les parkings dans la base de données.
 */
public class ParkingDAO {
    private final Connection connection;

    /**
     * Constructeur de la classe ParkingDAO.
     *
     * @param connection Connexion à la base de données.
     */
    public ParkingDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Récupère la liste des noms de tous les parkings.
     *
     * @return Liste des noms de parkings.
     * @throws SQLException En cas d'erreur de requête.
     */
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

    /**
     * Récupère les noms et les IDs des parkings.
     *
     * @return Liste des parkings sous le format "ID - Nom".
     * @throws SQLException En cas d'erreur de requête.
     */
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

    /**
     * Met à jour les informations d'un parking.
     *
     * @param idParking    ID du parking.
     * @param nom          Nom du parking (peut être null).
     * @param adresse      Adresse du parking (peut être null).
     * @param nombrePlaces Nombre de places (peut être null).
     * @return true si la mise à jour a été effectuée, false sinon.
     * @throws SQLException En cas d'erreur de requête.
     */
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

    /**
     * Ajoute un nouveau parking.
     *
     * @param name   Nom du parking.
     * @param address Adresse du parking.
     * @param places  Nombre de places disponibles.
     * @return true si le parking a été ajouté avec succès, false sinon.
     * @throws SQLException En cas d'erreur de requête.
     */
    public boolean addParking(String name, String address, int places) throws SQLException {
        String query = "INSERT INTO Parking (nomDuParking, adresseDuParking, nombrePlaces) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, address);
            statement.setInt(3, places);

            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Supprime un parking en fonction de son ID.
     *
     * @param idParking ID du parking.
     * @return true si le parking a été supprimé, false sinon.
     * @throws SQLException En cas d'erreur de requête.
     */
    public boolean deleteParking(int idParking) throws SQLException {
        String query = "DELETE FROM Parking WHERE idParking = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idParking);
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Vérifie si un parking existe en fonction de son ID.
     *
     * @param idParking ID du parking.
     * @return true si le parking existe, false sinon.
     * @throws SQLException En cas d'erreur de requête.
     */
    public boolean parkingExists(int idParking) throws SQLException {
        String query = "SELECT 1 FROM Parking WHERE idParking = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idParking);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * Récupère le nombre actuel de places dans un parking.
     *
     * @param idParking ID du parking.
     * @return Nombre de places occupées.
     * @throws SQLException En cas d'erreur de requête.
     */
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

    /**
     * Récupère la capacité totale d'un parking.
     *
     * @param idParking ID du parking.
     * @return Capacité totale.
     * @throws SQLException En cas d'erreur de requête.
     */
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

    /**
     * Récupère la liste des parkings avec leur nombre actuel de places.
     *
     * @return Liste des parkings avec leurs détails.
     * @throws SQLException En cas d'erreur de requête.
     */
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

    /**
     * Récupère le nombre total de places dans tous les parkings.
     *
     * @return Nombre total de places.
     */
    public int getTotalPlacesCount() {
        int totalPlaces = 0;

        String query = "SELECT SUM(nombrePlaces) AS total_places FROM Parking";

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                totalPlaces = resultSet.getInt("total_places");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalPlaces;
    }
}
