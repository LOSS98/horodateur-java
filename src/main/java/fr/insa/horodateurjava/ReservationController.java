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

/**
 * Contrôleur de réservation qui gère la réservation de places dans le parking.
 */
public class ReservationController {

    @FXML
    private TextField licensePlateField; // Champ pour entrer la plaque d'immatriculation
    @FXML
    private DatePicker endDatePicker; // Sélecteur de date pour la fin de réservation
    @FXML
    private TextField endHourField; // Champ pour l'heure de fin
    @FXML
    private TextField endMinuteField; // Champ pour les minutes de fin
    @FXML
    private TextField preferredFloorField; // Champ pour spécifier l'étage préféré
    @FXML
    private Label messageLabel; // Label pour afficher les messages d'erreur ou de confirmation

    private String vehicleType; // Type de véhicule sélectionné
    private String parkingName; // Nom du parking sélectionné

    /**
     * Initialise les données de la réservation avec le type de véhicule et le nom du parking.
     *
     * @param vehicleType Type de véhicule sélectionné.
     * @param parkingName Nom du parking sélectionné.
     */
    public void initializeData(String vehicleType, String parkingName) {
        this.vehicleType = vehicleType;
        this.parkingName = parkingName;
    }

    /**
     * Action pour effectuer une réservation de place.
     * Valide les champs, recherche une place disponible et confirme la réservation.
     */
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

            // Formatage et validation de l'heure
            String timeString = endHourField.getText() + ":" + endMinuteField.getText() + ":00";

            try {
                endTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (Exception e) {
                messageLabel.setText("Erreur : format de l'heure incorrect.");
                return;
            }

            endDateTime = LocalDateTime.of(endDatePicker.getValue(), endTime);

            // Vérifier si la date et l'heure de fin sont après la date actuelle
            if (endDateTime.isBefore(now)) {
                messageLabel.setText("Erreur : la date et l'heure de fin doivent être ultérieures à maintenant.");
                return;
            }

            try (Connection connection = DatabaseHandler.getConnection()) {
                PlaceDAO placeDAO = new PlaceDAO(connection);

                // Recherche de place disponible
                Place availablePlace = placeDAO.findAvailablePlace(vehicleType, parkingName, preferredFloorStr);
                if (availablePlace != null) {
                    Duration duration = Duration.between(now, endDateTime);
                    long totalHours = duration.toHours();

                    double totalPrice = totalHours * availablePlace.getTarifHoraire();

                    // Création de la réservation
                    Reservation reservation = new Reservation(0, immatriculation, availablePlace.getNumero(), now, endDateTime, totalPrice);
                    placeDAO.reservePlace(availablePlace, reservation);

                    // Charger la vue de confirmation
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/confirmation-view.fxml"));
                    BorderPane confirmationView = loader.load();

                    ConfirmationController confirmationController = loader.getController();
                    confirmationController.initialize(vehicleType, immatriculation, availablePlace.getNumero(), availablePlace.getEtage(), parkingName, now, endDateTime, totalPrice);

                    // Mise à jour de la scène
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

    /**
     * Action pour retourner à l'accueil.
     *
     * @param event Événement déclenché par le clic sur le lien de retour.
     */
    @FXML
    private void handleBackLinkAction(ActionEvent event) {
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        NavigationHelper.handleBackLinkAction(currentStage);
    }
}
