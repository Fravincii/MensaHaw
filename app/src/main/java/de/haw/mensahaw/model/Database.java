package de.haw.mensahaw.model;

public class Database {
    public final String SCALE_WEIGHT = "Scale/Weight";
    public final String SCALE_PRICE = "Scale/Price";
    public final String QRSCANNER_QRCODE = "QRScanner/QRCode";

    public final String QRCode_Weighted_Plate = "0";
    public final String[] QRCode_NORMAL_PLATES = {"1", "2", "3"};

    public final float PRICE_PERKG_WEIGHTED_PLATE = 2.75f;
    public final Dish firstDish = new Dish("Nudeln mit Hack", 2.75f);
    public final Dish secondDish = new Dish("Erbsen mit Hack", 1.5f);
    public final Dish thirdDish = new Dish("Francesco mit Hack", 8f);
    public final Dish[] TODAYS_DISHES = {firstDish,secondDish,thirdDish};
    public final String todaysWeightedDishName = "Einfach nur Nudeln";



}
