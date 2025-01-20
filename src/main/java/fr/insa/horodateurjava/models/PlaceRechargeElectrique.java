package fr.insa.horodateurjava.models;

public class PlaceRechargeElectrique extends Place {
    private double puissanceCharge;
    public PlaceRechargeElectrique(int numero, int etage, double tarifHoraire, double puissanceCharge, boolean enTravaux, int idParking) {
        super(numero, etage, "RechargeElectrique", true, tarifHoraire, puissanceCharge, enTravaux, idParking);
        this.puissanceCharge = puissanceCharge;
    }

    public double getPuissanceCharge() {
        return this.puissanceCharge;
    }
}
