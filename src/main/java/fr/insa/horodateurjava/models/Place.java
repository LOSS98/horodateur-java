package fr.insa.horodateurjava.models;

/**
 * Classe abstraite représentant une place dans un parking.
 * Cette classe sert de base pour différents types de places (classiques, handicapés, recharge électrique, etc.).
 */
public abstract class Place {

    // Attributs communs à toutes les places
    private int numero;
    private int idParking;
    private String type;
    private boolean disponibilite;
    private boolean enTravaux;
    private int etage;
    private double tarifHoraire;

    /**
     * Constructeur principal pour la classe Place.
     *
     * @param numero         Numéro de la place.
     * @param etage          Étage où se trouve la place.
     * @param type           Type de la place (classique, handicapé, etc.).
     * @param disponibilite  Disponibilité de la place (true = disponible).
     * @param tarifHoraire   Tarif horaire pour l'utilisation de la place.
     * @param enTravaux      Indique si la place est en travaux (true = en travaux).
     * @param idParking      Identifiant du parking auquel appartient la place.
     */
    public Place(int numero, int etage, String type, boolean disponibilite, double tarifHoraire, boolean enTravaux, int idParking) {
        this.numero = numero;
        this.etage = etage;
        this.type = type;
        this.disponibilite = disponibilite;
        this.tarifHoraire = tarifHoraire;
        this.enTravaux = enTravaux;
        this.idParking = idParking;
    }

    /**
     * Surcharge du constructeur pour inclure des attributs spécifiques comme la puissance de charge.
     *
     * @param numero          Numéro de la place.
     * @param etage           Étage où se trouve la place.
     * @param type            Type de la place (classique, recharge électrique, etc.).
     * @param disponibilite   Disponibilité de la place (true = disponible).
     * @param tarifHoraire    Tarif horaire pour l'utilisation de la place.
     * @param puissanceCharge Puissance de charge (utilisée pour les places électriques).
     * @param enTravaux       Indique si la place est en travaux (true = en travaux).
     * @param idParking       Identifiant du parking auquel appartient la place.
     */
    public Place(int numero, int etage, String type, boolean disponibilite, double tarifHoraire, double puissanceCharge, boolean enTravaux, int idParking) {
        this(numero, etage, type, disponibilite, tarifHoraire, enTravaux, idParking);
    }

    // Getters et Setters

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

    public int getIdParking() {
        return idParking;
    }

    public void setParking(int idParking) {
        this.idParking = idParking;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTarifHoraire() {
        return tarifHoraire;
    }

    public void setTarifHoraire(double tarifHoraire) {
        this.tarifHoraire = tarifHoraire;
    }
}
