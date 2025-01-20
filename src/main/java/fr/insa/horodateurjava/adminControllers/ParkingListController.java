package fr.insa.horodateurjava.adminControllers;

import fr.insa.horodateurjava.database.dao.ParkingDAO;
import fr.insa.horodateurjava.models.Parking;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class ParkingListController {

    @FXML
    private TableView<Parking> placeTableView;

    @FXML
    private TableColumn<Parking, Integer> idColumn;

    @FXML
    private TableColumn<Parking, String> nomColumn;

    @FXML
    private TableColumn<Parking, String> adresseColumn;

    @FXML
    private TableColumn<Parking, Integer> capColumn;

    @FXML
    private TableColumn<Parking, Integer> placesCountColumn;

    @FXML
    private Label parkingCountField;

    private ObservableList<Parking> parkingList = FXCollections.observableArrayList();

    public void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idParking"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomDuParking"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresseDuParking"));
        capColumn.setCellValueFactory(new PropertyValueFactory<>("nombrePlaces"));
        placesCountColumn.setCellValueFactory(new PropertyValueFactory<>("placesDeclarees"));

        // Charger les données dans la table
        loadParkingData();
    }

    private void loadParkingData() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            ParkingDAO parkingDAO = new ParkingDAO(connection);
            List<Parking> parkings = parkingDAO.getAllParkingsWithPlacesCount();
            parkingList.setAll(parkings);

            // Mettre à jour la TableView
            placeTableView.setItems(parkingList);

            // Mettre à jour le nombre total de parkings
            parkingCountField.setText(String.valueOf(parkingList.size()));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les parkings.");
        }
    }

    @FXML
    private void handleBackLinkAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Parent root = loader.load();

            // Obtenir la fenêtre actuelle
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Charger la nouvelle scène
            currentStage.setScene(scene);
            currentStage.setTitle("Accueil Admin");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à la page d'accueil admin.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
