package fr.insa.horodateurjava.models;

/**
 * Représente un parking avec ses caractéristiques principales.
 */
public class Parking {

    // Attributs privés
    private int idParking;
    private String nomDuParking;
    private String adresseDuParking;
    private int nombrePlaces;
    private int placesDeclarees;

    /**
     * Constructeur sans paramètre.
     * Obligatoire pour certaines bibliothèques comme JavaFX.
     */
    public Parking() {
    }

    /**
     * Constructeur pour un parking avec ses détails principaux.
     *
     * @param idParking       L'identifiant unique du parking.
     * @param nomDuParking    Le nom du parking.
     * @param adresseDuParking L'adresse du parking.
     * @param nombrePlaces    La capacité totale du parking.
     */
    public Parking(int idParking, String nomDuParking, String adresseDuParking, int nombrePlaces) {
        this.idParking = idParking;
        this.nomDuParking = nomDuParking;
        this.adresseDuParking = adresseDuParking;
        this.nombrePlaces = nombrePlaces;
    }

    /**
     * Constructeur pour un parking avec des places déclarées en plus.
     *
     * @param idParking       L'identifiant unique du parking.
     * @param nom             Le nom du parking.
     * @param adresse         L'adresse du parking.
     * @param capacite        La capacité totale du parking.
     * @param placesDeclarees Le nombre de places déclarées.
     */
    public Parking(int idParking, String nom, String adresse, int capacite, int placesDeclarees) {
        this(idParking, nom, adresse, capacite);
        this.placesDeclarees = placesDeclarees;
    }

    // Getters et setters

    public int getIdParking() {
        return idParking;
    }

    public void setIdParking(int idParking) {
        this.idParking = idParking;
    }

    public String getNomDuParking() {
        return nomDuParking;
    }

    public void setNomDuParking(String nomDuParking) {
        this.nomDuParking = nomDuParking;
    }

    public String getAdresseDuParking() {
        return adresseDuParking;
    }

    public void setAdresseDuParking(String adresseDuParking) {
        this.adresseDuParking = adresseDuParking;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public int getPlacesDeclarees() {
        return placesDeclarees;
    }

    public void setPlacesDeclarees(int placesDeclarees) {
        this.placesDeclarees = placesDeclarees;
    }

    /**
     * Fournit une représentation textuelle du parking.
     *
     * @return Une chaîne de caractères représentant les informations principales du parking.
     */
    @Override
    public String toString() {
        return "Parking{" +
                "idParking=" + idParking +
                ", nomDuParking='" + nomDuParking + '\'' +
                ", adresseDuParking='" + adresseDuParking + '\'' +
                ", nombrePlaces=" + nombrePlaces +
                ", placesDeclarees=" + placesDeclarees +
                '}';
    }
}
