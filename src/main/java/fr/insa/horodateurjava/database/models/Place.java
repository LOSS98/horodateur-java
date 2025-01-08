package fr.insa.horodateurjava.database.models;

public abstract class Place {
    private int numero;
    private int idParking;
    private String type;
    private boolean disponibilite;
    private boolean enTravaux;
    private int etage;
    private double tarifHoraire;

    // Getters et Setters communs
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    public boolean isEnTravaux() {
        return enTravaux;
    }

    public void setEnTravaux(boolean enTravaux) {
        this.enTravaux = enTravaux;
    }

    public int getEtage() {
        return etage;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public void setParking(int idParking) {
        this.idParking = idParking;
    }

    public int getIdParking() {
        return idParking;
    }

    public void setType(String type) {
        this.type = type;
    }
//
//    public String getType() {
//        return type;
//    }



    public double getTarifHoraire() {
        return tarifHoraire;
    }

    public void setTarifHoraire(double tarifHoraire) {
        this.tarifHoraire = tarifHoraire;
    }

    // Méthode abstraite pour différencier les sous-classes
    public abstract String getType();
}
