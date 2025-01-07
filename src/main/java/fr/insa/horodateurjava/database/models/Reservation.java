package fr.insa.horodateurjava.database.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int numeroReservation;
    private String immatriculation;
    @ManyToOne
    @JoinColumn(name = "numeroPlace", nullable = false)
    private int numeroPlace;
    private LocalDateTime dateHeureDebut;
    private LocalDateTime dateHeureFin;

    // Constructeur
    public Reservation(int numeroReservation, String immatriculation, int numeroPlace, LocalDateTime dateHeureDebut, LocalDateTime dateHeureFin) {
        this.numeroReservation = numeroReservation;
        this.immatriculation = immatriculation;
        this.numeroPlace = numeroPlace;
        this.dateHeureDebut = dateHeureDebut;
        this.dateHeureFin = dateHeureFin;
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
}
