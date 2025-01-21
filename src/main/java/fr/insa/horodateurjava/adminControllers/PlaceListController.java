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

/**
 * Contrôleur pour la gestion de la liste des places d'un parking.
 * Permet de visualiser les places disponibles pour un parking donné.
 */
public class PlaceListController {

    @FXML
    private ComboBox<String> parkingComboBox; // Liste déroulante pour sélectionner un parking

    @FXML
    private TableView<Place> placeTableView; // TableView pour afficher les places

    @FXML
    private TableColumn<Place, Integer> numeroColumn; // Colonne pour le numéro de la place

    @FXML
    private TableColumn<Place, Integer> etageColumn; // Colonne pour l'étage de la place

    @FXML
    private TableColumn<Place, String> typeColumn; // Colonne pour le type de place

    @FXML
    private TableColumn<Place, Boolean> disponibiliteColumn; // Colonne pour la disponibilité

    @FXML
    private TableColumn<Place, Double> tarifHoraireColumn; // Colonne pour le tarif horaire

    @FXML
    private TableColumn<Place, Double> puissanceChargeColumn; // Colonne pour la puissance de charge (si applicable)

    @FXML
    private TableColumn<Place, Boolean> enTravauxColumn; // Colonne pour indiquer si la place est en travaux

    @FXML
    private Label currentPlacesField; // Label pour afficher le nombre de places actuelles

    @FXML
    private Label totalPlacesField; // Label pour afficher la capacité totale du parking

    @FXML
    private Label messageLabel; // Label pour afficher les messages d'erreur ou d'information

    /**
     * Initialise les données et configure les colonnes de la table.
     */
    @FXML
    public void initialize() {
        // Configuration des colonnes
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

    /**
     * Charge les noms des parkings dans la liste déroulante.
     */
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

    /**
     * Affiche les places du parking sélectionné.
     *
     * @param event Événement déclenché par le bouton "Afficher".
     */
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

            int parkingId = Integer.parseInt(selectedParking.split(" - ")[0]); // Extraire l'ID du parking
            int totalCapacity = parkingDAO.getParkingCapacity(parkingId); // Récupérer la capacité totale
            int currentPlacesCount = parkingDAO.getParkingCurrentPlacesCount(parkingId); // Récupérer le nombre de places actuelles

            // Récupérer les places du parking
            List<Place> places = placeDAO.getPlacesByParking(parkingId);

            // Mettre à jour la TableView
            placeTableView.setItems(FXCollections.observableArrayList(places));

            // Mettre à jour les labels
            currentPlacesField.setText(String.valueOf(currentPlacesCount));
            totalPlacesField.setText(String.valueOf(totalCapacity));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de récupérer les places pour ce parking.");
        }
    }

    /**
     * Affiche une alerte avec un message personnalisé.
     *
     * @param alertType Type d'alerte (INFO, WARNING, ERROR).
     * @param title     Titre de l'alerte.
     * @param content   Contenu du message.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Retourne à la page d'accueil administrateur.
     *
     * @param actionEvent Événement déclenché par le clic sur le bouton "Retour".
     */
    public void handleBackLinkAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Parent root = loader.load();

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Charger la nouvelle scène
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
