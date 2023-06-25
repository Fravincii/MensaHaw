package de.haw.mensahaw;

import static org.junit.Assert.assertEquals;

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
    public void check_thatSwitchedView_isPlatePromptView() {
        final CheckoutActivity mockedCheckoutActivity = mock(CheckoutActivity.class);
        checkoutViewModel.setCheckoutActivity(mockedCheckoutActivity);
        checkoutViewModel.openPlatePromptView();

        //verify(mockedCheckoutActivity).runOnUiThread(() -> mockedCheckoutActivity.openStartView());
    }
    @Test
    public void check_thatSwitchedViewbecauseConnection_isPlatePromptView() {
        final CheckoutActivity mockedCheckoutActivity = mock(CheckoutActivity.class);
        checkoutViewModel.setCheckoutActivity(mockedCheckoutActivity);
        checkoutViewModel.openPlatePromptViewBecauseConnection();

        //verify(mockedCheckoutActivity).runOnUiThread(() -> mockedCheckoutActivity.failedConnection());
    }
    @Test
    public void whenCheckoutStart_thenShowCheckout(){
        final CheckoutActivity mockedCheckoutActivity = mock(CheckoutActivity.class);
        checkoutViewModel.setCheckoutActivity(mockedCheckoutActivity);
        checkoutViewModel.showCheckout();

        //verify(mockedCheckoutActivity).quitLoadingScreen();
    }
    @Test
    public void check_disconnectFromServer(){
        final ProcessManager processmanager = mock(ProcessManager.class);
        checkoutViewModel.setProcessManager(processmanager);

        checkoutViewModel.disconnectFromServer();
        verify(processmanager).disconnectFromServer();
    }
}
