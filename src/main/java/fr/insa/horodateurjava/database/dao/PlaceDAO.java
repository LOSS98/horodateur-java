package fr.insa.horodateurjava.database.dao;

import fr.insa.horodateurjava.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaceDAO {

    private static final Logger log = LoggerFactory.getLogger(PlaceDAO.class);
    private Connection connection;

    public PlaceDAO(Connection connection) {
        this.connection = connection;
        String query = """
        UPDATE Place
         SET disponibilite = true
         WHERE (numero, idParking) NOT IN (
             SELECT numeroPlace, idParking
             FROM Reservation
             WHERE dateHeureFin > datetime('now')
         )
    """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int rowsUpdated = statement.executeUpdate();
            System.out.println(rowsUpdated + " places mises à jour pour être disponibles.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public void addPlace(Place place) throws SQLException {
        String sql = "INSERT INTO Place (numero, etage, type, tarifHoraire, puissanceCharge, enTravaux, idParking, disponibilite) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, true)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, place.getNumero());
            stmt.setInt(2, place.getEtage());
            stmt.setString(3, place.getType());
            stmt.setDouble(4, place.getTarifHoraire());

            // Gestion de la puissance de charge (uniquement pour RechargeElectrique)
            if (place instanceof PlaceRechargeElectrique) {
                stmt.setDouble(5, ((PlaceRechargeElectrique) place).getPuissanceCharge());
            } else {
                stmt.setNull(5, Types.DOUBLE);
            }

            stmt.setBoolean(6, place.isEnTravaux());
            stmt.setInt(7, place.getIdParking());

            stmt.executeUpdate();
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

        // Récupération des données communes
        int numero = resultSet.getInt("numero");
        int etage = resultSet.getInt("etage");
        float tarifHoraire = resultSet.getFloat("tarifHoraire");
        boolean enTravaux = resultSet.getBoolean("enTravaux");
        int parkingId = resultSet.getInt("idParking");

        // Gestion de la puissance de charge (spécifique à RechargeElectrique)
        Float puissanceCharge = null;
        if (type.equals("RechargeElectrique")) {
            puissanceCharge = resultSet.getFloat("puissanceCharge");
            if (resultSet.wasNull()) { // Vérifie si la valeur est NULL dans la base de données
                puissanceCharge = null;
            }
        }

        switch (type) {
            case "Classique" -> place = new PlaceClassique(numero, etage, tarifHoraire, enTravaux, parkingId);
            case "DeuxRoues" -> place = new PlaceDeuxRoues(numero, etage, tarifHoraire, enTravaux, parkingId);
            case "Famille" -> place = new PlaceFamille(numero, etage, tarifHoraire, enTravaux, parkingId);
            case "Handicape" -> place = new PlaceHandicape(numero, etage, tarifHoraire, enTravaux, parkingId);
            case "RechargeElectrique" -> {
                if (puissanceCharge == null || puissanceCharge <= 0) {
                    throw new IllegalArgumentException("Puissance de charge invalide pour RechargeElectrique.");
                }
                place = new PlaceRechargeElectrique(numero, etage, tarifHoraire, puissanceCharge, enTravaux, parkingId);
            }
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
    INSERT INTO Reservation (immatriculation, numeroPlace, dateHeureDebut, dateHeureFin, idParking, prix)
    VALUES (?, ?, ?, ?, ?, ?)
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

            // Formatter pour le format "yyyy-MM-dd'T'HH:mm:ss"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            // Formater les dates avant de les insérer
            String formattedStartDate = reservation.getDateHeureDebut().format(formatter);
            String formattedEndDate = reservation.getDateHeureFin().format(formatter);

            // Insérer la réservation dans la table Reservation
            insertReservationStmt.setString(1, reservation.getImmatriculation()); // Immatriculation
            insertReservationStmt.setInt(2, reservation.getNumeroPlace()); // Numéro de la place
            insertReservationStmt.setString(3, formattedStartDate); // Date et heure de début formatées
            insertReservationStmt.setString(4, formattedEndDate);   // Date et heure de fin formatées
            insertReservationStmt.setInt(5, place.getIdParking()); // ID du parking (lié à la place)
            insertReservationStmt.setDouble(6, reservation.getPrix()); // Prix
            insertReservationStmt.executeUpdate();

            System.out.println("Réservation effectuée avec succès !");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsByNumeroAndIdParking(int numero, int idParking) throws SQLException {
        String query = "SELECT COUNT(*) FROM Place WHERE numero = ? AND idParking = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, numero);
            statement.setInt(2, idParking);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Retourne true si au moins une correspondance est trouvée
                }
            }
        }
        return false; // Retourne false si aucune correspondance
    }

    public void deletePlaceByNumeroAndIdParking(int numero, int idParking) throws SQLException {
        String query = "DELETE FROM Place WHERE numero = ? AND idParking = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, numero);
            statement.setInt(2, idParking);
            statement.executeUpdate();
        }
    }

    public void updatePlace(int parkingId, int numero, Integer etage, String type, Double tarifHoraire, Double puissanceCharge, Boolean enTravaux) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE Place SET ");
        boolean firstField = true;

        if (etage != null) {
            query.append("etage = ? ");
            firstField = false;
        }
        if (type != null) {
            if (!firstField) query.append(", ");
            query.append("type = ? ");
            firstField = false;
        }
        if (tarifHoraire != null) {
            if (!firstField) query.append(", ");
            query.append("tarifHoraire = ? ");
            firstField = false;
        }
        if (puissanceCharge != null) {
            if (!firstField) query.append(", ");
            query.append("puissanceCharge = ? ");
            firstField = false;
        }
        if (enTravaux != null) {
            if (!firstField) query.append(", ");
            query.append("enTravaux = ? ");
        }

        query.append("WHERE idParking = ? AND numero = ?");

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            int index = 1;

            if (etage != null) statement.setInt(index++, etage);
            if (type != null) statement.setString(index++, type);
            if (tarifHoraire != null) statement.setDouble(index++, tarifHoraire);
            if (puissanceCharge != null) statement.setDouble(index++, puissanceCharge);
            if (enTravaux != null) statement.setBoolean(index++, enTravaux);

            statement.setInt(index++, parkingId);
            statement.setInt(index, numero);

            statement.executeUpdate();
        }
    }

    public List<Place> getPlacesByParking(int parkingId) throws SQLException {
        String query = "SELECT * FROM Place WHERE idParking = ?";
        List<Place> places = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, parkingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int numero = resultSet.getInt("numero");
                    int etage = resultSet.getInt("etage");
                    String type = resultSet.getString("type");
                    boolean disponibilite = resultSet.getBoolean("disponibilite");
                    double tarifHoraire = resultSet.getDouble("tarifHoraire");
                    double puissanceCharge = resultSet.getDouble("puissanceCharge");
                    boolean enTravaux = resultSet.getBoolean("enTravaux");
                    int idParking = resultSet.getInt("idParking");

                    Place place;

                    // Déterminer la sous-classe de Place à utiliser
                    switch (type) {
                        case "Classique":
                            place = new PlaceClassique(numero, etage, tarifHoraire, enTravaux, idParking);
                            place.setDisponibilite(disponibilite);
                            break;
                        case "RechargeElectrique":
                            place = new PlaceRechargeElectrique(numero, etage, tarifHoraire, puissanceCharge, enTravaux, idParking);
                            place.setDisponibilite(disponibilite);
                            break;
                        case "DeuxRoues":
                            place = new PlaceDeuxRoues(numero, etage, tarifHoraire, enTravaux, idParking);
                            place.setDisponibilite(disponibilite);
                            break;
                        case "Famille":
                            place = new PlaceFamille(numero, etage, tarifHoraire, enTravaux, idParking);
                            place.setDisponibilite(disponibilite);
                            break;
                        case "Handicape":
                            place = new PlaceHandicape(numero, etage, tarifHoraire, enTravaux, idParking);
                            place.setDisponibilite(disponibilite);
                            break;
                        default:
                            throw new IllegalArgumentException("Type de place inconnu : " + type);
                    }

                    places.add(place);
                }
            }
        }
        return places;
    }








}
