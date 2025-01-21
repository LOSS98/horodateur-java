package fr.insa.horodateurjava.utils;

/**
 * Classe utilitaire pour gérer une session utilisateur.
 * Implémente un pattern singleton pour assurer qu'une seule instance de session est utilisée.
 */
public class Session {

    // Instance unique de la session
    private static Session instance;

    // Informations de l'utilisateur connecté
    private String userEmail;
    private String fname;
    private String lname;

    /**
     * Constructeur privé pour empêcher la création d'instances directes.
     */
    private Session() {
    }

    /**
     * Méthode pour obtenir l'instance unique de la session.
     * Si aucune instance n'existe, elle est créée.
     *
     * @return L'instance unique de la session.
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Récupère l'email de l'utilisateur connecté.
     *
     * @return L'email de l'utilisateur.
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Définit l'email de l'utilisateur connecté.
     *
     * @param userEmail L'email à définir.
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Récupère le prénom de l'utilisateur connecté.
     *
     * @return Le prénom de l'utilisateur.
     */
    public String getFname() {
        return fname;
    }

    /**
     * Définit le prénom de l'utilisateur connecté.
     *
     * @param fname Le prénom à définir.
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * Récupère le nom de l'utilisateur connecté.
     *
     * @return Le nom de l'utilisateur.
     */
    public String getLname() {
        return lname;
    }

    /**
     * Définit le nom de l'utilisateur connecté.
     *
     * @param lname Le nom à définir.
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * Réinitialise la session en effaçant les informations de l'utilisateur.
     */
    public void clearSession() {
        userEmail = null;
        fname = null;
        lname = null;
    }
}
