package de.haw.mensahaw;

import org.junit.Test;
import static org.mockito.Mockito.*;

import de.haw.mensahaw.model.Database;
import de.haw.mensahaw.model.Dish;
import de.haw.mensahaw.model.MQTTConnectionCallback;
import de.haw.mensahaw.model.MQTTManager;
import de.haw.mensahaw.model.MensaApplication;
import de.haw.mensahaw.model.ProcessManager;
import de.haw.mensahaw.viewmodel.Checkout_ViewModel;

import static org.junit.Assert.*;

import android.os.CountDownTimer;

import org.junit.*;

public class ProcessManagerUnitTest {

    ProcessManager processManager;
    @Before
    public void init(){
        processManager = new ProcessManager();
    }
    @Test
    public void initMQTT() {
        MQTTManager mqttManagerMock = mock(MQTTManager.class);
        Database databaseMock = mock(Database.class);
        CountDownTimer countDownTimerMock = mock(CountDownTimer.class);

        processManager.setCountDownTimer(countDownTimerMock);
        processManager.setMqttManager(mqttManagerMock);
        processManager.setDatabase(databaseMock);

        processManager.initMQTT();

        verify(mqttManagerMock).setDatabase(any(Database.class));
        verify(mqttManagerMock).connectToServer();
        verify(mqttManagerMock).setMqttConnectionCallback(any(MQTTConnectionCallback.class));
    }
    @Test
    public void waitforQR() {
        /*
        MQTTManager mqttManagerMock = mock(MQTTManager.class);
        Database databaseMock = mock(Database.class);
        String qrCode = "1";
        final Dish rightDish = databaseMock.firstDish;

        mqttManagerMock.connectToServer();
        processManager.setMqttManager(mqttManagerMock);

        processManager.waitForQRCode();

        verify(mqttManagerMock).subscribeToQRCode();

        verify(mqttManagerMock).setQRCallback(qrcode ->{
            verify(processManager).startPaying(rightDish);
        });
        mqttManagerMock.publishQRCode(qrCode);

         */
    }
    @Test
    public void setCheckoutViewModel() {
        Checkout_ViewModel checkoutViewModel = new Checkout_ViewModel();

        processManager.setCheckoutViewModel(checkoutViewModel);

        assertEquals(checkoutViewModel,processManager.getCheckoutViewModel());
    }
    @Test
    public void startPaying() {
        final String dishName = "testfood";
        final float dishPrice = 0.5f;
        final Dish testDishToPay = new Dish(dishName, dishPrice);

        Checkout_ViewModel checkoutViewModelMock = mock(Checkout_ViewModel.class);
        processManager.setCheckoutViewModel(checkoutViewModelMock);

        processManager.startPaying(testDishToPay);

        verify(checkoutViewModelMock).showCheckout();
        verify(checkoutViewModelMock).setPriceInView(dishPrice);
        verify(checkoutViewModelMock).setDishNameInView(dishName);
    }
}

