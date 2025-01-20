package fr.insa.horodateurjava;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.time.Duration;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfirmationController {

    @FXML
    private Label typePlaceLabel;
    @FXML
    private Label immatriculationLabel;
    @FXML
    private Label etageNumberLabel;
    @FXML
    private Label placeNumberLabel;
    @FXML
    private Label parkingNameLabel;
    @FXML
    private Label startDateLabel;
    @FXML
    private Label endDateLabel;
    @FXML
    private Label totalPriceLabel;

    public void initialize(String typePlace, String immatriculation, int placeNumero,int etageNumero, String parkingName, LocalDateTime startDateTime, LocalDateTime endDateTime, double totalPrice) {
        typePlaceLabel.setText(typePlace);
        immatriculationLabel.setText(immatriculation);
        placeNumberLabel.setText(String.valueOf(placeNumero));
        etageNumberLabel.setText(String.valueOf(etageNumero));
        parkingNameLabel.setText(parkingName);
        startDateLabel.setText(String.valueOf(startDateTime));
        endDateLabel.setText(String.valueOf(endDateTime));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");
        String formattedStartDate = startDateTime.format(formatter);
        String formattedEndDate = endDateTime.format(formatter);
        startDateLabel.setText(formattedStartDate);
        endDateLabel.setText(formattedEndDate);



        totalPriceLabel.setText(totalPrice + " €");
    }

    @FXML
    private void onBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/home-view.fxml"));
            BorderPane homeView = loader.load();

            Stage stage = (Stage) immatriculationLabel.getScene().getWindow();
            Scene scene = new Scene(homeView);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
