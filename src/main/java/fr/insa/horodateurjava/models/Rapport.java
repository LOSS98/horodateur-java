package fr.insa.horodateurjava.models;

import java.time.LocalDateTime;

public class Rapport {
    private int idRapport;
    private double tauxOccupation;
    private LocalDateTime dateGeneration;
    private String emailAdmin;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private double totalRecettes;


    // Constructeur
    public Rapport(int idRapport, double tauxOccupation, LocalDateTime dateGeneration, String emailAdmin) {
        this.idRapport = idRapport;
        this.tauxOccupation = tauxOccupation;
        this.dateGeneration = dateGeneration;
        this.emailAdmin = emailAdmin;
    }
    public Rapport(int idRapport, double tauxOccupation,double totalRecettes, LocalDateTime dateGeneration, String emailAdmin, LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.idRapport = idRapport;
        this.tauxOccupation = tauxOccupation;
        this.dateGeneration = dateGeneration;
        this.emailAdmin = emailAdmin;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.totalRecettes = totalRecettes;
    }

    // Getters et Setters
    public int getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(int idRapport) {
        this.idRapport = idRapport;
    }

    public double getTauxOccupation() {
        return tauxOccupation;
    }

    public void setTauxOccupation(double tauxOccupation) {
        this.tauxOccupation = tauxOccupation;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public double getTotalRecettes() {
        return totalRecettes;
    }
    public void setTotalRecettes(double totalRecettes) {
        this.totalRecettes = totalRecettes;
    }


    public LocalDateTime getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(LocalDateTime dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }
}