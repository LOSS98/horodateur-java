package fr.insa.horodateurjava.models;

public class PlaceFamille extends Place {
    public PlaceFamille(int numero, int etage, double tarifHoraire, boolean enTravaux, int idParking) {
        super(numero, etage, "Famille", true, tarifHoraire, enTravaux, idParking);
    }
}
