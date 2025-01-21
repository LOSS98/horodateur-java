package fr.insa.horodateurjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale de l'application JavaFX pour la gestion des places de parking.
 * Cette classe démarre l'application et charge la vue d'accueil définie dans un fichier FXML.
 */
public class HomeApplication extends Application {

    /**
     * Point d'entrée principal pour les applications JavaFX.
     * Cette méthode est automatiquement appelée après le lancement de l'application.
     *
     * @param stage La fenêtre principale (stage) où l'interface utilisateur sera affichée.
     * @throws Exception Si une erreur survient lors du chargement de la vue FXML.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Chargement de la vue d'accueil à partir du fichier FXML
        FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource("views/home-view.fxml"));

        // Création d'une scène à partir de la vue chargée
        Scene scene = new Scene(fxmlLoader.load());

        // Définir le titre de la fenêtre
        stage.setTitle("Accueil - Gestion des Places de Parking");

        // Attacher la scène au stage (fenêtre principale)
        stage.setScene(scene);

        // Afficher la fenêtre
        stage.show();
    }

    /**
     * Méthode main pour lancer l'application JavaFX.
     *
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        // Lancer l'application
        launch();
    }
}
