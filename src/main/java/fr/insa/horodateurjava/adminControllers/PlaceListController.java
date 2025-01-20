package fr.insa.horodateurjava.adminControllers;

import fr.insa.horodateurjava.database.dao.ParkingDAO;
import fr.insa.horodateurjava.database.dao.PlaceDAO;
import fr.insa.horodateurjava.models.Place;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class PlaceListController {

    @FXML
    private ComboBox<String> parkingComboBox;

    @FXML
    private TableView<Place> placeTableView;

    @FXML
    private TableColumn<Place, Integer> numeroColumn;

    @FXML
    private TableColumn<Place, Integer> etageColumn;

    @FXML
    private TableColumn<Place, String> typeColumn;

    @FXML
    private TableColumn<Place, Boolean> disponibiliteColumn;

    @FXML
    private TableColumn<Place, Double> tarifHoraireColumn;

    @FXML
    private TableColumn<Place, Double> puissanceChargeColumn;

    @FXML
    private TableColumn<Place, Boolean> enTravauxColumn;

    @FXML
    private Label currentPlacesField;

    @FXML
    private Label  totalPlacesField;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        // Configurer les colonnes
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        etageColumn.setCellValueFactory(new PropertyValueFactory<>("etage"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        disponibiliteColumn.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
        tarifHoraireColumn.setCellValueFactory(new PropertyValueFactory<>("tarifHoraire"));
        puissanceChargeColumn.setCellValueFactory(new PropertyValueFactory<>("puissanceCharge"));
        enTravauxColumn.setCellValueFactory(new PropertyValueFactory<>("enTravaux"));

        // Charger les noms des parkings
        loadParkingNames();
    }

    private void loadParkingNames() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            ParkingDAO parkingDAO = new ParkingDAO(connection);
            List<String> parkingNames = parkingDAO.getAllParkingNamesWithIds();
            parkingComboBox.getItems().addAll(parkingNames);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les noms des parkings.");
        }
    }

    @FXML
    private void handleShowPlaces(ActionEvent event) {
        String selectedParking = parkingComboBox.getValue();

        if (selectedParking == null || selectedParking.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un parking.");
            return;
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            PlaceDAO placeDAO = new PlaceDAO(connection);
            ParkingDAO parkingDAO = new ParkingDAO(connection);

            int parkingId = Integer.parseInt(selectedParking.split(" - ")[0]);
            int totalCapacity = parkingDAO.getParkingCapacity(parkingId);
            int currentPlacesCount = parkingDAO.getParkingCurrentPlacesCount(parkingId);
            // Récupérer les places du parking
            List<Place> places = placeDAO.getPlacesByParking(parkingId);

            // Ajouter les places à la TableView
            placeTableView.setItems(FXCollections.observableArrayList(places));
            currentPlacesField.setText(String.valueOf(currentPlacesCount));
            totalPlacesField.setText(String.valueOf(totalCapacity));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de récupérer les places pour ce parking.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
