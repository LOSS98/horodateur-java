package fr.insa.horodateurjava.adminControllers;

import fr.insa.horodateurjava.database.dao.ReportDAO;
import fr.insa.horodateurjava.database.dao.ReservationDAO;
import fr.insa.horodateurjava.models.Rapport;
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

public class HistoryListController {

    @FXML
    private TableView<Reservation> placeTableView;

    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    @FXML
    private TableColumn<Reservation, String> immaColumn;

    @FXML
    private TableColumn<Reservation, String> numColumn;

    @FXML
    private TableColumn<Reservation, String> parkingColumn;

    @FXML
    private TableColumn<Reservation, LocalDateTime> startColumn;

    @FXML
    private TableColumn<Reservation, LocalDateTime> endColumn;

    @FXML
    private TableColumn<Reservation, Double> priceColumn;

    @FXML
    private Label currentPlacesField;

    private ReservationDAO reservationDAO;

    public void initialize() throws SQLException {
        // Initialiser DAO
        Connection connection = DatabaseHandler.getConnection();
        reservationDAO = new ReservationDAO(connection);

        // Configurer les colonnes du TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("numeroReservation"));
        immaColumn.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        numColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPlace"));
        parkingColumn.setCellValueFactory(new PropertyValueFactory<>("idParking"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("dateHeureDebut"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("dateHeureFin"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        // Charger les données
        ObservableList<Reservation> data = FXCollections.observableArrayList(reservationDAO.getAllReservations());
        placeTableView.setItems(data);

        // Mettre à jour le nombre de réservations
        currentPlacesField.setText(String.valueOf(data.size()));
    }
    @FXML
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
