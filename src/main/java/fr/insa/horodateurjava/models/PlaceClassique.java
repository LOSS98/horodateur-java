package fr.insa.horodateurjava.models;

/**
 * Classe représentant une place classique dans un parking.
 * Hérite de la classe abstraite {@link Place}.
 */
public class PlaceClassique extends Place {

    /**
     * Constructeur pour créer une place classique.
     *
     * @param numero       Numéro de la place.
     * @param etage        Étage où se trouve la place.
     * @param tarifHoraire Tarif horaire pour l'utilisation de la place.
     * @param enTravaux    Indique si la place est en travaux (true = en travaux).
     * @param idParking    Identifiant du parking auquel appartient la place.
     */
    public PlaceClassique(int numero, int etage, double tarifHoraire, boolean enTravaux, int idParking) {
        super(numero, etage, "Classique", true, tarifHoraire, enTravaux, idParking);
    }
}
