package fr.insa.horodateurjava.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fr.insa.horodateurjava.ReservationController;

import java.io.IOException;

public class NavigationHelper {

    public static void navigateToReservation(Stage currentStage, String vehicleType, String parkingName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NavigationHelper.class.getResource("/fr/insa/horodateurjava/views/reservation-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            ReservationController controller = fxmlLoader.getController();
            controller.initializeData(vehicleType, parkingName);

            Stage reservationStage = new Stage();
            reservationStage.setTitle("Réservation - " + vehicleType);
            reservationStage.setScene(scene);
            reservationStage.show();

            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void navigateTo(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationHelper.class.getResource(fxmlPath));
            Parent root = loader.load();

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void handleBackLinkAction(Stage currentStage) {
        navigateTo(currentStage, "/fr/insa/horodateurjava/views/home-view.fxml");
    }
}
