package de.haw.mensahaw;

import org.junit.Test;

import static org.junit.Assert.*;

import de.haw.mensahaw.model.Dish;
import de.haw.mensahaw.model.MQTTManager;

public class DishUnitTests {
    @Test
    public void Init_givenName_equalsName() {
        String nameOfDish = "Nudeln";

        Dish dish = new Dish(nameOfDish, 0.5f);

        assertEquals(nameOfDish, dish.getName());
    }

    @Test
    public void Init_givenPrice_equalsPrice() {
        String nameOfDish = "Nudeln";
        float priceOfDish = 0.5f;

        Dish dish = new Dish(nameOfDish, 0.5f);

        assertEquals(priceOfDish, dish.getPrice(),0.00001f);
    }

}
