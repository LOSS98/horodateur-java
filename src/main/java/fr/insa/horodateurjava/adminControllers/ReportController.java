package fr.insa.horodateurjava.adminControllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.insa.horodateurjava.database.dao.ParkingDAO;
import fr.insa.horodateurjava.database.dao.ReportDAO;
import fr.insa.horodateurjava.database.dao.ReservationDAO;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import fr.insa.horodateurjava.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReportController {
    @FXML
    private DatePicker dateDebutField;
    @FXML
    private DatePicker dateFinField;
    @FXML
    private TextField hhdebutField;
    @FXML
    private TextField mmdebutField;
    @FXML
    private TextField hhfinField;
    @FXML
    private TextField mmfinField;

    @FXML
    private void handleGenerateReportAction() {
        try {

            // Validation des champs de date et heure
            if (dateDebutField.getValue() == null || dateFinField.getValue() == null ||
                    hhdebutField.getText().isEmpty() || mmdebutField.getText().isEmpty() ||
                    hhfinField.getText().isEmpty() || mmfinField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
                return;
            }
            LocalDateTime now = LocalDateTime.now();

            // Conversion des dates et heures en LocalDateTime
            String dateDebut = dateDebutField.getValue().toString();
            String heureDebut = hhdebutField.getText() + ":" + mmdebutField.getText() + ":00";
            String dateHeureDebut = dateDebut + " " + heureDebut;

            String dateFin = dateFinField.getValue().toString();
            String heureFin = hhfinField.getText() + ":" + mmfinField.getText() + ":00";
            String dateHeureFin = dateFin + " " + heureFin;

            // Configurer le sélecteur de fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder le rapport");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);
            if (file == null) return;

            // Création du document PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Ajouter un titre
            Paragraph title = new Paragraph("Rapport des Réservations",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Ligne vide
            document.add(new Paragraph("Plage horaire : " + dateHeureDebut + " à " + dateHeureFin));
            document.add(new Paragraph("Généré le : " + now));
            document.add(new Paragraph("Généré par : " + Session.getInstance().getLname()+" "+Session.getInstance().getFname()));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Récupération des données via DAO
            ReservationDAO reservationDAO = new ReservationDAO(DatabaseHandler.getConnection());
            ParkingDAO parkingDAO = new ParkingDAO(DatabaseHandler.getConnection());

            List<Map<String, Object>> reservations = reservationDAO.getReservationsBetween(dateHeureDebut, dateHeureFin);

            // Ajouter une table
            PdfPTable table = new PdfPTable(6); // Nombre de colonnes
            table.setWidthPercentage(100);
            addTableHeader(table, "Numéro", "Immatriculation", "Numéro de place", "Début", "Fin", "Prix");

            // Remplir la table avec les données
            for (Map<String, Object> reservation : reservations) {
                table.addCell(String.valueOf(reservation.get("numeroReservation")));
                table.addCell((String) reservation.get("immatriculation"));
                table.addCell(String.valueOf(reservation.get("numeroPlace")));
                table.addCell((String) reservation.get("dateHeureDebut"));
                table.addCell((String) reservation.get("dateHeureFin"));
                table.addCell(String.format("%.2f €", reservation.get("prix")));
            }

            // Ajouter la table au document
            document.add(table);

            // Calculer les statistiques
            int totalPlaces = parkingDAO.getTotalPlacesCount();
            int occupiedPlaces = reservationDAO.getDistinctPlacesCountBetween(dateHeureDebut, dateHeureFin);
            double totalRevenue = reservationDAO.getTotalRevenue();
            int totalReservations = reservationDAO.getTotalReservations();

            double occupationRate = (totalPlaces > 0) ? ((double) occupiedPlaces / totalPlaces) * 100 : 0;

            // Ajouter une section pour les statistiques
            document.add(new Paragraph(" "));
            Paragraph statsTitle = new Paragraph("Statistiques :", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK));
            statsTitle.setAlignment(Element.ALIGN_LEFT);
            document.add(statsTitle);
            document.add(new Paragraph("Taux d'occupation : " + String.format("%.2f", occupationRate) + " %"));
            document.add(new Paragraph("Total des recettes : " + String.format("%.2f", totalRevenue) + " €"));
            document.add(new Paragraph("Nombre total de réservations : " + totalReservations));

            // Fermer le document
            document.close();
            String emailAdmin = Session.getInstance().getUserEmail();
            Connection connection = DatabaseHandler.getConnection();
            ReportDAO rapportDAO = new ReportDAO(connection);
            rapportDAO.insertRapport(occupationRate, totalRevenue, emailAdmin, dateHeureDebut, dateHeureFin);
            // Message de confirmation
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Rapport généré avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de générer le rapport : " + e.getMessage());
        }
    }



    private String parseTimestamp(String timestamp) {
        try {
            // Parser le format actuel et le convertir au format souhaité
            java.time.format.DateTimeFormatter inputFormatter = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            java.time.format.DateTimeFormatter outputFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return java.time.LocalDateTime.parse(timestamp, inputFormatter).format(outputFormatter);
        } catch (Exception e) {
            return "Format invalide"; // Gérer les cas où le timestamp est incorrect
        }
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setPhrase(new Phrase(header));
            table.addCell(headerCell);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
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

    public void onViewReports(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/list-reports-view.fxml"));
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
