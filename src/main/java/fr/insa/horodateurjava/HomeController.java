package fr.insa.horodateurjava;

import fr.insa.horodateurjava.database.dao.ParkingDAO;
import fr.insa.horodateurjava.database.dao.PlaceDAO;
import fr.insa.horodateurjava.models.Place;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import fr.insa.horodateurjava.utils.NavigationHelper;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrôleur principal pour la page d'accueil de l'application.
 * Permet à l'utilisateur de sélectionner un type de véhicule et un parking
 * pour rechercher une place disponible ou accéder à l'espace administrateur.
 */
public class HomeController {

    @FXML
    private ChoiceBox<String> vehicleTypeChoiceBox;

    @FXML
    private ChoiceBox<String> parkingNameChoiceBox;

    /**
     * Méthode d'initialisation appelée automatiquement après le chargement de la vue FXML.
     * Configure les choix de types de véhicules et charge les noms des parkings depuis la base de données.
     */
    @FXML
    public void initialize() {
        // Charger les types de véhicules dans le ChoiceBox
        if (vehicleTypeChoiceBox != null) {
            vehicleTypeChoiceBox.getItems().addAll("Classique", "DeuxRoues", "Famille", "Handicape", "RechargeElectrique");
        }

        // Charger les noms des parkings dans le ChoiceBox
        if (parkingNameChoiceBox != null) {
            loadParkingNames();
        }
    }

    /**
     * Charge les noms des parkings depuis la base de données et les affiche dans le ChoiceBox.
     */
    private void loadParkingNames() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            ParkingDAO parkingDAO = new ParkingDAO(connection);

            List<String> parkingNames = parkingDAO.getAllParkingNames();
            parkingNameChoiceBox.getItems().clear();
            parkingNameChoiceBox.getItems().addAll(parkingNames);
        } catch (Exception e) {
            // Afficher une alerte en cas d'erreur de connexion
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Problème de connexion à la base de données");
            alert.setContentText("Impossible de charger les parkings.");
            alert.showAndWait();
        }
    }

    /**
     * Action déclenchée lors de la recherche d'une place.
     * Vérifie si une place est disponible pour le type de véhicule et le parking sélectionnés.
     *
     * @param event Événement déclenché par le clic sur le bouton de recherche.
     */
    @FXML
    private void onSearchPlace(ActionEvent event) {
        String selectedVehicleType = vehicleTypeChoiceBox.getValue();
        String selectedParkingName = parkingNameChoiceBox.getValue();

        // Vérification des sélections
        if (selectedVehicleType == null || selectedParkingName == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez sélectionner un type de véhicule et un parking.");
            alert.showAndWait();
            return;
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            PlaceDAO placeDAO = new PlaceDAO(connection);

            // Rechercher une place disponible
            Place availablePlace = placeDAO.findAvailablePlace(selectedVehicleType, selectedParkingName, null);

            if (availablePlace != null) {
                // Naviguer vers la vue de réservation
                Stage stage = (Stage) vehicleTypeChoiceBox.getScene().getWindow();
                NavigationHelper.navigateToReservation(stage, selectedVehicleType, selectedParkingName);
            } else {
                // Naviguer vers une vue indiquant qu'aucune place n'est disponible
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/noplace-view.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Problème de connexion à la base de données");
            alert.setContentText("Erreur lors de la recherche de place.");
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Action déclenchée lors de l'accès à l'espace administrateur.
     * Charge la vue de connexion pour l'administration.
     *
     * @param actionEvent Événement déclenché par le clic sur le bouton d'accès admin.
     */
    public void onAdminAccess(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/admin-login-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Problème de navigation");
            alert.setContentText("Impossible de charger la page de connexion.");
            alert.showAndWait();
        }
    }
}
