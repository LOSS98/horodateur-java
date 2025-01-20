package fr.insa.horodateurjava.models;

public class PlaceClassique extends Place {
    public PlaceClassique(int numero, int etage, double tarifHoraire, boolean enTravaux, int idParking) {
        super(numero, etage, "Classique", true, tarifHoraire, enTravaux, idParking);
    }
}
