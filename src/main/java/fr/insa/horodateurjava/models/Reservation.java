package fr.insa.horodateurjava.models;

import java.time.LocalDateTime;

/**
 * Classe représentant une réservation de place de parking.
 * Une réservation inclut des informations sur le véhicule, la place réservée,
 * la durée de la réservation et le coût associé.
 */
public class Reservation {

    // Identifiant unique de la réservation
    private int numeroReservation;

    // Immatriculation du véhicule ayant réservé la place
    private String immatriculation;

    // Numéro de la place réservée
    private int numeroPlace;

    // Date et heure de début de la réservation
    private LocalDateTime dateHeureDebut;

    // Date et heure de fin de la réservation
    private LocalDateTime dateHeureFin;

    // Identifiant du parking où la place est située
    private int idParking;

    // Prix total de la réservation
    private double prix;

    /**
     * Constructeur par défaut
     */
    public Reservation() {
    }

    /**
     * Constructeur avec tous les champs.
     *
     * @param numeroReservation Identifiant unique de la réservation.
     * @param immatriculation   Immatriculation du véhicule.
     * @param numeroPlace       Numéro de la place réservée.
     * @param dateHeureDebut    Date et heure de début de la réservation.
     * @param dateHeureFin      Date et heure de fin de la réservation.
     * @param idParking         Identifiant du parking.
     * @param prix              Prix total de la réservation.
     */
    public Reservation(int numeroReservation, String immatriculation, int numeroPlace,
                       LocalDateTime dateHeureDebut, LocalDateTime dateHeureFin,
                       int idParking, double prix) {
        this.numeroReservation = numeroReservation;
        this.immatriculation = immatriculation;
        this.numeroPlace = numeroPlace;
        this.dateHeureDebut = dateHeureDebut;
        this.dateHeureFin = dateHeureFin;
        this.idParking = idParking;
        this.prix = prix;
    }

    /**
     * Constructeur simplifié pour des scénarios spécifiques.
     *
     * @param i                Identifiant de la réservation.
     * @param immatriculation  Immatriculation du véhicule.
     * @param numero           Numéro de la place.
     * @param now              Date et heure de début.
     * @param endDateTime      Date et heure de fin.
     */
    public Reservation(int i, String immatriculation, int numero, LocalDateTime now, LocalDateTime endDateTime) {
        this.numeroReservation = i;
        this.immatriculation = immatriculation;
        this.numeroPlace = numero;
        this.dateHeureDebut = now;
        this.dateHeureFin = endDateTime;
    }

    /**
     * Constructeur simplifié avec ajout du prix.
     *
     * @param i                Identifiant de la réservation.
     * @param immatriculation  Immatriculation du véhicule.
     * @param numero           Numéro de la place.
     * @param now              Date et heure de début.
     * @param endDateTime      Date et heure de fin.
     * @param prix             Prix total de la réservation.
     */
    public Reservation(int i, String immatriculation, int numero, LocalDateTime now, LocalDateTime endDateTime, double prix) {
        this.numeroReservation = i;
        this.immatriculation = immatriculation;
        this.numeroPlace = numero;
        this.dateHeureDebut = now;
        this.dateHeureFin = endDateTime;
        this.prix = prix;
    }

    // Getters et Setters

    /**
     * @return Identifiant unique de la réservation.
     */
    public int getNumeroReservation() {
        return numeroReservation;
    }

    public void setNumeroReservation(int numeroReservation) {
        this.numeroReservation = numeroReservation;
    }

    /**
     * @return Immatriculation du véhicule ayant réservé la place.
     */
    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    /**
     * @return Numéro de la place réservée.
     */
    public int getNumeroPlace() {
        return numeroPlace;
    }

    public void setNumeroPlace(int numeroPlace) {
        this.numeroPlace = numeroPlace;
    }

    /**
     * @return Date et heure de début de la réservation.
     */
    public LocalDateTime getDateHeureDebut() {
        return dateHeureDebut;
    }

    public void setDateHeureDebut(LocalDateTime dateHeureDebut) {
        this.dateHeureDebut = dateHeureDebut;
    }

    /**
     * @return Date et heure de fin de la réservation.
     */
    public LocalDateTime getDateHeureFin() {
        return dateHeureFin;
    }

    public void setDateHeureFin(LocalDateTime dateHeureFin) {
        this.dateHeureFin = dateHeureFin;
    }

    /**
     * @return Identifiant du parking où la place est située.
     */
    public int getIdParking() {
        return idParking;
    }

    public void setIdParking(int idParking) {
        this.idParking = idParking;
    }

    /**
     * @return Prix total de la réservation.
     */
    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    /**
     * Retourne une représentation textuelle de l'objet.
     *
     * @return Représentation textuelle de la réservation.
     */
    @Override
    public String toString() {
        return "Reservation{" +
                "numeroReservation=" + numeroReservation +
                ", immatriculation='" + immatriculation + '\'' +
                ", numeroPlace=" + numeroPlace +
                ", dateHeureDebut=" + dateHeureDebut +
                ", dateHeureFin=" + dateHeureFin +
                ", idParking=" + idParking +
                ", prix=" + prix +
                '}';
    }
}
