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

/**
 * Contrôleur pour afficher la liste des parkings et gérer les interactions utilisateur associées.
 */
public class ParkingListController {

    @FXML
    private TableView<Parking> placeTableView; // TableView pour afficher la liste des parkings

    @FXML
    private TableColumn<Parking, Integer> idColumn; // Colonne pour l'ID du parking

    @FXML
    private TableColumn<Parking, String> nomColumn; // Colonne pour le nom du parking

    @FXML
    private TableColumn<Parking, String> adresseColumn; // Colonne pour l'adresse du parking

    @FXML
    private TableColumn<Parking, Integer> capColumn; // Colonne pour la capacité totale du parking

    @FXML
    private TableColumn<Parking, Integer> placesCountColumn; // Colonne pour le nombre de places déclarées

    @FXML
    private Label parkingCountField; // Label pour afficher le nombre total de parkings

    private ObservableList<Parking> parkingList = FXCollections.observableArrayList();

    /**
     * Méthode d'initialisation appelée automatiquement après le chargement de la vue.
     */
    public void initialize() {
        // Configuration des colonnes du TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idParking"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomDuParking"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresseDuParking"));
        capColumn.setCellValueFactory(new PropertyValueFactory<>("nombrePlaces"));
        placesCountColumn.setCellValueFactory(new PropertyValueFactory<>("placesDeclarees"));

        // Charger les données dans la TableView
        loadParkingData();
    }

    /**
     * Charge les données des parkings depuis la base de données.
     */
    private void loadParkingData() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            // Récupération des données à l'aide du DAO
            ParkingDAO parkingDAO = new ParkingDAO(connection);
            List<Parking> parkings = parkingDAO.getAllParkingsWithPlacesCount();

            // Mise à jour de la liste observable
            parkingList.setAll(parkings);

            // Mettre à jour la TableView
            placeTableView.setItems(parkingList);

            // Afficher le nombre total de parkings dans le Label
            parkingCountField.setText(String.valueOf(parkingList.size()));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les parkings.");
        }
    }

    /**
     * Gère l'action de retour vers la page principale de l'admin.
     *
     * @param event Événement déclencheur.
     */
    @FXML
    private void handleBackLinkAction(ActionEvent event) {
        try {
            // Charger la vue principale de l'admin
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

    /**
     * Affiche une alerte à l'utilisateur.
     *
     * @param alertType Type d'alerte (WARNING, INFORMATION, ERROR).
     * @param title     Titre de l'alerte.
     * @param message   Contenu du message.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
