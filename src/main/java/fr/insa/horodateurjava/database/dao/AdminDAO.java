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

/**
 * Data Access Object (DAO) pour gérer les opérations sur la table `Administrateur`.
 */
public class AdminDAO {

    private final Connection connection;

    /**
     * Constructeur pour initialiser la connexion à la base de données.
     *
     * @param connection La connexion à la base de données.
     * @throws SQLException Si une erreur de connexion se produit.
     */
    public AdminDAO(Connection connection) throws SQLException {
        this.connection = DatabaseHandler.getConnection();
    }

    /**
     * Méthode pour connecter un administrateur.
     *
     * @param email    L'email de l'administrateur.
     * @param password Le mot de passe de l'administrateur.
     * @return Un objet `Administrateur` si les informations de connexion sont valides, sinon null.
     */
    public Administrateur login(String email, String password) {
        String query = "SELECT * FROM Administrateur WHERE email = ? AND motDePasse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Administrateur(
                            rs.getInt("idAdmin"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("motDePasse")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ajoute un nouvel administrateur.
     *
     * @param admin L'objet `Administrateur` contenant les informations de l'administrateur.
     * @return true si l'opération est réussie, sinon false.
     * @throws SQLException Si une erreur SQL se produit.
     */
    public boolean addAdmin(Administrateur admin) throws SQLException {
        String query = "INSERT INTO Administrateur (nom, prenom, email, motDePasse) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, admin.getNom());
            statement.setString(2, admin.getPrenom());
            statement.setString(3, admin.getEmail());
            statement.setString(4, PasswordUtils.hashPassword(admin.getMotDePasse())); // Hacher le mot de passe
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Met à jour les informations d'un administrateur existant.
     *
     * @param email    L'email de l'administrateur à mettre à jour.
     * @param lastName Le nouveau nom de l'administrateur.
     * @param firstName Le nouveau prénom de l'administrateur.
     * @param password Le nouveau mot de passe de l'administrateur.
     * @return true si la mise à jour est réussie, sinon false.
     * @throws SQLException Si une erreur SQL se produit.
     */
    public boolean updateAdmin(String email, String lastName, String firstName, String password) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE Administrateur SET ");
        boolean hasField = false;

        if (lastName != null && !lastName.isEmpty()) {
            query.append("nom = ?");
            hasField = true;
        }
        if (firstName != null && !firstName.isEmpty()) {
            if (hasField) query.append(", ");
            query.append("prenom = ?");
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

    /**
     * Supprime un administrateur de la base de données.
     *
     * @param email L'email de l'administrateur à supprimer.
     * @return true si la suppression est réussie, sinon false.
     * @throws SQLException Si une erreur SQL se produit.
     */
    public boolean deleteAdmin(String email) throws SQLException {
        String query = "DELETE FROM Administrateur WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Vérifie si un email existe déjà dans la base de données.
     *
     * @param email L'email à vérifier.
     * @return true si l'email existe, sinon false.
     * @throws SQLException Si une erreur SQL se produit.
     */
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT 1 FROM Administrateur WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * Récupère tous les administrateurs de la base de données.
     *
     * @return Une liste d'objets `Administrateur`.
     * @throws SQLException Si une erreur SQL se produit.
     */
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
