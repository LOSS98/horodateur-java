package fr.insa.horodateurjava.adminControllers;

import fr.insa.horodateurjava.database.dao.AdminDAO;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import fr.insa.horodateurjava.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import fr.insa.horodateurjava.utils.NavigationHelper;
import fr.insa.horodateurjava.utils.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contrôleur pour la gestion de l'authentification des administrateurs.
 * Permet de se connecter à l'application avec des identifiants valides.
 */
public class AdminLoginController {

    @FXML
    private Button loginButton; // Bouton pour soumettre le formulaire de connexion
    @FXML
    private TextField emailField; // Champ de saisie pour l'email de l'administrateur
    @FXML
    private PasswordField passwordField; // Champ de saisie pour le mot de passe de l'administrateur

    private AdminDAO adminDAO; // DAO pour les opérations liées aux administrateurs

    /**
     * Méthode d'initialisation du contrôleur.
     * Initialise la connexion à la base de données et instancie le DAO.
     *
     * @throws SQLException si une erreur survient lors de la connexion à la base de données.
     */
    public void initialize() throws SQLException {
        Connection connection = DatabaseHandler.getConnection();
        adminDAO = new AdminDAO(connection);
    }

    /**
     * Gère la soumission du formulaire de connexion.
     * Valide les informations fournies, vérifie les identifiants, et redirige l'utilisateur
     * vers la page d'accueil administrateur en cas de succès.
     *
     * @param event L'événement déclencheur.
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Vérification des champs requis
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérification des identifiants dans la base de données
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM Administrateur WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Récupérer le mot de passe haché et les informations de l'administrateur
                String hashedPassword = resultSet.getString("motDePasse");

                // Stocker les informations de l'administrateur dans la session
                Session session = Session.getInstance();
                session.setFname(resultSet.getString("prenom"));
                session.setLname(resultSet.getString("nom"));
                session.setUserEmail(resultSet.getString("email"));

                // Vérifier le mot de passe
                if (PasswordUtils.verifyPassword(password, hashedPassword)) {
                    // Rediriger vers la page d'accueil administrateur
                    NavigationHelper.navigateTo((Stage) loginButton.getScene().getWindow(),
                            "/fr/insa/horodateurjava/views/admin/admin-home-view.fxml");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Échec de connexion", "Mot de passe incorrect.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Échec de connexion", "Email introuvable.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Problème lors de la connexion à la base de données.");
        }
    }

    /**
     * Affiche une alerte avec le type, le titre et le contenu spécifiés.
     *
     * @param alertType Le type de l'alerte (INFORMATION, WARNING, ERROR).
     * @param title     Le titre de l'alerte.
     * @param content   Le contenu de l'alerte.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
