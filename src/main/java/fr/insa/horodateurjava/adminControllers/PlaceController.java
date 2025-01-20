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
import javafx.scene.control.ComboBox;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class PlaceController {

    @FXML
    private ComboBox<String> parkingComboBox;

    @FXML
    private TextField numeroField;

    @FXML
    private TextField etageField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField tarifHoraireField;

    @FXML
    private TextField puissanceChargeField;

    @FXML
    private ComboBox<String> enTravauxComboBox;

    @FXML
    private Label messageLabel;

    public void initialize() {
        // Charger les parkings
        loadParkingNames();

        // Charger les types de places
        typeComboBox.getItems().addAll("Classique", "DeuxRoues", "Famille", "Handicape", "RechargeElectrique");

        // Charger les options En Travaux
        enTravauxComboBox.getItems().addAll("Oui", "Non");
    }

    private void loadParkingNames() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            ParkingDAO parkingDAO = new ParkingDAO(connection);

            List<String> parkingNames = parkingDAO.getAllParkingNamesWithIds();
            if (parkingNames.isEmpty()) {
                System.out.println("Aucun parking trouvé dans la base de données.");
            } else {
                System.out.println("Parkings chargés");
            }

            parkingComboBox.getItems().clear();
            parkingComboBox.getItems().addAll(parkingNames);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les parkings.");
        }
    }


    @FXML
    public void handleAddPlace() {
        try {
            // Récupérer les données utilisateur
            String parkingOption = parkingComboBox.getValue();
            String numeroText = numeroField.getText().trim();
            String etageText = etageField.getText().trim();
            String type = typeComboBox.getValue();
            String tarifText = tarifHoraireField.getText().trim();
            String puissanceText = puissanceChargeField.getText().trim();
            boolean enTravaux = enTravauxComboBox.equals("Oui");

            // Validation des champs
            if (parkingOption == null || parkingOption.isEmpty()) {
                messageLabel.setText("Erreur : Veuillez sélectionner un parking.");
                return;
            }

            if (numeroText.isEmpty()) {
                messageLabel.setText("Erreur : Veuillez saisir un numéro de place.");
                return;
            }

            if (etageText.isEmpty()) {
                messageLabel.setText("Erreur : Veuillez saisir un étage.");
                return;
            }

            if (type == null || type.isEmpty()) {
                messageLabel.setText("Erreur : Veuillez sélectionner un type de place.");
                return;
            }

            if (tarifText.isEmpty()) {
                messageLabel.setText("Erreur : Veuillez saisir un tarif horaire.");
                return;
            }

            if (type.equals("RechargeElectrique") && (puissanceText.isEmpty() || puissanceText.trim().equals("0"))) {
                messageLabel.setText("Erreur : Veuillez saisir une puissance de charge pour une place Recharge Électrique.");
                return;
            }

            if (enTravauxComboBox == null) {
                messageLabel.setText("Erreur : Veuillez sélectionner une option pour 'En Travaux'.");
                return;
            }



            // Extraire ID du parking
            int parkingId = Integer.parseInt(parkingOption.split(" - ")[0]);
            int etage = Integer.parseInt(etageText);
            double tarifHoraire = Double.parseDouble(tarifText);
            double puissanceCharge = 0.0;
            int numero = Integer.parseInt(numeroField.getText().trim());

            try (Connection connection = DatabaseHandler.getConnection()) {
                ParkingDAO parkingDAO = new ParkingDAO(connection);
                int totalCapacity = parkingDAO.getParkingCapacity(parkingId);
                int currentPlacesCount = parkingDAO.getParkingCurrentPlacesCount(parkingId);
                if (currentPlacesCount >= totalCapacity) {
                    messageLabel.setText("Erreur : Capacité maximale du parking atteinte.");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la vérification de capacité.");
            }

            // Vérifier la puissance de charge si nécessaire
            if ("RechargeElectrique".equals(type)) {
                if (puissanceText.isEmpty()) {
                    messageLabel.setText("Erreur : la puissance de charge est requise pour une place RechargeElectrique.");
                    return;
                }
                puissanceCharge = Double.parseDouble(puissanceText);
                if (puissanceCharge <= 0) {
                    messageLabel.setText("Erreur : la puissance de charge doit être un nombre positif.");
                    return;
                }
            }

            // Créer un objet Place spécifique au typeSS
            Place place;
            switch (type) {
                case "Classique" -> place = new PlaceClassique(numero, etage, tarifHoraire, enTravaux, parkingId);
                case "DeuxRoues" -> place = new PlaceDeuxRoues(numero, etage, tarifHoraire, enTravaux, parkingId);
                case "Famille" -> place = new PlaceFamille(numero, etage, tarifHoraire, enTravaux, parkingId);
                case "Handicape" -> place = new PlaceHandicape(numero, etage, tarifHoraire, enTravaux, parkingId);
                case "RechargeElectrique" -> place = new PlaceRechargeElectrique(numero, etage, tarifHoraire, puissanceCharge, enTravaux, parkingId);
                default -> throw new IllegalArgumentException("Type de place inconnu : " + type);
            }



            // Insérer dans la base de données
            try (Connection connection = DatabaseHandler.getConnection()) {
                PlaceDAO placeDAO = new PlaceDAO(connection);
                if (placeDAO.existsByNumeroAndIdParking(numero, parkingId)) {
                    messageLabel.setText("Erreur : Une place avec ce numéro existe déjà dans ce parking.");
                    return;
                }
                placeDAO.addPlace(place);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "La place a été ajoutée avec succès !");
                clearFields();
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Erreur : certains champs doivent être des nombres valides.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout de la place.");
        }
    }

    @FXML
    private void handleDeletePlace() {
        try {
            String parkingOption = parkingComboBox.getValue();
            String numeroText = numeroField.getText().trim();

            if (parkingOption == null || parkingOption.isEmpty()) {
                messageLabel.setText("Erreur : Veuillez sélectionner un parking.");
                return;
            }

            if (numeroText.isEmpty()) {
                messageLabel.setText("Erreur : Veuillez saisir un numéro de place.");
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
            messageLabel.setText("Erreur : Veuillez saisir des nombres valides.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la suppression de la place.");
        }
    }

    @FXML
    public void handleUpdatePlace() {
        try {
            // Récupérer le parking et le numéro pour identifier la place à mettre à jour
            String parkingOption = parkingComboBox.getValue();
            String numeroText = numeroField.getText().trim();

            if (parkingOption == null || parkingOption.isEmpty()) {
                messageLabel.setText("Erreur : Veuillez sélectionner un parking.");
                return;
            }

            if (numeroText.isEmpty()) {
                messageLabel.setText("Erreur : Veuillez saisir un numéro de place.");
                return;
            }

            int parkingId = Integer.parseInt(parkingOption.split(" - ")[0]);
            int numero = Integer.parseInt(numeroText);

            // Récupérer les autres champs (peuvent être vides)
            String etageText = etageField.getText().trim();
            String type = typeComboBox.getValue();
            String tarifText = tarifHoraireField.getText().trim();
            String puissanceText = puissanceChargeField.getText().trim();
            String enTravauxOption = enTravauxComboBox.getValue();

            // Préparer les données pour la mise à jour
            Integer etage = etageText.isEmpty() ? null : Integer.parseInt(etageText);
            Double tarifHoraire = tarifText.isEmpty() ? null : Double.parseDouble(tarifText);
            Double puissanceCharge = puissanceText.isEmpty() ? null : Double.parseDouble(puissanceText);
            Boolean enTravaux = enTravauxOption == null ? null : enTravauxOption.equals("Oui");

            // Créer une connexion et appeler le DAO
            try (Connection connection = DatabaseHandler.getConnection()) {
                PlaceDAO placeDAO = new PlaceDAO(connection);

                // Vérifier si la place existe
                if (!placeDAO.existsByNumeroAndIdParking(numero, parkingId)) {
                    messageLabel.setText("Erreur : Aucune place trouvée avec ce numéro et ce parking.");
                    return;
                }

                // Mettre à jour la place
                placeDAO.updatePlace(parkingId, numero, etage, type, tarifHoraire, puissanceCharge, enTravaux);

                showAlert(Alert.AlertType.INFORMATION, "Succès", "La place a été mise à jour avec succès !");
                clearFields();
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Erreur : certains champs doivent être des nombres valides.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la mise à jour de la place.");
        }
    }



    @FXML
    private void handleBackLinkAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/insa/horodateurjava/views/admin/admin-home-view.fxml"));
            Parent root = loader.load();

            // Obtenir la fenêtre actuelle
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Charger la nouvelle scène
            currentStage.setScene(scene);
            currentStage.setTitle("Accueil Admin");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à la page d'accueil admin.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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
}
