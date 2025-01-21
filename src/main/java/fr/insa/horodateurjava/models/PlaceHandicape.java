package fr.insa.horodateurjava.models;

/**
 * Classe représentant une place dédiée aux personnes en situation de handicap.
 * Hérite de la classe abstraite Place.
 */
public class PlaceHandicape extends Place {

    /**
     * Constructeur de la classe PlaceHandicape.
     *
     * @param numero        Le numéro unique de la place.
     * @param etage         L'étage où se situe la place.
     * @param tarifHoraire  Le tarif horaire pour l'utilisation de la place.
     * @param enTravaux     Indique si la place est en travaux (true/false).
     * @param idParking     L'identifiant du parking auquel appartient cette place.
     */
    public PlaceHandicape(int numero, int etage, double tarifHoraire, boolean enTravaux, int idParking) {
        // Appelle le constructeur de la classe parent Place avec les paramètres spécifiés,
        // en définissant "HandicapeHandicape" comme type de place (probablement une erreur répétée ici).
        super(numero, etage, "HandicapeHandicape", true, tarifHoraire, enTravaux, idParking);
    }
}
