package de.haw.mensahaw;

import org.junit.Test;
import static org.mockito.Mockito.*;

import de.haw.mensahaw.model.Database;
import de.haw.mensahaw.model.Dish;
import de.haw.mensahaw.model.MQTTConnectionCallback;
import de.haw.mensahaw.model.MQTTManager;
import de.haw.mensahaw.model.MqttClient;
import de.haw.mensahaw.model.ProcessManager;
import de.haw.mensahaw.model.QRCallback;
import de.haw.mensahaw.viewmodel.Checkout_ViewModel;

import static org.junit.Assert.*;

import android.os.CountDownTimer;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

public class ProcessManagerUnitTest {

    ProcessManager processManager;
    @Before
    public void init(){
        processManager = new ProcessManager();
    }

    @Test
    public void getSetCheckoutViewModel(){
        final Checkout_ViewModel expectedViewmodelmock = mock(Checkout_ViewModel.class);
        processManager.setCheckoutViewModel(expectedViewmodelmock);

        final Checkout_ViewModel actualViemodel = processManager.getCheckoutViewModel();
        assertEquals(expectedViewmodelmock,actualViemodel);

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
    public void processQRCode0() {
        final String qrCode = "0";
        final MQTTManager mockedMQTTMAnager = mock(MQTTManager.class);
        processManager.setMqttManager(mockedMQTTMAnager);
        processManager.setDatabase(new Database());
        processManager.processQRCode(qrCode);

        verify(mockedMQTTMAnager).subscribeToWeight();
    }
    @Test
    public void processQRCode1() {
        final String qrCode = "1";
        final MQTTManager mockedMQTTMAnager = mock(MQTTManager.class);
        final Checkout_ViewModel checkoutViewModel = mock(Checkout_ViewModel.class);

        processManager.setMqttManager(mockedMQTTMAnager);
        processManager.setDatabase(new Database());
        processManager.setCheckoutViewModel(checkoutViewModel);
        processManager.processQRCode(qrCode);

        verify(checkoutViewModel).showCheckout();
    }
    @Test
    public void handleMQTTConnectionCallback() {
        final MQTTManager mockedMQTTMAnager = mock(MQTTManager.class);

        processManager.setMqttManager(mockedMQTTMAnager);

        processManager.handleMQTTConnectionCallback();
        verify(mockedMQTTMAnager).removeMqttConnectionCallback();

    }
    @Test
    public void handleWeight() {
        final float weight = 2.5f;
        final MQTTManager mockedMQTTMAnager = mock(MQTTManager.class);
        final Database database = mock(Database.class);
        final Dish dish = new Dish(database.todaysWeightedDishName, weight * database.PRICE_PERKG_WEIGHTED_PLATE);

        processManager.setCheckoutViewModel(mock(Checkout_ViewModel.class));
        processManager.setMqttManager(mockedMQTTMAnager);
        processManager.setDatabase(database);

        processManager.handleWeight(weight);

        verify(mockedMQTTMAnager).publishPrice(dish.getPrice());
        verify(mockedMQTTMAnager).removeScaleCallback();
        verify(mockedMQTTMAnager).unsubscribeFromWeight();
    }
    @Test
    public void handleTimeOut() {
        final boolean receive = false;

        final Checkout_ViewModel checkoutViewModel = mock(Checkout_ViewModel.class);
        final MQTTManager mqttManagerMock = mock(MQTTManager.class);
        processManager.setMqttManager(mqttManagerMock);
        processManager.setCheckoutViewModel(checkoutViewModel);
        processManager.setReceivedWeight(receive);

        processManager.handleTimeOut();
        verify(mqttManagerMock).removeScaleCallback();
        verify(mqttManagerMock).removeQRCallback();
        verify(mqttManagerMock).removeMqttConnectionCallback();
        verify(mqttManagerMock).unsubscribeFromQRCode();
        verify(mqttManagerMock).unsubscribeFromWeight();
        verify(checkoutViewModel).openPlatePromptView();
    }

    @Test
    public void waitforQR2() {
        MQTTManager mqttManagerMock = mock(MQTTManager.class);
        processManager.setMqttManager(mqttManagerMock);

        processManager.waitForQRCode();
        verify(mqttManagerMock).subscribeToQRCode();
    }

    @Test
    public void waitforWeight() {
        MQTTManager mqttManagerMock = mock(MQTTManager.class);
        processManager.setMqttManager(mqttManagerMock);
        processManager.waitForWeight();
        verify(mqttManagerMock).subscribeToWeight();


    }
    @Test
    public void setCheckoutViewModel() {
        Checkout_ViewModel checkoutViewModel = new Checkout_ViewModel();

        processManager.setCheckoutViewModel(checkoutViewModel);

        assertEquals(checkoutViewModel,processManager.getCheckoutViewModel());
    }


    @Test
    public void setReceivedWeight() {
        final boolean expectedBoolean = true;
        processManager.setReceivedWeight(expectedBoolean);

        final boolean actualBoolean = processManager.isReceivedWeight();

        assertEquals(expectedBoolean,actualBoolean);
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

