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

public class ReportListController {

    @FXML
    private TableView<Rapport> placeTableView;

    @FXML
    private TableColumn<Rapport, Integer> idColumn;

    @FXML
    private TableColumn<Rapport, Float> occupationColumn;

    @FXML
    private TableColumn<Rapport, Double> recettesColumn;

    @FXML
    private TableColumn<Rapport, String> startColumn;

    @FXML
    private TableColumn<Rapport, String> endColumn;

    @FXML
    private TableColumn<Rapport, String> generatedByColumn;

    @FXML
    private javafx.scene.control.Label countField;

    private ObservableList<Rapport> rapports = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurer les colonnes de la table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idRapport"));
        occupationColumn.setCellValueFactory(new PropertyValueFactory<>("tauxOccupation"));
        recettesColumn.setCellValueFactory(new PropertyValueFactory<>("totalRecettes"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        generatedByColumn.setCellValueFactory(new PropertyValueFactory<>("adminName"));

        loadReports();
    }

    private void loadReports() {
        try {
            Connection connection = DatabaseHandler.getConnection();
            ReportDAO reportDAO = new ReportDAO(connection);

            List<Rapport> rapportList = reportDAO.getAllReports();
            rapports.clear();
            rapports.addAll(rapportList);

            placeTableView.setItems(rapports);

            // Mettre à jour le compteur de rapports
            countField.setText(String.valueOf(rapportList.size()));

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les rapports.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleBackLinkAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
