package fr.insa.horodateurjava.database.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingDAO {
    private Connection connection;

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
}