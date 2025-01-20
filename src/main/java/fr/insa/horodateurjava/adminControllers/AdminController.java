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

    @FXML
    private void handleAddAdmin(ActionEvent event) {
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passField.getText().trim();
        String confirmPassword = CpassField.getText().trim();

        if (lastName.isEmpty() || firstName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Erreur de mot de passe", "Les mots de passe ne correspondent pas !");
            return;
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            AdminDAO adminDAO = new AdminDAO(connection);

            // Vérifier si l'email existe déjà
            if (adminDAO.emailExists(email)) {
                showAlert(Alert.AlertType.WARNING, "Email existant", "Un administrateur avec cet email existe déjà !");
                return;
            }

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

    @FXML
    private void handleUpdateAdmin(ActionEvent event) {
        String email = emailField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String password = passField.getText().trim();
        String confirmPassword = CpassField.getText().trim();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquant", "Veuillez entrer l'email de l'administrateur à modifier !");
            return;
        }

        // Vérifier si les mots de passe sont fournis et qu'ils correspondent
        if (!password.isEmpty() || !confirmPassword.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                showAlert(Alert.AlertType.WARNING, "Erreur de mot de passe", "Les mots de passe ne correspondent pas !");
                return;
            }
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            AdminDAO adminDAO = new AdminDAO(connection);

            // Vérifier si l'email existe dans la base avant modification
            if (!adminDAO.emailExists(email)) {
                showAlert(Alert.AlertType.WARNING, "Email introuvable", "Aucun administrateur trouvé avec cet email !");
                return;
            }

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


    @FXML
    private void handleDeleteAdmin(ActionEvent event) {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquant", "Veuillez entrer l'email de l'administrateur à supprimer !");
            return;
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            AdminDAO adminDAO = new AdminDAO(connection);

            // Vérifier si l'email existe dans la base avant suppression
            if (!adminDAO.emailExists(email)) {
                showAlert(Alert.AlertType.WARNING, "Email introuvable", "Aucun administrateur trouvé avec cet email !");
                return;
            }

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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        lastNameField.clear();
        firstNameField.clear();
        emailField.clear();
        passField.clear();
        CpassField.clear();
    }

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
