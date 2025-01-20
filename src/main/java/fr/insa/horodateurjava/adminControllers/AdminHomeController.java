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

public class AdminHomeController {
    @FXML
    private Label adminNameLabel;

    public void initialize() {
        Session session = Session.getInstance();
        String fname = session.getFname();
        String lname = session.getLname();
        String userEmail = session.getUserEmail();
        adminNameLabel.setText("Bienvenue, " + fname + " " + lname + " !");
    }

    @FXML
    private void onLogout() {
        Session session = Session.getInstance();
        session.clearSession();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText(null);
        alert.setContentText("Vous avez été déconnecté avec succès !");
        alert.showAndWait();

        Stage currentStage = (Stage) adminNameLabel.getScene().getWindow();
        NavigationHelper.navigateTo(currentStage, "/fr/insa/horodateurjava/views/home-view.fxml");
    }

    @FXML
    public void onAddParking(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/add-parking-view.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();            currentStage.setTitle("Ajouter un Parking");
            currentStage.setScene(new Scene(root));
            currentStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onAddPlace(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/add-place-view.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setTitle("Ajouter un Parking");
            currentStage.setScene(new Scene(root));
            currentStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddAdmin(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/add-admin-view.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setTitle("Ajouter un Parking");
            currentStage.setScene(new Scene(root));
            currentStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onListPlaces(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/list-places-view.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setTitle("Ajouter un Parking");
            currentStage.setScene(new Scene(root));
            currentStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onListParkings(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/list-parkings-view.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setTitle("Ajouter un Parking");
            currentStage.setScene(new Scene(root));
            currentStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onListAdmins(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/list-admins-view.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setTitle("Ajouter un Parking");
            currentStage.setScene(new Scene(root));
            currentStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onGenerateReport(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/report-view.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setTitle("Ajouter un Parking");
            currentStage.setScene(new Scene(root));
            currentStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onViewHistory(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/list-history-view.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.setTitle("Ajouter un Parking");
            currentStage.setScene(new Scene(root));
            currentStage.show();


        } catch (IOException e) {
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




}
