package de.haw.mensahaw;

import static org.junit.Assert.assertEquals;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import de.haw.mensahaw.model.ProcessManager;
import de.haw.mensahaw.view.CheckoutActivity;
import de.haw.mensahaw.viewmodel.Checkout_ViewModel;
public class Checkout_ViewmodelUnitTest {

    private Checkout_ViewModel checkoutViewModel;
    @Before
    public void init(){
        checkoutViewModel = new Checkout_ViewModel();
    }
    @Test
    public void getProcessmanager(){
        final ProcessManager expectedProcessManager = mock(ProcessManager.class);

        checkoutViewModel.setProcessManager(expectedProcessManager);
        final ProcessManager actualProcessManager = checkoutViewModel.getProcessManager();

        assertEquals(expectedProcessManager,actualProcessManager);
    }
    @Test
    public void getCheckoutView(){
        final CheckoutActivity expectedCheckoutActivity = mock(CheckoutActivity.class);

        checkoutViewModel.setCheckoutActivity(expectedCheckoutActivity);
        final CheckoutActivity actualCheckoutActivity = checkoutViewModel.getCheckoutActivity();

        assertEquals(expectedCheckoutActivity,actualCheckoutActivity);
    }
    @Test
    public void initMQTT(){
        final ProcessManager mockedProcessManager = mock(ProcessManager.class);
        checkoutViewModel.setProcessManager(mockedProcessManager);
        checkoutViewModel.initMQTT();

        verify(mockedProcessManager).setCheckoutViewModel(checkoutViewModel);
        verify(mockedProcessManager).initMQTT();
    }
    @Test
    public void openPlatePromptView(){
        final CheckoutActivity mockedCheckoutActivity = mock(CheckoutActivity.class);
        checkoutViewModel.setCheckoutActivity(mockedCheckoutActivity);
        checkoutViewModel.openPlatePromptView();

        verify(mockedCheckoutActivity).openStartView();
    }
    @Test
    public void showCheckout(){
        final CheckoutActivity mockedCheckoutActivity = mock(CheckoutActivity.class);
       checkoutViewModel.setCheckoutActivity(mockedCheckoutActivity);
        checkoutViewModel.showCheckout();

        verify(mockedCheckoutActivity).quitLoadingScreen();
    }




    @Test
    public void get_dishName(){
        Checkout_ViewModel checkoutViewModel = new Checkout_ViewModel();
        MutableLiveData<String> expectedResult = mock(MutableLiveData.class);
        expectedResult.postValue("Döner");

        checkoutViewModel.setDishNameInView("Döner");
        String actualResult = checkoutViewModel.getDishName().toString();

        assertEquals(expectedResult.getValue(),actualResult);
    }
}
