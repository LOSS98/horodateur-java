package fr.insa.horodateurjava.database.dao;

import fr.insa.horodateurjava.models.Administrateur;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import fr.insa.horodateurjava.utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private Connection connection;

    public AdminDAO(Connection connection) throws SQLException {
        this.connection = DatabaseHandler.getConnection();    }

    // Login method
    public Administrateur login(String email, String password) {
        try {
            String query = "SELECT * FROM Administrateur WHERE email = ? AND motDePasse = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Administrateur(
                        rs.getInt("idAdmin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean addAdmin(Administrateur admin) throws SQLException {
        String query = "INSERT INTO Administrateur (nom, prenom, email, motDePasse) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, admin.getNom());
            statement.setString(2, admin.getPrenom());
            statement.setString(3, admin.getEmail());
            statement.setString(4, PasswordUtils.hashPassword(admin.getMotDePasse())); // Hacher le mot de passe ici

            return statement.executeUpdate() > 0;
        }
    }


    public boolean updateAdmin(String email, String lastName, String firstName, String password) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE Administrateur SET ");
        boolean hasField = false;

        if (lastName != null && !lastName.isEmpty()) {
            query.append("lastName = ?");
            hasField = true;
        }
        if (firstName != null && !firstName.isEmpty()) {
            if (hasField) query.append(", ");
            query.append("firstName = ?");
            hasField = true;
        }
        if (password != null && !password.isEmpty()) {
            if (hasField) query.append(", ");
            query.append("motDePasse = ?");
            hasField = true;
        }

        if (!hasField) {
            throw new IllegalArgumentException("Aucun champ à mettre à jour.");
        }

        query.append(" WHERE email = ?");

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            int index = 1;
            if (lastName != null && !lastName.isEmpty()) {
                statement.setString(index++, lastName);
            }
            if (firstName != null && !firstName.isEmpty()) {
                statement.setString(index++, firstName);
            }
            if (password != null && !password.isEmpty()) {
                statement.setString(index++, PasswordUtils.hashPassword(password));
            }
            statement.setString(index, email);

            return statement.executeUpdate() > 0;
        }
    }


    public boolean deleteAdmin(String email) throws SQLException {
        String query = "DELETE FROM Administrateur WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT 1 FROM Administrateur WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public List<Administrateur> getAllAdmins() throws SQLException {
        String query = "SELECT idAdmin, nom, prenom, email FROM Administrateur";
        List<Administrateur> admins = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("idAdmin");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String email = resultSet.getString("email");

                admins.add(new Administrateur(id, nom, prenom, email));
            }
        }
        return admins;
    }

}
