package de.haw.mensahaw.model;

public class DishManager {
    private Dish[] currentDishes;

    public DishManager(Dish[] currentDishes) {
        this.currentDishes = currentDishes;
    }
    public DishManager() {
        Dish firstDish = new Dish("Nudeln mit Hack", 2.75f);
        Dish secondDish = new Dish("Erbsen mit Hack", 1.5f);
        Dish thirdDish = new Dish("Hack mit Hack", 5f);
        Dish[] newDishes = {firstDish,secondDish,thirdDish};
        currentDishes = newDishes;
    }
    public int getCurrentDishArrayLength() {return currentDishes.length;}
    public Dish[] getCurrentDishes() {
        return currentDishes;
    }
    public Dish getDishById(int id){
        return currentDishes[id];
    }





}
