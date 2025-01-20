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

public class AdminListController {

    @FXML
    private TableView<Administrateur> placeTableView;

    @FXML
    private TableColumn<Administrateur, Integer> idColumn;

    @FXML
    private TableColumn<Administrateur, String> nomColumn;

    @FXML
    private TableColumn<Administrateur, String> prenomColumn;

    @FXML
    private TableColumn<Administrateur, String> emailColumn;

    @FXML
    private Label adminCountField;

    private final ObservableList<Administrateur> adminList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idAdmin"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadAdmins();
    }

    private void loadAdmins() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            AdminDAO adminDAO = new AdminDAO(connection);
            List<Administrateur> admins = adminDAO.getAllAdmins();

            adminList.setAll(admins);
            placeTableView.setItems(adminList);
            adminCountField.setText(String.valueOf(admins.size()));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les administrateurs.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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
