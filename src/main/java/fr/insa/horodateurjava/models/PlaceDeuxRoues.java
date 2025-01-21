package fr.insa.horodateurjava.models;

/**
 * Classe représentant une place spécifique pour les deux-roues.
 * Hérite de la classe abstraite Place.
 */
public class PlaceDeuxRoues extends Place {

    /**
     * Constructeur de la classe PlaceDeuxRoues.
     *
     * @param numero        Le numéro unique de la place.
     * @param etage         L'étage où se situe la place.
     * @param tarifHoraire  Le tarif horaire pour l'utilisation de la place.
     * @param enTravaux     Indique si la place est en travaux (true/false).
     * @param idParking     L'identifiant du parking auquel appartient cette place.
     */
    public PlaceDeuxRoues(int numero, int etage, double tarifHoraire, boolean enTravaux, int idParking) {
        // Appelle le constructeur de la classe parent Place avec les paramètres spécifiés,
        // en définissant "DeuxRoues" comme type de place.
        super(numero, etage, "DeuxRoues", true, tarifHoraire, enTravaux, idParking);
    }
}
