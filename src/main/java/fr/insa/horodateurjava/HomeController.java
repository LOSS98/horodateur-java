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

public class HomeController {
    @FXML
    private ChoiceBox<String> vehicleTypeChoiceBox;

    @FXML
    private ChoiceBox<String> parkingNameChoiceBox;

    @FXML
    public void initialize() {
        if (vehicleTypeChoiceBox != null) {
            vehicleTypeChoiceBox.getItems().addAll("Classique", "DeuxRoues", "Famille", "Handicape", "RechargeElectrique");
        }

        if (parkingNameChoiceBox != null) {
            loadParkingNames();
        }
    }

    private void loadParkingNames() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            ParkingDAO parkingDAO = new ParkingDAO(connection);

            List<String> parkingNames = parkingDAO.getAllParkingNames();


            parkingNameChoiceBox.getItems().clear();
            parkingNameChoiceBox.getItems().addAll(parkingNames);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Problème de connexion à la base de données");
            alert.setContentText("Impossible de charger les parkings.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onSearchPlace(ActionEvent event) {
        String selectedVehicleType = vehicleTypeChoiceBox.getValue();
        String selectedParkingName = parkingNameChoiceBox.getValue();

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
            Place availablePlace = placeDAO.findAvailablePlace(selectedVehicleType, selectedParkingName, null);

            if (availablePlace != null) {
                Stage stage = (Stage) vehicleTypeChoiceBox.getScene().getWindow();
                NavigationHelper.navigateToReservation(stage, selectedVehicleType, selectedParkingName);
            } else {
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
            alert.setContentText("Impossible de charger la page de connection.");
            alert.showAndWait();
        }
    }

}
