package de.haw.mensahaw;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import de.haw.mensahaw.model.ProcessManager;
import de.haw.mensahaw.view.CheckoutActivity;
import de.haw.mensahaw.view.PayConfirmationActivity;
import de.haw.mensahaw.viewmodel.Checkout_ViewModel;
import de.haw.mensahaw.viewmodel.PayConfirmation_ViewModel;

public class PayConfirmation_ViewmodelUnitTest {

    private PayConfirmation_ViewModel payConfirmation_viewModel;
    @Before
    public void init(){
        payConfirmation_viewModel = new PayConfirmation_ViewModel();

    }
    @Test
    public void getting_Processmanager_intoViewModel(){

        final ProcessManager expectedProcessManager = mock(ProcessManager.class);
        payConfirmation_viewModel.setProcessManager(expectedProcessManager);
        final ProcessManager actualProcessManager = payConfirmation_viewModel.getProcessManager();

        assertEquals(expectedProcessManager,actualProcessManager);
    }
    @Test
    public void check_disconnectFromServer(){
        final ProcessManager processmanager = mock(ProcessManager.class);
        payConfirmation_viewModel.setProcessManager(processmanager);

        payConfirmation_viewModel.disconnectFromServer();
        verify(processmanager).disconnectFromServer();
    }
}
