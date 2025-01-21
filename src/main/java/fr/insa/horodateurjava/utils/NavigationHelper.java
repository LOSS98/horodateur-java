package fr.insa.horodateurjava.utils;

import fr.insa.horodateurjava.adminControllers.AdminHomeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fr.insa.horodateurjava.ReservationController;

import java.io.IOException;

/**
 * Classe utilitaire pour faciliter la navigation entre les différentes vues de l'application.
 * Cette classe contient des méthodes statiques permettant de charger et afficher des scènes.
 */
public class NavigationHelper {

    /**
     * Navigue vers la vue de réservation.
     *
     * @param currentStage La fenêtre actuelle.
     * @param vehicleType  Le type de véhicule à réserver.
     * @param parkingName  Le nom du parking sélectionné.
     */
    public static void navigateToReservation(Stage currentStage, String vehicleType, String parkingName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NavigationHelper.class.getResource("/fr/insa/horodateurjava/views/reservation-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Initialiser le contrôleur de la vue de réservation avec les données
            ReservationController controller = fxmlLoader.getController();
            controller.initializeData(vehicleType, parkingName);

            // Créer une nouvelle fenêtre pour la réservation
            Stage reservationStage = new Stage();
            reservationStage.setTitle("Réservation - " + vehicleType);
            reservationStage.setScene(scene);
            reservationStage.show();

            // Fermer la fenêtre actuelle
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigue vers la vue admin.
     *
     * @param currentStage   La fenêtre actuelle.
     * @param adminFirstName Le prénom de l'administrateur.
     * @param adminLastName  Le nom de l'administrateur.
     */
    public static void navigateToAdmin(Stage currentStage, String adminFirstName, String adminLastName) {
        try {
            // Charger le fichier FXML pour la vue admin
            FXMLLoader fxmlLoader = new FXMLLoader(NavigationHelper.class.getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Récupérer le contrôleur de la vue admin
            AdminHomeController controller = fxmlLoader.getController();

            // Créer une nouvelle fenêtre pour l'interface admin
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

    /**
     * Navigue vers une vue spécifiée par son chemin FXML.
     *
     * @param stage    La fenêtre actuelle.
     * @param fxmlPath Le chemin du fichier FXML.
     */
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

    /**
     * Gère l'action de retour vers la vue d'accueil.
     *
     * @param currentStage La fenêtre actuelle.
     */
    public static void handleBackLinkAction(Stage currentStage) {
        navigateTo(currentStage, "/fr/insa/horodateurjava/views/home-view.fxml");
    }
}
