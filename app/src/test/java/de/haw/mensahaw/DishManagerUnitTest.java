package de.haw.mensahaw;

import org.junit.Test;

import static org.junit.Assert.*;

import de.haw.mensahaw.model.Dish;
import de.haw.mensahaw.model.DishManager;
import de.haw.mensahaw.model.MQTTManager;

public class DishManagerUnitTest {


    @Test
    public void getCurrentDishes_given3_equalsThree() {
        Dish[] dishes = {new Dish("test0", 1.5f),
                new Dish("test1", 3.2f),
                new Dish("test3", 0.75f)};

        DishManager dishManager = new DishManager(dishes);

        assertEquals(dishes, dishManager.getCurrentDishes());
    }
    @Test
    public void getCurrentDishes_given2_equalsTwo() {
        Dish[] dishes = {new Dish("test0", 1.5f),
                new Dish("test1", 3.2f),
                new Dish("test3", 0.75f)};

        DishManager dishManager = new DishManager(dishes);

        assertEquals(dishes, dishManager.getCurrentDishes());
    }
    @Test
    public void emptyInit_currentDishesSize_isThree() {
        DishManager dishManager = new DishManager();

        assertEquals(3, dishManager.getCurrentDishArrayLength());
    }
    @Test
    public void fullInit_currentDishesSize_isThree() {
        Dish[] dishes = {new Dish("test0", 1.5f),
                         new Dish("test1", 3.2f),
                         new Dish("test3", 0.75f)};

        DishManager dishManager = new DishManager(dishes);

        assertEquals(3, dishManager.getCurrentDishArrayLength());
    }
}
