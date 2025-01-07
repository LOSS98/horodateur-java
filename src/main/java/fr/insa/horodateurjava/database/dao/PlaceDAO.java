package fr.insa.horodateurjava.database.dao;

import fr.insa.horodateurjava.database.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaceDAO {

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

        return place;
    }
}
