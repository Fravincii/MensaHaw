package de.haw.mensahaw;

import org.junit.Test;
import static org.mockito.Mockito.*;

import de.haw.mensahaw.model.Database;
import de.haw.mensahaw.model.Dish;
import de.haw.mensahaw.model.MQTTConnectionCallback;
import de.haw.mensahaw.model.MQTTManager;
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
    public void getSetMQTTManager(){
        MQTTManager expectedMQTTManager = new MQTTManager();

        processManager.setMqttManager(expectedMQTTManager);
        MQTTManager actualMQTTManager = processManager.getMqttManager();

        assertEquals(expectedMQTTManager, actualMQTTManager);
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
        verify(mqttManagerMock).setMQTTConnectionCallback(any(MQTTConnectionCallback.class));
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
    public void waitforQR1() {

        MQTTManager mqttManagerMock = mock(MQTTManager.class);
        processManager.waitForQRCode();
        verify(mqttManagerMock).subscribeToQRCode();
        verify(mqttManagerMock).setQRCallback(any());
    }
    @Test
    public void waitforQR2() {
        MQTTManager mqttManagerMock = mock(MQTTManager.class);
        processManager.waitForQRCode();
        verify(mqttManagerMock).subscribeToQRCode();
    }

    @Test
    public void setCheckoutViewModel() {
        Checkout_ViewModel checkoutViewModel = new Checkout_ViewModel();

        processManager.setCheckoutViewModel(checkoutViewModel);

        assertEquals(checkoutViewModel,processManager.getCheckoutViewModel());
    }
    @Test
    public void weightedDishPrice(){
        final Database database = new Database();
        processManager.setDatabase(database);

        final float weight = 3.75f;
        final float expectedEndPrice = weight * database.PRICE_PERKG_WEIGHTED_PLATE;
        final Dish weightedDish = processManager.weightedDish(weight);
        final float actualEndPrice = weightedDish.getPrice();

        assertEquals(expectedEndPrice, actualEndPrice, 0.00001f);
    }
    @Test
    public void weightedDishName(){
        final Database database = new Database();
        processManager.setDatabase(database);

        final float weight = 3.75f;
        final String expectedName = database.todaysWeightedDishName;
        final Dish weightedDish = processManager.weightedDish(weight);
        final String actualName = weightedDish.getName();

        assertEquals(expectedName, actualName);
    }
    @Test
    public void startCountdown() {
        final MQTTManager mockedMQTTManager = mock(MQTTManager.class);
        final CountDownTimer mockedCountdown = mock(CountDownTimer.class);

        processManager.setMqttManager(mockedMQTTManager);

        processManager.setCountDownTimer(mockedCountdown);
        processManager.startCountdown();


        processManager.setReceivedWeight(true);

        verifyNoInteractions();
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

