package fr.insa.horodateurjava.models;

public class Parking {

    private int idParking;
    private String nomDuParking;
    private String adresseDuParking;
    private int nombrePlaces;
    private int placesDeclarees;

    // Constructeur sans paramètre (obligatoire pour certaines bibliothèques comme JavaFX)
    public Parking() {
    }

    // Constructeur avec paramètres
    public Parking(int idParking, String nomDuParking, String adresseDuParking, int nombrePlaces) {
        this.idParking = idParking;
        this.nomDuParking = nomDuParking;
        this.adresseDuParking = adresseDuParking;
        this.nombrePlaces = nombrePlaces;
    }

    public Parking(int idParking, String nom, String adresse, int capacite, int placesDeclarees) {
        this(idParking, nom, adresse, capacite);
        this.placesDeclarees = placesDeclarees;
    }

    // Getters et Setters

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
    @Override
    public String toString() {
        return "Parking{" +
                "idParking=" + idParking +
                ", nomDuParking='" + nomDuParking + '\'' +
                ", adresseDuParking='" + adresseDuParking + '\'' +
                ", nombrePlaces=" + nombrePlaces +
                '}';
    }
}
