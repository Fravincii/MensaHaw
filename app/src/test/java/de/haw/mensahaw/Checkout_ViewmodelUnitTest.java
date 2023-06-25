package de.haw.mensahaw;

import static org.junit.Assert.assertEquals;
import androidx.lifecycle.MutableLiveData;

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
    public void getting_Processmanager_intoViewModel(){
        final ProcessManager expectedProcessManager = mock(ProcessManager.class);

        checkoutViewModel.setProcessManager(expectedProcessManager);
        final ProcessManager actualProcessManager = checkoutViewModel.getProcessManager();

        assertEquals(expectedProcessManager,actualProcessManager);
    }
    @Test
    public void check_thatSwitchedView_isCheckoutView(){
        final CheckoutActivity expectedCheckoutActivity = mock(CheckoutActivity.class);

        checkoutViewModel.setCheckoutActivity(expectedCheckoutActivity);
        final CheckoutActivity actualCheckoutActivity = checkoutViewModel.getCheckoutActivity();

        assertEquals(expectedCheckoutActivity,actualCheckoutActivity);
    }
    @Test
    public void check_thatMQTT_isInitialised(){
        final ProcessManager mockedProcessManager = mock(ProcessManager.class);
        checkoutViewModel.setProcessManager(mockedProcessManager);
        checkoutViewModel.initMQTT();

        verify(mockedProcessManager).setCheckoutViewModel(checkoutViewModel);
        verify(mockedProcessManager).initMQTT();
    }
    @Test
    public void check_thatSwitchedView_isPlatePromptView(){
        final CheckoutActivity mockedCheckoutActivity = mock(CheckoutActivity.class);
        checkoutViewModel.setCheckoutActivity(mockedCheckoutActivity);
        checkoutViewModel.openPlatePromptView();

        verify(mockedCheckoutActivity).openStartView();
    }
    @Test
    public void whenCheckoutStart_thenShowCheckout(){
        final CheckoutActivity mockedCheckoutActivity = mock(CheckoutActivity.class);
        checkoutViewModel.setCheckoutActivity(mockedCheckoutActivity);
        checkoutViewModel.showCheckout();

        verify(mockedCheckoutActivity).quitLoadingScreen();
    }
    @Test
    public void givenliveDishname_isActualDishname(){
        Checkout_ViewModel checkoutViewModel = new Checkout_ViewModel();
        MutableLiveData<String> expectedResult = mock(MutableLiveData.class);
        expectedResult.postValue("Döner");

        checkoutViewModel.setDishName(expectedResult);
        checkoutViewModel.setDishNameInView("Döner");


        String actualResult = checkoutViewModel.getDishName().getValue().toString();

        assertEquals(expectedResult.toString(),actualResult);
    }
}
