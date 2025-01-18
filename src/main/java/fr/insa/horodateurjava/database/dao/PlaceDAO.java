package fr.insa.horodateurjava.database.dao;

import fr.insa.horodateurjava.database.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class PlaceDAO {

    private static final Logger log = LoggerFactory.getLogger(PlaceDAO.class);
    private Connection connection;

    public PlaceDAO(Connection connection) {
        this.connection = connection;
    }

    // Créer la table Place
    public void createTable() throws SQLException {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS Place (
                numero INTEGER PRIMARY KEY,
                disponibilite BOOLEAN NOT NULL,
                enTravaux BOOLEAN NOT NULL,
                etage INTEGER NOT NULL,
                tarifHoraire REAL NOT NULL,
                type TEXT NOT NULL
            );
        """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
        }
    }

    // Ajouter une place
    public void createPlace(Place place) throws SQLException {
        String query = "INSERT INTO Place (numero, disponibilite, enTravaux, etage, tarifHoraire, type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, place.getNumero());
            statement.setBoolean(2, place.isDisponibilite());
            statement.setBoolean(3, place.isEnTravaux());
            statement.setInt(4, place.getEtage());
            statement.setDouble(5, place.getTarifHoraire());
            statement.setString(6, place.getType());
            statement.executeUpdate();
        }
    }

    // Lire toutes les places
    public List<Place> getAllPlaces() throws SQLException {
        List<Place> places = new ArrayList<>();
        String query = "SELECT * FROM Place";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                places.add(mapPlace(resultSet));
            }
        }
        return places;
    }

    // Mapper une place à partir du ResultSet
    private Place mapPlace(ResultSet resultSet) throws SQLException {
        String type = resultSet.getString("type");
        Place place;
        switch (type) {
            case "Classique" -> place = new PlaceClassique();
            case "DeuxRoues" -> place = new PlaceDeuxRoues();
            case "Famille" -> place = new PlaceFamille();
            case "Handicape" -> place = new PlaceHandicape();
            case "RechargeElectrique" -> place = new PlaceRechargeElectrique();
            default -> throw new IllegalArgumentException("Type inconnu : " + type);
        }

        place.setNumero(resultSet.getInt("numero"));
        place.setDisponibilite(resultSet.getBoolean("disponibilite"));
        place.setEnTravaux(resultSet.getBoolean("enTravaux"));
        place.setEtage(resultSet.getInt("etage"));
        place.setTarifHoraire(resultSet.getDouble("tarifHoraire"));
        place.setParking(resultSet.getInt("idParking"));
        place.setType(resultSet.getString("type"));

        return place;
    }

    public Place findAvailablePlace(String vehicleType, String parkingName, String florPlace) throws SQLException {
        String query;
        if (florPlace == null) {
            query = """
        SELECT p.* 
        FROM Place p
        JOIN Parking pk ON p.idParking = pk.idParking
        WHERE pk.nomDuParking = ? 
          AND p.type = ? 
          AND p.disponibilite = true 
          AND p.enTravaux = false
        LIMIT 1
    """;
        } else {
            query = """
        SELECT p.* 
        FROM Place p
        JOIN Parking pk ON p.idParking = pk.idParking
        WHERE pk.nomDuParking = ? 
          AND p.type = ? 
          AND p.disponibilite = true 
          AND p.enTravaux = false
          AND p.etage = ?
        LIMIT 1
    """;
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, parkingName);
            statement.setString(2, vehicleType);

            if (florPlace != null) {
                statement.setString(3, florPlace);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapPlace(resultSet);
                } else if (florPlace != null) {
                    return findAvailablePlace(vehicleType, parkingName, null);
                } else {
                    return null;
                }
            }
        }
    }

    public boolean reservePlace(Place place, Reservation reservation) {
        // Requête pour mettre à jour la place comme étant réservée
        String updatePlaceQuery = "UPDATE Place SET disponibilite = false WHERE numero = ? AND idParking = ?";

        // Requête pour insérer la réservation dans la table Reservation
        String insertReservationQuery = """
    INSERT INTO Reservation (immatriculation, numeroPlace, dateHeureDebut, dateHeureFin, idParking)
    VALUES (?, ?, ?, ?, ?)
    """;

        try (PreparedStatement updatePlaceStmt = connection.prepareStatement(updatePlaceQuery);
             PreparedStatement insertReservationStmt = connection.prepareStatement(insertReservationQuery)) {

            // Marquer la place comme indisponible
            updatePlaceStmt.setInt(1, place.getNumero()); // Numéro de la place
            updatePlaceStmt.setInt(2, place.getIdParking()); // ID du parking
            int rowsUpdated = updatePlaceStmt.executeUpdate();

            if (rowsUpdated == 0) {
                // Aucune ligne mise à jour, la place est déjà réservée ou inexistante
                System.out.println("La place est déjà réservée ou inexistante.");
                return false;
            }

            // Insérer la réservation dans la table Reservation
            insertReservationStmt.setString(1, reservation.getImmatriculation()); // Immatriculation
            insertReservationStmt.setInt(2, reservation.getNumeroPlace()); // Numéro de la place
            insertReservationStmt.setObject(3, reservation.getDateHeureDebut()); // Date et heure de début
            insertReservationStmt.setObject(4, reservation.getDateHeureFin()); // Date et heure de fin
            insertReservationStmt.setInt(5, place.getIdParking()); // ID du parking (lié à la place)
            insertReservationStmt.executeUpdate();

            System.out.println("Réservation effectuée avec succès !");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
