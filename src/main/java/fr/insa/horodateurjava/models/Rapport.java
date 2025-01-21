package fr.insa.horodateurjava.models;

import java.time.LocalDateTime;

/**
 * Classe représentant un rapport généré pour l'analyse d'occupation et de revenus d'un parking.
 * Un rapport inclut des informations sur les taux d'occupation, les recettes totales et les plages temporelles analysées.
 */
public class Rapport {

    // Identifiant unique du rapport
    private int idRapport;

    // Taux d'occupation calculé pour la période donnée
    private double tauxOccupation;

    // Date et heure de génération du rapport
    private LocalDateTime dateGeneration;

    // Email de l'administrateur ayant généré le rapport
    private String emailAdmin;

    // Date et heure de début de la période analysée
    private LocalDateTime dateDebut;

    // Date et heure de fin de la période analysée
    private LocalDateTime dateFin;

    // Recettes totales générées durant la période analysée
    private double totalRecettes;

    /**
     * Constructeur pour créer un rapport avec les informations de base.
     *
     * @param idRapport      Identifiant unique du rapport.
     * @param tauxOccupation Taux d'occupation calculé.
     * @param dateGeneration Date et heure de génération du rapport.
     * @param emailAdmin     Email de l'administrateur ayant généré le rapport.
     */
    public Rapport(int idRapport, double tauxOccupation, LocalDateTime dateGeneration, String emailAdmin) {
        this.idRapport = idRapport;
        this.tauxOccupation = tauxOccupation;
        this.dateGeneration = dateGeneration;
        this.emailAdmin = emailAdmin;
    }

    /**
     * Constructeur complet pour créer un rapport avec toutes les informations.
     *
     * @param idRapport      Identifiant unique du rapport.
     * @param tauxOccupation Taux d'occupation calculé.
     * @param totalRecettes  Recettes totales générées durant la période.
     * @param dateGeneration Date et heure de génération du rapport.
     * @param emailAdmin     Email de l'administrateur ayant généré le rapport.
     * @param dateDebut      Date et heure de début de la période analysée.
     * @param dateFin        Date et heure de fin de la période analysée.
     */
    public Rapport(int idRapport, double tauxOccupation, double totalRecettes, LocalDateTime dateGeneration, String emailAdmin, LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.idRapport = idRapport;
        this.tauxOccupation = tauxOccupation;
        this.dateGeneration = dateGeneration;
        this.emailAdmin = emailAdmin;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.totalRecettes = totalRecettes;
    }

    // Getters et Setters

    /**
     * @return Identifiant unique du rapport.
     */
    public int getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(int idRapport) {
        this.idRapport = idRapport;
    }

    /**
     * @return Taux d'occupation calculé pour la période donnée.
     */
    public double getTauxOccupation() {
        return tauxOccupation;
    }

    public void setTauxOccupation(double tauxOccupation) {
        this.tauxOccupation = tauxOccupation;
    }

    /**
     * @return Date et heure de début de la période analysée.
     */
    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @return Date et heure de fin de la période analysée.
     */
    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @return Recettes totales générées durant la période analysée.
     */
    public double getTotalRecettes() {
        return totalRecettes;
    }

    public void setTotalRecettes(double totalRecettes) {
        this.totalRecettes = totalRecettes;
    }

    /**
     * @return Date et heure de génération du rapport.
     */
    public LocalDateTime getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(LocalDateTime dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    /**
     * @return Email de l'administrateur ayant généré le rapport.
     */
    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }
}
