package fr.insa.horodateurjava.adminControllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.insa.horodateurjava.database.dao.ParkingDAO;
import fr.insa.horodateurjava.database.dao.ReservationDAO;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import fr.insa.horodateurjava.utils.NavigationHelper;
import fr.insa.horodateurjava.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Contrôleur pour la page d'accueil administrateur.
 * Gère la navigation entre les différentes vues et propose des actions pour les administrateurs.
 */
public class AdminHomeController {

    @FXML
    private Label adminNameLabel;

    /**
     * Initialisation de la vue d'accueil.
     * Récupère les informations de session pour afficher le nom de l'administrateur connecté.
     */
    public void initialize() {
        Session session = Session.getInstance();
        String fname = session.getFname();
        String lname = session.getLname();
        adminNameLabel.setText("Bienvenue, " + fname + " " + lname + " !");
    }

    /**
     * Déconnexion de l'administrateur.
     * Efface la session et redirige vers la page d'accueil.
     */
    @FXML
    private void onLogout() {
        Session session = Session.getInstance();
        session.clearSession();

        showAlert(Alert.AlertType.INFORMATION, "Déconnexion", "Vous avez été déconnecté avec succès !");

        Stage currentStage = (Stage) adminNameLabel.getScene().getWindow();
        NavigationHelper.navigateTo(currentStage, "/fr/insa/horodateurjava/views/home-view.fxml");
    }

    /**
     * Navigue vers la vue pour ajouter un parking.
     */
    @FXML
    public void onAddParking(ActionEvent actionEvent) {
        navigateToView(actionEvent, "/fr/insa/horodateurjava/views/admin/add-parking-view.fxml", "Ajouter un Parking");
    }

    /**
     * Navigue vers la vue pour ajouter une place de parking.
     */
    @FXML
    public void onAddPlace(ActionEvent actionEvent) {
        navigateToView(actionEvent, "/fr/insa/horodateurjava/views/admin/add-place-view.fxml", "Ajouter une Place");
    }

    /**
     * Navigue vers la vue pour ajouter un administrateur.
     */
    @FXML
    public void onAddAdmin(ActionEvent actionEvent) {
        navigateToView(actionEvent, "/fr/insa/horodateurjava/views/admin/add-admin-view.fxml", "Ajouter un Administrateur");
    }

    /**
     * Navigue vers la liste des places de parking.
     */
    @FXML
    public void onListPlaces(ActionEvent actionEvent) {
        navigateToView(actionEvent, "/fr/insa/horodateurjava/views/admin/list-places-view.fxml", "Liste des Places");
    }

    /**
     * Navigue vers la liste des parkings.
     */
    @FXML
    public void onListParkings(ActionEvent actionEvent) {
        navigateToView(actionEvent, "/fr/insa/horodateurjava/views/admin/list-parkings-view.fxml", "Liste des Parkings");
    }

    /**
     * Navigue vers la liste des administrateurs.
     */
    @FXML
    public void onListAdmins(ActionEvent actionEvent) {
        navigateToView(actionEvent, "/fr/insa/horodateurjava/views/admin/list-admins-view.fxml", "Liste des Administrateurs");
    }

    /**
     * Navigue vers la vue pour générer un rapport.
     */
    @FXML
    public void onGenerateReport(ActionEvent actionEvent) {
        navigateToView(actionEvent, "/fr/insa/horodateurjava/views/admin/report-view.fxml", "Générer un Rapport");
    }

    /**
     * Navigue vers l'historique des actions ou des réservations.
     */
    @FXML
    public void onViewHistory(ActionEvent actionEvent) {
        navigateToView(actionEvent, "/fr/insa/horodateurjava/views/admin/list-history-view.fxml", "Historique");
    }

    /**
     * Affiche une alerte d'information, d'avertissement ou d'erreur.
     *
     * @param type    Type de l'alerte (INFORMATION, WARNING, ERROR).
     * @param title   Titre de l'alerte.
     * @param message Contenu du message de l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Navigue vers une vue spécifique en chargeant son FXML.
     *
     * @param actionEvent Événement déclencheur.
     * @param fxmlPath    Chemin du fichier FXML de la vue cible.
     * @param title       Titre de la fenêtre après navigation.
     */
    private void navigateToView(ActionEvent actionEvent, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setTitle(title);
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
