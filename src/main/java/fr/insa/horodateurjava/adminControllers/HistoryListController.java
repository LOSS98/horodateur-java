package fr.insa.horodateurjava.adminControllers;

import fr.insa.horodateurjava.database.dao.ReservationDAO;
import fr.insa.horodateurjava.models.Reservation;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Contrôleur pour afficher l'historique des réservations.
 * Cette classe gère l'affichage et la navigation de la vue contenant la liste des réservations.
 */
public class HistoryListController {

    @FXML
    private TableView<Reservation> placeTableView; // Tableau pour afficher les réservations

    @FXML
    private TableColumn<Reservation, Integer> idColumn; // Colonne pour le numéro de réservation

    @FXML
    private TableColumn<Reservation, String> immaColumn; // Colonne pour l'immatriculation

    @FXML
    private TableColumn<Reservation, String> numColumn; // Colonne pour le numéro de la place

    @FXML
    private TableColumn<Reservation, String> parkingColumn; // Colonne pour l'identifiant du parking

    @FXML
    private TableColumn<Reservation, LocalDateTime> startColumn; // Colonne pour la date de début

    @FXML
    private TableColumn<Reservation, LocalDateTime> endColumn; // Colonne pour la date de fin

    @FXML
    private TableColumn<Reservation, Double> priceColumn; // Colonne pour le prix

    @FXML
    private Label currentPlacesField; // Label pour afficher le nombre total de réservations

    private ReservationDAO reservationDAO; // DAO pour les opérations liées aux réservations

    /**
     * Méthode d'initialisation du contrôleur.
     * Configure les colonnes du tableau, initialise le DAO et charge les données.
     *
     * @throws SQLException en cas de problème lors de la connexion à la base de données.
     */
    public void initialize() throws SQLException {
        // Initialiser le DAO avec la connexion à la base de données
        Connection connection = DatabaseHandler.getConnection();
        reservationDAO = new ReservationDAO(connection);

        // Configurer les colonnes du TableView pour lier les données aux propriétés des objets
        idColumn.setCellValueFactory(new PropertyValueFactory<>("numeroReservation"));
        immaColumn.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        numColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPlace"));
        parkingColumn.setCellValueFactory(new PropertyValueFactory<>("idParking"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("dateHeureDebut"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("dateHeureFin"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        // Charger les données des réservations depuis la base de données
        ObservableList<Reservation> data = FXCollections.observableArrayList(reservationDAO.getAllReservations());
        placeTableView.setItems(data);

        // Mettre à jour le label pour afficher le nombre total de réservations
        currentPlacesField.setText(String.valueOf(data.size()));
    }

    /**
     * Gère l'action pour revenir à la page d'accueil administrateur.
     *
     * @param actionEvent L'événement déclencheur.
     */
    @FXML
    public void handleBackLinkAction(ActionEvent actionEvent) {
        try {
            // Charger la vue d'accueil de l'administrateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Parent root = loader.load();

            // Obtenir la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
