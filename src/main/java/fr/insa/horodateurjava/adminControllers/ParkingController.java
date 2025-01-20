package fr.insa.horodateurjava.adminControllers;

import fr.insa.horodateurjava.database.dao.ParkingDAO;
import fr.insa.horodateurjava.utils.DatabaseHandler;
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

public class ParkingController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField placesField;

    /**
     * Méthode pour ajouter un nouveau parking
     */
    @FXML
    private void handleAddParking() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String placesText = placesField.getText().trim();

        if (name.isEmpty() || address.isEmpty() || placesText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
            return;
        }

        int places;
        try {
            places = Integer.parseInt(placesText);
            if (places <= 0) {
                showAlert(Alert.AlertType.WARNING, "Nombre de places invalide", "Le nombre de places doit être un entier positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un nombre valide pour les places.");
            return;
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            ParkingDAO parkingDAO = new ParkingDAO(connection);
            parkingDAO.addParking(name, address, places); // Utilise les méthodes DAO
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le parking a été ajouté avec succès !");
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du parking.");
        }
    }

    /**
     * Méthode pour modifier un parking existant
     */
    @FXML
    private void handleUpdateParking() {
        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String placesText = placesField.getText().trim();

        if (idText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez fournir l'ID du parking à modifier.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID doit être un entier valide.");
            return;
        }

        Integer places = null;
        if (!placesText.isEmpty()) {
            try {
                places = Integer.parseInt(placesText);
                if (places <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Nombre de places invalide", "Le nombre de places doit être un entier positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un nombre valide pour les places.");
                return;
            }
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            ParkingDAO parkingDAO = new ParkingDAO(connection);
            if (!parkingDAO.updateParking(id, name, address, places)) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Aucun parking trouvé avec cet ID.");
                return;
            }
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le parking a été mis à jour avec succès !");
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la mise à jour du parking.");
        }
    }

    /**
     * Méthode pour supprimer un parking
     */
    @FXML
    private void handleDeleteParking() {
        String idText = idField.getText().trim();

        if (idText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez fournir l'ID du parking à supprimer.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'ID doit être un entier valide.");
            return;
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            ParkingDAO parkingDAO = new ParkingDAO(connection);
            if (!parkingDAO.deleteParking(id)) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Aucun parking trouvé avec cet ID.");
                return;
            }
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le parking a été supprimé avec succès !");
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la suppression du parking.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        addressField.clear();
        placesField.clear();
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
