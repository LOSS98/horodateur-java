package fr.insa.horodateurjava.models;

public class PlaceDeuxRoues extends Place {
    public PlaceDeuxRoues(int numero, int etage, double tarifHoraire, boolean enTravaux, int idParking) {
        super(numero, etage, "DeuxRoues", true, tarifHoraire, enTravaux, idParking);
    }
}
