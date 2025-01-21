package fr.insa.horodateurjava.adminControllers;

import fr.insa.horodateurjava.database.dao.ParkingDAO;
import fr.insa.horodateurjava.database.dao.PlaceDAO;
import fr.insa.horodateurjava.models.*;
import fr.insa.horodateurjava.utils.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Contrôleur pour la gestion des places de parking.
 * Gère l'ajout, la suppression, la mise à jour des places et leur liaison avec un parking spécifique.
 */
public class PlaceController {

    @FXML
    private ComboBox<String> parkingComboBox; // Liste déroulante des parkings disponibles

    @FXML
    private TextField numeroField; // Champ pour le numéro de la place

    @FXML
    private TextField etageField; // Champ pour l'étage

    @FXML
    private ComboBox<String> typeComboBox; // Liste déroulante pour sélectionner le type de place

    @FXML
    private TextField tarifHoraireField; // Champ pour le tarif horaire

    @FXML
    private TextField puissanceChargeField; // Champ pour la puissance de charge (si applicable)

    @FXML
    private ComboBox<String> enTravauxComboBox; // Liste déroulante pour indiquer si la place est en travaux

    @FXML
    private Label messageLabel; // Label pour afficher les messages d'erreur ou de succès

    /**
     * Initialise les données et configure les listes déroulantes.
     */
    public void initialize() {
        loadParkingNames();
        typeComboBox.getItems().addAll("Classique", "DeuxRoues", "Famille", "Handicape", "RechargeElectrique");
        enTravauxComboBox.getItems().addAll("Oui", "Non");
    }

    /**
     * Charge les noms des parkings dans la liste déroulante.
     */
    private void loadParkingNames() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            ParkingDAO parkingDAO = new ParkingDAO(connection);
            List<String> parkingNames = parkingDAO.getAllParkingNamesWithIds();

            parkingComboBox.getItems().clear();
            parkingComboBox.getItems().addAll(parkingNames);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les parkings.");
        }
    }

    /**
     * Gère l'ajout d'une nouvelle place.
     */
    @FXML
    public void handleAddPlace() {
        try {
            String parkingOption = parkingComboBox.getValue();
            String numeroText = numeroField.getText().trim();
            String etageText = etageField.getText().trim();
            String type = typeComboBox.getValue();
            String tarifText = tarifHoraireField.getText().trim();
            String puissanceText = puissanceChargeField.getText().trim();
            String enTravauxOption = enTravauxComboBox.getValue();

            // Validation des champs
            if (parkingOption == null || numeroText.isEmpty() || etageText.isEmpty() || type == null || tarifText.isEmpty()) {
                messageLabel.setText("Erreur : Tous les champs obligatoires doivent être remplis.");
                return;
            }

            int parkingId = Integer.parseInt(parkingOption.split(" - ")[0]);
            int numero = Integer.parseInt(numeroText);
            int etage = Integer.parseInt(etageText);
            double tarifHoraire = Double.parseDouble(tarifText);
            double puissanceCharge = "RechargeElectrique".equals(type) ? Double.parseDouble(puissanceText) : 0.0;
            boolean enTravaux = "Oui".equals(enTravauxOption);

            try (Connection connection = DatabaseHandler.getConnection()) {
                PlaceDAO placeDAO = new PlaceDAO(connection);

                if (placeDAO.existsByNumeroAndIdParking(numero, parkingId)) {
                    messageLabel.setText("Erreur : Une place avec ce numéro existe déjà.");
                    return;
                }

                Place place = createPlaceByType(type, numero, etage, tarifHoraire, puissanceCharge, enTravaux, parkingId);
                placeDAO.addPlace(place);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "La place a été ajoutée avec succès !");
                clearFields();
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Erreur : Certains champs doivent contenir des nombres valides.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout de la place.");
        }
    }

    /**
     * Gère la suppression d'une place.
     */
    @FXML
    private void handleDeletePlace() {
        try {
            String parkingOption = parkingComboBox.getValue();
            String numeroText = numeroField.getText().trim();

            if (parkingOption == null || numeroText.isEmpty()) {
                messageLabel.setText("Erreur : Veuillez fournir le parking et le numéro de la place.");
                return;
            }

            int parkingId = Integer.parseInt(parkingOption.split(" - ")[0]);
            int numero = Integer.parseInt(numeroText);

            try (Connection connection = DatabaseHandler.getConnection()) {
                PlaceDAO placeDAO = new PlaceDAO(connection);

                if (!placeDAO.existsByNumeroAndIdParking(numero, parkingId)) {
                    messageLabel.setText("Erreur : Aucun emplacement trouvé avec ce numéro et parking.");
                    return;
                }

                placeDAO.deletePlaceByNumeroAndIdParking(numero, parkingId);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La place a été supprimée avec succès !");
                clearFields();
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Erreur : Veuillez fournir des nombres valides.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la suppression de la place.");
        }
    }

    /**
     * Gère la mise à jour d'une place existante.
     */
    @FXML
    public void handleUpdatePlace() {
        try {
            String parkingOption = parkingComboBox.getValue();
            String numeroText = numeroField.getText().trim();

            if (parkingOption == null || numeroText.isEmpty()) {
                messageLabel.setText("Erreur : Parking et numéro de place sont requis.");
                return;
            }

            int parkingId = Integer.parseInt(parkingOption.split(" - ")[0]);
            int numero = Integer.parseInt(numeroText);

            try (Connection connection = DatabaseHandler.getConnection()) {
                PlaceDAO placeDAO = new PlaceDAO(connection);

                if (!placeDAO.existsByNumeroAndIdParking(numero, parkingId)) {
                    messageLabel.setText("Erreur : Aucune place trouvée avec ce numéro.");
                    return;
                }

                // Récupérer les champs pour mise à jour
                Integer etage = etageField.getText().isEmpty() ? null : Integer.parseInt(etageField.getText());
                Double tarif = tarifHoraireField.getText().isEmpty() ? null : Double.parseDouble(tarifHoraireField.getText());
                Double puissance = puissanceChargeField.getText().isEmpty() ? null : Double.parseDouble(puissanceChargeField.getText());
                Boolean enTravaux = "Oui".equals(enTravauxComboBox.getValue());

                placeDAO.updatePlace(parkingId, numero, etage, typeComboBox.getValue(), tarif, puissance, enTravaux);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "La place a été mise à jour !");
                clearFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la mise à jour.");
        }
    }

    /**
     * Crée un objet Place en fonction du type sélectionné.
     */
    private Place createPlaceByType(String type, int numero, int etage, double tarif, double puissance, boolean enTravaux, int parkingId) {
        return switch (type) {
            case "Classique" -> new PlaceClassique(numero, etage, tarif, enTravaux, parkingId);
            case "DeuxRoues" -> new PlaceDeuxRoues(numero, etage, tarif, enTravaux, parkingId);
            case "Famille" -> new PlaceFamille(numero, etage, tarif, enTravaux, parkingId);
            case "Handicape" -> new PlaceHandicape(numero, etage, tarif, enTravaux, parkingId);
            case "RechargeElectrique" -> new PlaceRechargeElectrique(numero, etage, tarif, puissance, enTravaux, parkingId);
            default -> throw new IllegalArgumentException("Type inconnu : " + type);
        };
    }

    /**
     * Retourne à la page d'accueil administrateur.
     */
    @FXML
    private void handleBackLinkAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Accueil Admin");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à l'accueil.");
        }
    }

    /**
     * Réinitialise les champs du formulaire.
     */
    private void clearFields() {
        parkingComboBox.setValue(null);
        numeroField.clear();
        etageField.clear();
        typeComboBox.setValue(null);
        tarifHoraireField.clear();
        puissanceChargeField.clear();
        enTravauxComboBox.setValue(null);
        messageLabel.setText("");
    }

    /**
     * Affiche une alerte à l'utilisateur.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
