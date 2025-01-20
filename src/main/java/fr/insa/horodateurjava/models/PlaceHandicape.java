package fr.insa.horodateurjava.models;

public class PlaceHandicape extends Place {
    public PlaceHandicape(int numero, int etage, double tarifHoraire, boolean enTravaux, int idParking) {
        super(numero, etage, "HandicapeHandicape", true, tarifHoraire, enTravaux, idParking);
    }
}
