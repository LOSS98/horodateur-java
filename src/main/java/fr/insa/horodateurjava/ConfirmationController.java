package fr.insa.horodateurjava;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contrôleur pour la vue de confirmation de réservation.
 * Cette classe gère l'affichage des informations de confirmation et permet de retourner à l'accueil.
 */
public class ConfirmationController {

    // Labels utilisés pour afficher les informations de la réservation
    @FXML
    private Label typePlaceLabel;        // Type de place réservée (ex: Classique, Handicape)
    @FXML
    private Label immatriculationLabel; // Numéro d'immatriculation du véhicule
    @FXML
    private Label etageNumberLabel;     // Numéro de l'étage où se trouve la place
    @FXML
    private Label placeNumberLabel;     // Numéro de la place réservée
    @FXML
    private Label parkingNameLabel;     // Nom du parking
    @FXML
    private Label startDateLabel;       // Date et heure de début de la réservation
    @FXML
    private Label endDateLabel;         // Date et heure de fin de la réservation
    @FXML
    private Label totalPriceLabel;      // Prix total de la réservation

    /**
     * Initialise les données de la vue de confirmation avec les détails de la réservation.
     *
     * @param typePlace      Type de la place (ex: Classique, Handicape).
     * @param immatriculation Numéro d'immatriculation du véhicule.
     * @param placeNumero    Numéro de la place.
     * @param etageNumero    Numéro de l'étage.
     * @param parkingName    Nom du parking.
     * @param startDateTime  Date et heure de début de la réservation.
     * @param endDateTime    Date et heure de fin de la réservation.
     * @param totalPrice     Prix total de la réservation.
     */
    public void initialize(String typePlace, String immatriculation, int placeNumero, int etageNumero, String parkingName, LocalDateTime startDateTime, LocalDateTime endDateTime, double totalPrice) {
        // Affectation des valeurs aux labels correspondants
        typePlaceLabel.setText(typePlace);
        immatriculationLabel.setText(immatriculation);
        placeNumberLabel.setText(String.valueOf(placeNumero));
        etageNumberLabel.setText(String.valueOf(etageNumero));
        parkingNameLabel.setText(parkingName);

        // Formatage des dates pour un affichage convivial
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");
        String formattedStartDate = startDateTime.format(formatter);
        String formattedEndDate = endDateTime.format(formatter);

        // Affichage des dates et du prix
        startDateLabel.setText(formattedStartDate);
        endDateLabel.setText(formattedEndDate);
        totalPriceLabel.setText(totalPrice + " €");
    }

    /**
     * Action déclenchée lors du retour à la page d'accueil.
     * Charge la vue d'accueil et remplace la scène actuelle.
     */
    @FXML
    private void onBackToHome() {
        try {
            // Chargement du fichier FXML de la vue d'accueil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/home-view.fxml"));
            BorderPane homeView = loader.load();

            // Récupération de la fenêtre actuelle
            Stage stage = (Stage) immatriculationLabel.getScene().getWindow();
            // Création et affectation de la nouvelle scène
            Scene scene = new Scene(homeView);
            stage.setScene(scene);
        } catch (Exception e) {
            // Gestion des exceptions en cas de problème lors du chargement de la vue
            e.printStackTrace();
        }
    }
}
