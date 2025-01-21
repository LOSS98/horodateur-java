package fr.insa.horodateurjava.adminControllers;

import fr.insa.horodateurjava.database.dao.ReportDAO;
import fr.insa.horodateurjava.models.Rapport;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Contrôleur pour gérer la liste des rapports générés et afficher leurs informations dans une interface utilisateur.
 */
public class ReportListController {

    @FXML
    private TableView<Rapport> placeTableView; // TableView pour afficher la liste des rapports

    @FXML
    private TableColumn<Rapport, Integer> idColumn; // Colonne pour l'identifiant du rapport

    @FXML
    private TableColumn<Rapport, Float> occupationColumn; // Colonne pour le taux d'occupation

    @FXML
    private TableColumn<Rapport, Double> recettesColumn; // Colonne pour les recettes totales

    @FXML
    private TableColumn<Rapport, String> startColumn; // Colonne pour la date de début de la période

    @FXML
    private TableColumn<Rapport, String> endColumn; // Colonne pour la date de fin de la période

    @FXML
    private TableColumn<Rapport, String> generatedByColumn; // Colonne pour l'email de l'admin ayant généré le rapport

    @FXML
    private javafx.scene.control.Label countField; // Label pour afficher le nombre total de rapports

    private ObservableList<Rapport> rapports = FXCollections.observableArrayList(); // Liste observable des rapports

    /**
     * Méthode d'initialisation appelée automatiquement lors du chargement de la vue.
     * Configure les colonnes et charge les rapports depuis la base de données.
     */
    @FXML
    public void initialize() {
        // Configuration des colonnes de la table avec les propriétés correspondantes des objets Rapport
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idRapport"));
        occupationColumn.setCellValueFactory(new PropertyValueFactory<>("tauxOccupation"));
        recettesColumn.setCellValueFactory(new PropertyValueFactory<>("totalRecettes"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        generatedByColumn.setCellValueFactory(new PropertyValueFactory<>("emailAdmin"));

        // Charger les rapports depuis la base de données
        loadReports();
    }

    /**
     * Charge les rapports depuis la base de données et les affiche dans le TableView.
     */
    private void loadReports() {
        try {
            // Connexion à la base de données
            Connection connection = DatabaseHandler.getConnection();
            ReportDAO reportDAO = new ReportDAO(connection);

            // Récupérer tous les rapports
            List<Rapport> rapportList = reportDAO.getAllReports();

            // Effacer les données actuelles et ajouter les nouveaux rapports
            rapports.clear();
            rapports.addAll(rapportList);

            // Définir les données dans le TableView
            placeTableView.setItems(rapports);

            // Mettre à jour le compteur de rapports
            countField.setText(String.valueOf(rapportList.size()));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les rapports.");
            e.printStackTrace();
        }
    }

    /**
     * Affiche une alerte avec le message donné.
     *
     * @param type    Le type d'alerte (e.g., INFORMATION, ERROR)
     * @param title   Le titre de l'alerte
     * @param message Le message à afficher dans l'alerte
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Gère l'action de retour à l'accueil admin.
     *
     * @param actionEvent L'événement déclenché par l'utilisateur
     */
    @FXML
    public void handleBackLinkAction(ActionEvent actionEvent) {
        try {
            // Charger la vue de l'accueil admin
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et définir la nouvelle scène
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
