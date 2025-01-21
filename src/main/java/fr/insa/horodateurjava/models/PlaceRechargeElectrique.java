package fr.insa.horodateurjava.models;

/**
 * Classe représentant une place de parking avec une station de recharge électrique.
 * Hérite de la classe abstraite `Place`.
 */
public class PlaceRechargeElectrique extends Place {

    // Puissance de charge de la station de recharge électrique, en kW.
    private double puissanceCharge;

    /**
     * Constructeur de la classe PlaceRechargeElectrique.
     *
     * @param numero          Le numéro unique de la place.
     * @param etage           L'étage où se situe la place.
     * @param tarifHoraire    Le tarif horaire pour l'utilisation de la place.
     * @param puissanceCharge La puissance de charge de la station (en kW).
     * @param enTravaux       Indique si la place est en travaux (true/false).
     * @param idParking       L'identifiant du parking auquel appartient cette place.
     */
    public PlaceRechargeElectrique(int numero, int etage, double tarifHoraire, double puissanceCharge, boolean enTravaux, int idParking) {
        // Appelle le constructeur de la classe parent Place avec les paramètres spécifiés.
        // Définit "RechargeElectrique" comme type de place.
        super(numero, etage, "RechargeElectrique", true, tarifHoraire, puissanceCharge, enTravaux, idParking);
        this.puissanceCharge = puissanceCharge; // Initialise la puissance de charge spécifique à cette place.
    }

    /**
     * Getter pour obtenir la puissance de charge de la station.
     *
     * @return La puissance de charge en kW.
     */
    public double getPuissanceCharge() {
        return this.puissanceCharge;
    }
}
