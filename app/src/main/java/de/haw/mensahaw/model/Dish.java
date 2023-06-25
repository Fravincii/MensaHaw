package de.haw.mensahaw.model;

public class Dish {
    private final String name;
    private final float price;

    public Dish(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public float getPrice() {
        return price;
    }


}
