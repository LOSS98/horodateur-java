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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour la génération de rapports PDF sur les réservations et les statistiques.
 */
public class ReportController {

    @FXML
    private DatePicker dateDebutField; // Champ pour sélectionner la date de début

    @FXML
    private DatePicker dateFinField; // Champ pour sélectionner la date de fin

    @FXML
    private TextField hhdebutField; // Champ pour l'heure de début (heures)

    @FXML
    private TextField mmdebutField; // Champ pour l'heure de début (minutes)

    @FXML
    private TextField hhfinField; // Champ pour l'heure de fin (heures)

    @FXML
    private TextField mmfinField; // Champ pour l'heure de fin (minutes)

    /**
     * Gère l'action de génération de rapport.
     */
    @FXML
    private void handleGenerateReportAction() {
        try {
            // Validation des champs de saisie
            if (dateDebutField.getValue() == null || dateFinField.getValue() == null ||
                    hhdebutField.getText().isEmpty() || mmdebutField.getText().isEmpty() ||
                    hhfinField.getText().isEmpty() || mmfinField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            // Construction des plages horaires
            String dateDebut = dateDebutField.getValue().toString();
            String heureDebut = hhdebutField.getText() + ":" + mmdebutField.getText() + ":00";
            String dateHeureDebut = dateDebut + " " + heureDebut;

            String dateFin = dateFinField.getValue().toString();
            String heureFin = hhfinField.getText() + ":" + mmfinField.getText() + ":00";
            String dateHeureFin = dateFin + " " + heureFin;

            // Configuration du sélecteur de fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder le rapport");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);
            if (file == null) return;

            // Création du document PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Ajout des informations de base au rapport
            LocalDateTime now = LocalDateTime.now();
            document.add(new Paragraph("Rapport des Réservations",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE)));
            document.add(new Paragraph("Plage horaire : " + dateHeureDebut + " à " + dateHeureFin));
            document.add(new Paragraph("Généré le : " + now));
            document.add(new Paragraph("Généré par : " + Session.getInstance().getLname() + " " + Session.getInstance().getFname()));
            document.add(new Paragraph(" "));

            // Récupération des données depuis la base de données
            ReservationDAO reservationDAO = new ReservationDAO(DatabaseHandler.getConnection());
            ParkingDAO parkingDAO = new ParkingDAO(DatabaseHandler.getConnection());
            List<Map<String, Object>> reservations = reservationDAO.getReservationsBetween(dateHeureDebut, dateHeureFin);

            // Ajout de la table des réservations au rapport
            PdfPTable table = new PdfPTable(6); // Table avec 6 colonnes
            table.setWidthPercentage(100);
            addTableHeader(table, "Numéro", "Immatriculation", "Numéro de place", "Début", "Fin", "Prix");
            for (Map<String, Object> reservation : reservations) {
                table.addCell(String.valueOf(reservation.get("numeroReservation")));
                table.addCell((String) reservation.get("immatriculation"));
                table.addCell(String.valueOf(reservation.get("numeroPlace")));
                table.addCell((String) reservation.get("dateHeureDebut"));
                table.addCell((String) reservation.get("dateHeureFin"));
                table.addCell(String.format("%.2f €", reservation.get("prix")));
            }
            document.add(table);

            // Calcul des statistiques et ajout au rapport
            int totalPlaces = parkingDAO.getTotalPlacesCount();
            int occupiedPlaces = reservationDAO.getDistinctPlacesCountBetween(dateHeureDebut, dateHeureFin);
            double totalRevenue = reservationDAO.getTotalRevenue();
            int totalReservations = reservationDAO.getTotalReservations();
            double occupationRate = (totalPlaces > 0) ? ((double) occupiedPlaces / totalPlaces) * 100 : 0;

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Statistiques :", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            document.add(new Paragraph("Taux d'occupation : " + String.format("%.2f", occupationRate) + " %"));
            document.add(new Paragraph("Total des recettes : " + String.format("%.2f", totalRevenue) + " €"));
            document.add(new Paragraph("Nombre total de réservations : " + totalReservations));

            // Enregistrer le rapport dans la base de données
            String emailAdmin = Session.getInstance().getUserEmail();
            ReportDAO reportDAO = new ReportDAO(DatabaseHandler.getConnection());
            reportDAO.insertRapport(occupationRate, totalRevenue, emailAdmin, dateHeureDebut, dateHeureFin);

            document.close();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Rapport généré avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de générer le rapport : " + e.getMessage());
        }
    }

    /**
     * Ajoute des en-têtes à une table PDF.
     */
    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setPhrase(new Phrase(header));
            table.addCell(headerCell);
        }
    }

    /**
     * Affiche une alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retourne à la page d'accueil admin.
     */
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

    /**
     * Navigue vers la page des rapports.
     */
    public void onViewReports(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/list-reports-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Rapports");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
