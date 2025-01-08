package fr.insa.horodateurjava;

import fr.insa.horodateurjava.database.dao.PlaceDAO;
import fr.insa.horodateurjava.database.models.*;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;

public class HelloController {

    @FXML
    private TableView<Place> tableView;

    @FXML
    private TableColumn<Place, Integer> colNumero;

    @FXML
    private TableColumn<Place, Boolean> colDisponibilite;

    @FXML
    private TableColumn<Place, Boolean> colEnTravaux;

    @FXML
    private TableColumn<Place, Integer> colEtage;

    @FXML
    private TableColumn<Place, Double> colTarifHoraire;

    @FXML
    private TableColumn<Place, Integer> colParking;

    @FXML
    private TableColumn<Place, String> colType;

    private ObservableList<Place> places;

    public void initialize() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            PlaceDAO placeDAO = new PlaceDAO(connection);

            // Récupérer les places classiques
            places = FXCollections.observableArrayList(placeDAO.getAllPlaces());

            // Configurer les colonnes
            colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
            colDisponibilite.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
            colEnTravaux.setCellValueFactory(new PropertyValueFactory<>("enTravaux"));
            colEtage.setCellValueFactory(new PropertyValueFactory<>("etage"));
            colTarifHoraire.setCellValueFactory(new PropertyValueFactory<>("tarifHoraire"));
            colParking.setCellValueFactory(new PropertyValueFactory<>("idParking"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));

            // Ajouter les données à la table
            tableView.setItems(places);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}








/*package fr.insa.horodateurjava;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}*/