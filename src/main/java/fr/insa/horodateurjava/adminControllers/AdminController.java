package fr.insa.horodateurjava.adminControllers;

import fr.insa.horodateurjava.models.Administrateur;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import fr.insa.horodateurjava.database.dao.AdminDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

/**
 * Contrôleur pour gérer les opérations liées aux administrateurs :
 * ajout, modification, suppression et navigation.
 */
public class AdminController {

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passField;

    @FXML
    private TextField CpassField;

    /**
     * Gère l'ajout d'un nouvel administrateur.
     * Vérifie les champs obligatoires, la correspondance des mots de passe et l'unicité de l'email avant insertion.
     *
     * @param event Action déclenchée par un clic sur le bouton "Ajouter".
     */
    @FXML
    private void handleAddAdmin(ActionEvent event) {
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passField.getText().trim();
        String confirmPassword = CpassField.getText().trim();

        // Vérifier si tous les champs sont remplis
        if (lastName.isEmpty() || firstName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier si les mots de passe correspondent
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Erreur de mot de passe", "Les mots de passe ne correspondent pas !");
            return;
        }

        // Gestion de l'ajout en base de données
        try (Connection connection = DatabaseHandler.getConnection()) {
            AdminDAO adminDAO = new AdminDAO(connection);

            // Vérifier si l'email est déjà utilisé
            if (adminDAO.emailExists(email)) {
                showAlert(Alert.AlertType.WARNING, "Email existant", "Un administrateur avec cet email existe déjà !");
                return;
            }

            // Création d'un administrateur et tentative d'ajout
            Administrateur admin = new Administrateur(lastName, firstName, email, password);
            if (adminDAO.addAdmin(admin)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "L'administrateur a été ajouté avec succès !");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter l'administrateur.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout de l'administrateur.");
        }
    }

    /**
     * Gère la mise à jour des informations d'un administrateur existant.
     *
     * @param event Action déclenchée par un clic sur le bouton "Modifier".
     */
    @FXML
    private void handleUpdateAdmin(ActionEvent event) {
        String email = emailField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String password = passField.getText().trim();
        String confirmPassword = CpassField.getText().trim();

        // Vérifier si l'email est fourni
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquant", "Veuillez entrer l'email de l'administrateur à modifier !");
            return;
        }

        // Vérifier la correspondance des mots de passe si fournis
        if (!password.isEmpty() || !confirmPassword.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                showAlert(Alert.AlertType.WARNING, "Erreur de mot de passe", "Les mots de passe ne correspondent pas !");
                return;
            }
        }

        // Gestion de la mise à jour en base de données
        try (Connection connection = DatabaseHandler.getConnection()) {
            AdminDAO adminDAO = new AdminDAO(connection);

            // Vérifier si l'administrateur existe
            if (!adminDAO.emailExists(email)) {
                showAlert(Alert.AlertType.WARNING, "Email introuvable", "Aucun administrateur trouvé avec cet email !");
                return;
            }

            // Mise à jour de l'administrateur
            if (adminDAO.updateAdmin(email, lastName, firstName, password)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "L'administrateur a été modifié avec succès !");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la modification de l'administrateur.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la modification de l'administrateur.");
        }
    }

    /**
     * Gère la suppression d'un administrateur en fonction de son email.
     *
     * @param event Action déclenchée par un clic sur le bouton "Supprimer".
     */
    @FXML
    private void handleDeleteAdmin(ActionEvent event) {
        String email = emailField.getText().trim();

        // Vérifier si l'email est fourni
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquant", "Veuillez entrer l'email de l'administrateur à supprimer !");
            return;
        }

        // Gestion de la suppression en base de données
        try (Connection connection = DatabaseHandler.getConnection()) {
            AdminDAO adminDAO = new AdminDAO(connection);

            // Vérifier si l'administrateur existe
            if (!adminDAO.emailExists(email)) {
                showAlert(Alert.AlertType.WARNING, "Email introuvable", "Aucun administrateur trouvé avec cet email !");
                return;
            }

            // Suppression de l'administrateur
            if (adminDAO.deleteAdmin(email)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "L'administrateur a été supprimé avec succès !");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la suppression de l'administrateur.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la suppression de l'administrateur.");
        }
    }

    /**
     * Affiche une alerte avec le type, le titre et le message fournis.
     *
     * @param alertType Type de l'alerte (INFORMATION, WARNING, ERROR).
     * @param title     Titre de l'alerte.
     * @param message   Message de l'alerte.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Réinitialise tous les champs de saisie.
     */
    private void clearFields() {
        lastNameField.clear();
        firstNameField.clear();
        emailField.clear();
        passField.clear();
        CpassField.clear();
    }

    /**
     * Retourne à la vue d'accueil pour les administrateurs.
     *
     * @param actionEvent Action déclenchée par un clic sur le lien "Retour".
     */
    public void handleBackLinkAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
