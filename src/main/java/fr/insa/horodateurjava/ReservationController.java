package fr.insa.horodateurjava;

import fr.insa.horodateurjava.database.dao.PlaceDAO;
import fr.insa.horodateurjava.models.Place;
import fr.insa.horodateurjava.models.Reservation;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import fr.insa.horodateurjava.utils.NavigationHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ReservationController {

    @FXML
    private TextField licensePlateField;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField endHourField;
    @FXML
    private TextField endMinuteField;
    @FXML
    private TextField preferredFloorField;
    @FXML
    private Label messageLabel;

    private String vehicleType;
    private String parkingName;

    public void initializeData(String vehicleType, String parkingName) {
        this.vehicleType = vehicleType;
        this.parkingName = parkingName;
    }

    @FXML
    private void onReservePlace() {
        try {
            LocalDateTime now = LocalDateTime.now();
            String immatriculation = licensePlateField.getText();
            String preferredFloorStr = preferredFloorField.getText();

            // Vérification des champs requis
            if (immatriculation.isEmpty() || preferredFloorStr.isEmpty()) {
                messageLabel.setText("Erreur : tous les champs doivent être remplis.");
                return;
            }

            if (endDatePicker.getValue() == null || endHourField.getText().isEmpty() || endMinuteField.getText().isEmpty()) {
                messageLabel.setText("Erreur : veuillez spécifier la date et l'heure de fin.");
                return;
            }



            LocalDateTime endDateTime;
            LocalTime endTime;

            String timeString = endHourField.getText() + ":" + endMinuteField.getText() + ":00";




            try {
                endTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                messageLabel.setText("Erreur : format de l'heure incorrect.");
                return;
            }

            endDateTime = LocalDateTime.of(endDatePicker.getValue(), endTime);

            if (endDateTime.isBefore(now)) {
                messageLabel.setText("Erreur : la date et l'heure de fin doivent être ultérieures à maintenant.");
                return;
            }

            try (Connection connection = DatabaseHandler.getConnection()) {
                PlaceDAO placeDAO = new PlaceDAO(connection);

                Place availablePlace = placeDAO.findAvailablePlace(vehicleType, parkingName, preferredFloorStr);
                if (availablePlace != null) {
                    Duration duration = Duration.between(now, endDateTime);
                    long totalHours = duration.toHours();

                    double totalPrice = totalHours * availablePlace.getTarifHoraire();
                    Reservation reservation = new Reservation(0, immatriculation, availablePlace.getNumero(), now, endDateTime, totalPrice);
                    placeDAO.reservePlace(availablePlace, reservation);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/confirmation-view.fxml"));

                    BorderPane confirmationView = loader.load();

                    ConfirmationController confirmationController = loader.getController();



                    confirmationController.initialize(vehicleType, immatriculation, availablePlace.getNumero(), availablePlace.getEtage(), parkingName, now, endDateTime, totalPrice);

                    Stage stage = (Stage) messageLabel.getScene().getWindow();
                    Scene scene = new Scene(confirmationView);
                    stage.setScene(scene);

                    messageLabel.setText("Réservation confirmée pour la place " + availablePlace.getNumero());
                } else {
                    messageLabel.setText("Aucune place disponible.");
                }
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Erreur : l'étage doit être un nombre valide.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Erreur lors de la réservation.");
        }
    }

    @FXML
    private void handleBackLinkAction(ActionEvent event) {
        // Récupérer la fenêtre actuelle et utiliser NavigationHelper
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        NavigationHelper.handleBackLinkAction(currentStage);
    }

}
