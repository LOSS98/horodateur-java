package fr.insa.horodateurjava.database.models;

import java.time.LocalDateTime;

public class Rapport {
    private int idRapport;
    private float tauxOccupation;
    private String heuresDePointe; // JSON String
    private LocalDateTime dateGeneration;
    private int idAdmin;

    // Constructeur
    public Rapport(int idRapport, float tauxOccupation, String heuresDePointe, LocalDateTime dateGeneration, int idAdmin) {
        this.idRapport = idRapport;
        this.tauxOccupation = tauxOccupation;
        this.heuresDePointe = heuresDePointe;
        this.dateGeneration = dateGeneration;
        this.idAdmin = idAdmin;
    }

    // Getters et Setters
    public int getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(int idRapport) {
        this.idRapport = idRapport;
    }

    public float getTauxOccupation() {
        return tauxOccupation;
    }

    public void setTauxOccupation(float tauxOccupation) {
        this.tauxOccupation = tauxOccupation;
    }

    public String getHeuresDePointe() {
        return heuresDePointe;
    }

    public void setHeuresDePointe(String heuresDePointe) {
        this.heuresDePointe = heuresDePointe;
    }

    public LocalDateTime getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(LocalDateTime dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
}