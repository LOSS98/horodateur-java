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

public class AdminLoginController {

    public Button loginButton;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private AdminDAO adminDAO;

    public void initialize() throws SQLException {
        Connection connection = DatabaseHandler.getConnection();
        adminDAO = new AdminDAO(connection);
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM Administrateur WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("motDePasse");
                Session session = Session.getInstance();
                session.setFname(resultSet.getString("prenom"));
                session.setLname(resultSet.getString("nom"));
                session.setUserEmail(resultSet.getString("email"));
                if (PasswordUtils.verifyPassword(password, hashedPassword)) {
                    NavigationHelper.navigateTo((Stage) loginButton.getScene().getWindow(), "/fr/insa/horodateurjava/views/admin/admin-home-view.fxml");
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


    private void redirectToAdminPage() {
        System.out.println("Redirection vers la page administrateur...");
        // Logic to load admin page
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.show();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
