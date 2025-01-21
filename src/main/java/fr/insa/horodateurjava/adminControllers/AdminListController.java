package fr.insa.horodateurjava.adminControllers;

import fr.insa.horodateurjava.database.dao.AdminDAO;
import fr.insa.horodateurjava.models.Administrateur;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Contrôleur pour la gestion de la liste des administrateurs.
 * Affiche une table avec les informations des administrateurs
 * et permet la navigation vers la vue d'accueil.
 */
public class AdminListController {

    @FXML
    private TableView<Administrateur> placeTableView; // TableView pour afficher les administrateurs

    @FXML
    private TableColumn<Administrateur, Integer> idColumn; // Colonne pour l'ID de l'administrateur

    @FXML
    private TableColumn<Administrateur, String> nomColumn; // Colonne pour le nom

    @FXML
    private TableColumn<Administrateur, String> prenomColumn; // Colonne pour le prénom

    @FXML
    private TableColumn<Administrateur, String> emailColumn; // Colonne pour l'email

    @FXML
    private Label adminCountField; // Champ pour afficher le nombre total d'administrateurs

    private final ObservableList<Administrateur> adminList = FXCollections.observableArrayList(); // Liste observable des administrateurs

    /**
     * Initialisation du contrôleur.
     * Configure les colonnes de la TableView et charge les administrateurs.
     */
    @FXML
    public void initialize() {
        // Associer les propriétés des colonnes aux attributs du modèle Administrateur
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idAdmin"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Charger les administrateurs dans la table
        loadAdmins();
    }

    /**
     * Charge les administrateurs depuis la base de données
     * et les affiche dans la TableView.
     */
    private void loadAdmins() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            AdminDAO adminDAO = new AdminDAO(connection);

            // Récupérer la liste des administrateurs depuis la base
            List<Administrateur> admins = adminDAO.getAllAdmins();

            // Mettre à jour la liste observable et la TableView
            adminList.setAll(admins);
            placeTableView.setItems(adminList);

            // Mettre à jour le champ avec le nombre total d'administrateurs
            adminCountField.setText(String.valueOf(admins.size()));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les administrateurs.");
        }
    }

    /**
     * Affiche une alerte avec le type, le titre et le message spécifiés.
     *
     * @param alertType Le type d'alerte (INFORMATION, WARNING, ERROR).
     * @param title     Le titre de l'alerte.
     * @param message   Le message à afficher dans l'alerte.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Gère l'action de retour en naviguant vers la vue d'accueil administrateur.
     *
     * @param actionEvent L'événement déclencheur.
     */
    public void handleBackLinkAction(ActionEvent actionEvent) {
        try {
            // Charger la vue de l'accueil administrateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Parent root = loader.load();

            // Obtenir la fenêtre actuelle et y appliquer la nouvelle scène
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
