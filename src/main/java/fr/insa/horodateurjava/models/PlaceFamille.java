package fr.insa.horodateurjava.models;

/**
 * Classe représentant une place spécifique pour les familles.
 * Hérite de la classe abstraite Place.
 */
public class PlaceFamille extends Place {

    /**
     * Constructeur de la classe PlaceFamille.
     *
     * @param numero        Le numéro unique de la place.
     * @param etage         L'étage où se situe la place.
     * @param tarifHoraire  Le tarif horaire pour l'utilisation de la place.
     * @param enTravaux     Indique si la place est en travaux (true/false).
     * @param idParking     L'identifiant du parking auquel appartient cette place.
     */
    public PlaceFamille(int numero, int etage, double tarifHoraire, boolean enTravaux, int idParking) {
        // Appelle le constructeur de la classe parent Place avec les paramètres spécifiés,
        // en définissant "Famille" comme type de place.
        super(numero, etage, "Famille", true, tarifHoraire, enTravaux, idParking);
    }
}
