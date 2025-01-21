package fr.insa.horodateurjava.models;

/**
 * Représente un administrateur avec ses informations personnelles et ses identifiants.
 */
public class Administrateur {

    // Attributs privés
    private int idAdmin;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;

    /**
     * Constructeur complet avec tous les attributs.
     *
     * @param idAdmin    L'identifiant unique de l'administrateur.
     * @param nom        Le nom de l'administrateur.
     * @param prenom     Le prénom de l'administrateur.
     * @param email      L'email de l'administrateur.
     * @param motDePasse Le mot de passe de l'administrateur.
     */
    public Administrateur(int idAdmin, String nom, String prenom, String email, String motDePasse) {
        this.idAdmin = idAdmin;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    /**
     * Constructeur sans mot de passe, utilisé pour des scénarios où le mot de passe est masqué.
     *
     * @param idAdmin L'identifiant unique de l'administrateur.
     * @param nom     Le nom de l'administrateur.
     * @param prenom  Le prénom de l'administrateur.
     * @param email   L'email de l'administrateur.
     */
    public Administrateur(int idAdmin, String nom, String prenom, String email) {
        this.idAdmin = idAdmin;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    /**
     * Constructeur utilisé lors de la création d'un nouvel administrateur (sans ID).
     *
     * @param nom        Le nom de l'administrateur.
     * @param prenom     Le prénom de l'administrateur.
     * @param email      L'email de l'administrateur.
     * @param motDePasse Le mot de passe de l'administrateur.
     */
    public Administrateur(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // Getters et setters

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    @Override
    public String toString() {
        return "Administrateur{" +
                "idAdmin=" + idAdmin +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
