package fr.insa.horodateurjava.models;

import java.time.LocalDateTime;

public class Reservation {

    private int numeroReservation;
    private String immatriculation;
    private int numeroPlace;
    private LocalDateTime dateHeureDebut;
    private LocalDateTime dateHeureFin;
    private int idParking;
    private double prix;

    // Constructeur par défaut
    public Reservation() {
    }

    // Constructeur avec tous les champs
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

    public Reservation(int i, String immatriculation, int numero, LocalDateTime now, LocalDateTime endDateTime) {
        this.numeroReservation = i;
        this.immatriculation = immatriculation;
        this.numeroPlace = numero;
        this.dateHeureDebut = now;
        this.dateHeureFin = endDateTime;
    }
    public Reservation(int i, String immatriculation, int numero, LocalDateTime now, LocalDateTime endDateTime, double prix) {
        this.numeroReservation = i;
        this.immatriculation = immatriculation;
        this.numeroPlace = numero;
        this.dateHeureDebut = now;
        this.dateHeureFin = endDateTime;
        this.prix = prix;
    }

    // Getters et Setters
    public int getNumeroReservation() {
        return numeroReservation;
    }

    public void setNumeroReservation(int numeroReservation) {
        this.numeroReservation = numeroReservation;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public int getNumeroPlace() {
        return numeroPlace;
    }

    public void setNumeroPlace(int numeroPlace) {
        this.numeroPlace = numeroPlace;
    }

    public LocalDateTime getDateHeureDebut() {
        return dateHeureDebut;
    }

    public void setDateHeureDebut(LocalDateTime dateHeureDebut) {
        this.dateHeureDebut = dateHeureDebut;
    }

    public LocalDateTime getDateHeureFin() {
        return dateHeureFin;
    }

    public void setDateHeureFin(LocalDateTime dateHeureFin) {
        this.dateHeureFin = dateHeureFin;
    }

    public int getIdParking() {
        return idParking;
    }

    public void setIdParking(int idParking) {
        this.idParking = idParking;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    // Méthode toString
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
