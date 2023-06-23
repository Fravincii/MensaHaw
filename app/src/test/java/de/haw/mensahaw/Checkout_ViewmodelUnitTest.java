package de.haw.mensahaw;

import static org.junit.Assert.assertEquals;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import static org.mockito.Mockito.*;
import org.junit.Test;

import de.haw.mensahaw.viewmodel.Checkout_ViewModel;
public class Checkout_ViewmodelUnitTest {

    @Test
    public void get_dishName(){
        Checkout_ViewModel checkoutViewModel = new Checkout_ViewModel();
        MutableLiveData<String> expectedResult = new MutableLiveData<>();
        expectedResult.setValue("Döner");

        checkoutViewModel.setDishNameInView("Döner");
        MutableLiveData<String> actualResult = checkoutViewModel.getDishName();

        assertEquals(expectedResult.getValue(),actualResult.getValue());
    }
}
