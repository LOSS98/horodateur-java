package fr.insa.horodateurjava.utils;

import fr.insa.horodateurjava.adminControllers.AdminHomeController;
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

    public static void navigateToAdmin(Stage currentStage, String adminFirstName, String adminLastName) {
        try {
            // Charger le fichier FXML pour la vue admin
            FXMLLoader fxmlLoader = new FXMLLoader(NavigationHelper.class.getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Récupérer le contrôleur de la vue
            AdminHomeController controller = fxmlLoader.getController();

            // Créer une nouvelle fenêtre (Stage) pour l'interface admin
            Stage adminStage = new Stage();
            adminStage.setTitle("Administration - " + adminFirstName + " " + adminLastName);
            adminStage.setScene(scene);
            adminStage.show();

            // Fermer la fenêtre actuelle
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
