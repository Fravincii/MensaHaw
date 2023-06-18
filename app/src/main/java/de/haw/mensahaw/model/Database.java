package de.haw.mensahaw.model;

public class Database {
    public static final String SCALE_WEIGHT = "Scale/Weight";
    public static final String SCALE_PRICE = "Scale/Price";
    public static final String QRSCANNER_QRCODE = "QRScanner/QRCode";

    public static final String QR_Weighted_Plate = "0";
    public static final String[] QR_NORMAL_PLATES = {"1", "2", "3"};

    public static final float PRICE_PERKG_WEIGHTED_PLATE = 2.75f;
    public static final Dish firstDish = new Dish("Nudeln mit Hack", 2.75f);
    public static final Dish secondDish = new Dish("Erbsen mit Hack", 1.5f);
    public static final Dish thirdDish = new Dish("Hack mit Hack", 5f);

    public static final Dish[] TODAYS_DISHES = {firstDish,secondDish,thirdDish};
}
