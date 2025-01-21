package fr.insa.horodateurjava.database.dao;

import fr.insa.horodateurjava.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) pour gérer les entités "Place" dans la base de données.
 */
public class PlaceDAO {

    private static final Logger log = LoggerFactory.getLogger(PlaceDAO.class);
    private Connection connection;

    /**
     * Constructeur du DAO. Initialise une connexion et met à jour les disponibilités des places.
     *
     * @param connection La connexion à la base de données.
     */
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

    /**
     * Crée la table "Place" si elle n'existe pas déjà.
     *
     * @throws SQLException En cas d'erreur SQL.
     */
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

    /**
     * Ajoute une nouvelle place dans la base de données.
     *
     * @param place L'objet Place à ajouter.
     * @throws SQLException En cas d'erreur SQL.
     */
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

    /**
     * Récupère toutes les places disponibles dans la base de données.
     *
     * @return Une liste d'objets Place.
     * @throws SQLException En cas d'erreur SQL.
     */
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

    /**
     * Mapper un résultat SQL vers un objet Place.
     *
     * @param resultSet Le résultat de la requête.
     * @return Un objet Place correspondant aux données.
     * @throws SQLException En cas d'erreur SQL.
     */
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

        // Créer une instance selon le type
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

        return place;
    }

    /**
     * Trouve une place disponible selon les critères fournis.
     *
     * @param vehicleType Le type de véhicule (Classique, DeuxRoues, etc.).
     * @param parkingName Le nom du parking.
     * @param florPlace   (Optionnel) Étage souhaité.
     * @return Une instance Place si disponible, sinon null.
     * @throws SQLException En cas d'erreur SQL.
     */
    public Place findAvailablePlace(String vehicleType, String parkingName, String florPlace) throws SQLException {
        // Construction de la requête en fonction de l'étage
        String query = florPlace == null ?
                "SELECT p.* FROM Place p JOIN Parking pk ON p.idParking = pk.idParking WHERE pk.nomDuParking = ? AND p.type = ? AND p.disponibilite = true AND p.enTravaux = false LIMIT 1" :
                "SELECT p.* FROM Place p JOIN Parking pk ON p.idParking = pk.idParking WHERE pk.nomDuParking = ? AND p.type = ? AND p.disponibilite = true AND p.enTravaux = false AND p.etage = ? LIMIT 1";

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

    /**
     * Réserve une place et enregistre la réservation en base de données.
     *
     * @param place       La place à réserver.
     * @param reservation Les détails de la réservation.
     * @return true si la réservation a réussi, sinon false.
     */
    public boolean reservePlace(Place place, Reservation reservation) {
        String updatePlaceQuery = "UPDATE Place SET disponibilite = false WHERE numero = ? AND idParking = ?";
        String insertReservationQuery = "INSERT INTO Reservation (immatriculation, numeroPlace, dateHeureDebut, dateHeureFin, idParking, prix) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement updatePlaceStmt = connection.prepareStatement(updatePlaceQuery);
             PreparedStatement insertReservationStmt = connection.prepareStatement(insertReservationQuery)) {

            updatePlaceStmt.setInt(1, place.getNumero());
            updatePlaceStmt.setInt(2, place.getIdParking());
            int rowsUpdated = updatePlaceStmt.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("La place est déjà réservée ou inexistante.");
                return false;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            insertReservationStmt.setString(1, reservation.getImmatriculation());
            insertReservationStmt.setInt(2, reservation.getNumeroPlace());
            insertReservationStmt.setString(3, reservation.getDateHeureDebut().format(formatter));
            insertReservationStmt.setString(4, reservation.getDateHeureFin().format(formatter));
            insertReservationStmt.setInt(5, place.getIdParking());
            insertReservationStmt.setDouble(6, reservation.getPrix());
            insertReservationStmt.executeUpdate();

            System.out.println("Réservation effectuée avec succès !");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vérifie si une place existe dans un parking donné.
     *
     * @param numero    Numéro de la place.
     * @param idParking Identifiant du parking.
     * @return true si la place existe, sinon false.
     * @throws SQLException En cas d'erreur SQL.
     */
    public boolean existsByNumeroAndIdParking(int numero, int idParking) throws SQLException {
        String query = "SELECT COUNT(*) FROM Place WHERE numero = ? AND idParking = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, numero);
            statement.setInt(2, idParking);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }

    /**
     * Supprime une place d'un parking spécifique.
     *
     * @param numero    Numéro de la place.
     * @param idParking Identifiant du parking.
     * @throws SQLException En cas d'erreur SQL.
     */
    public void deletePlaceByNumeroAndIdParking(int numero, int idParking) throws SQLException {
        String query = "DELETE FROM Place WHERE numero = ? AND idParking = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, numero);
            statement.setInt(2, idParking);
            statement.executeUpdate();
        }
    }

    /**
     * Met à jour les informations d'une place dans un parking spécifique.
     *
     * @param parkingId       Identifiant du parking.
     * @param numero          Numéro de la place.
     * @param etage           (Optionnel) Nouvel étage.
     * @param type            (Optionnel) Nouveau type.
     * @param tarifHoraire    (Optionnel) Nouveau tarif horaire.
     * @param puissanceCharge (Optionnel) Nouvelle puissance de charge.
     * @param enTravaux       (Optionnel) Nouvel état de travaux.
     * @throws SQLException En cas d'erreur SQL.
     */
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

    /**
     * Récupère toutes les places d'un parking spécifique.
     *
     * @param parkingId Identifiant du parking.
     * @return Une liste d'objets Place.
     * @throws SQLException En cas d'erreur SQL.
     */
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
