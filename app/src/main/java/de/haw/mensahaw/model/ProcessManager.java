package de.haw.mensahaw.model;

import android.os.CountDownTimer;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;


import de.haw.mensahaw.viewmodel.Checkout_ViewModel;

public class ProcessManager {
    public MQTTManager getMqttManager() {return mqttManager;}
    public void setMqttManager(MQTTManager mqttManager) {
        this.mqttManager = mqttManager;
    }

    private MQTTManager mqttManager;
    private Database database;

    public void setDatabase(Database database) {
        this.database = database;
    }

    //Gets called when User presses Button
    public void initMQTT(){
        if(mqttManager == null) mqttManager = new MQTTManager();
        mqttManager.setDatabase(database);
        if (countDownTimer == null) startCountdown();
        mqttManager.connectToServer();
        mqttManager.setMQTTConnectionCallback(() -> handleMQTTConnectionCallback());
    }
    public void handleMQTTConnectionCallback(){
        waitForQRCode();
        mqttManager.removeMqttConnectionCallback();
    }
    public void waitForQRCode(){
        mqttManager.subscribeToQRCode();
        mqttManager.setQRCallback(qrCode -> processQRCode(qrCode));
    }
    public void processQRCode(String qrCode){
        final List<String> qrNormalPlatesList = Arrays.asList(database.QRCode_NORMAL_PLATES);

        if(database.QRCode_Weighted_Plate.equals(qrCode)) waitForWeight();
        else if (qrNormalPlatesList.contains(qrCode)){
            int index = qrNormalPlatesList.indexOf(qrCode);
            final Dish currentDish = database.TODAYS_DISHES[index];
            receivedWeight = true;
            startPaying(currentDish);
        }
        else {Log.error("No Result for this QRCode!"); return;}
        mqttManager.removeQRCallback();
        mqttManager.unsubscribeFromQRCode();
    }

    public void waitForWeight(){
        mqttManager.subscribeToWeight();
        mqttManager.setScaleCallback(weight -> handleWeight(weight));
    }

    public void handleWeight(float weight){
        receivedWeight = true;
        final Dish weightedDish = weightedDish(weight);
        mqttManager.publishPrice(weightedDish.getPrice());
        mqttManager.removeScaleCallback();
        mqttManager.unsubscribeFromWeight();
        startPaying(weightedDish);
    }

    public Dish weightedDish(float weight){
        final float endPrice = weight * database.PRICE_PERKG_WEIGHTED_PLATE;
        return new Dish(database.todaysWeightedDishName, endPrice);
    }
    public boolean isReceivedWeight() {return receivedWeight;}
    public void setReceivedWeight(boolean receivedWeight) {this.receivedWeight = receivedWeight;}
    private boolean receivedWeight;
    public void setCountDownTimer(CountDownTimer countDownTimer) {this.countDownTimer = countDownTimer;}

    private CountDownTimer countDownTimer;
    public void startCountdown(){
        countDownTimer = new CountDownTimer(45000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.info("timeLeft:" + millisUntilFinished);
                if(millisUntilFinished < 40000) mqttManager.publishQRCode("1");
            }
            @Override
            public void onFinish()  {handleTimeOut();}

        }.start();
    }
    public void handleTimeOut(){
        if(!receivedWeight){
            mqttManager.removeMqttConnectionCallback();
            mqttManager.removeQRCallback();
            mqttManager.removeScaleCallback();
            mqttManager.unsubscribeFromQRCode();
            mqttManager.unsubscribeFromWeight();
            if(checkoutViewModel != null) checkoutViewModel.openPlatePromptView();
        }
    }
    private Checkout_ViewModel checkoutViewModel;
    public void setCheckoutViewModel(Checkout_ViewModel checkoutViewModel) {this.checkoutViewModel = checkoutViewModel;}
    public Checkout_ViewModel getCheckoutViewModel() {return checkoutViewModel;}
    public void startPaying(Dish dishToPay){
        if (checkoutViewModel == null) return;
        checkoutViewModel.showCheckout();
        checkoutViewModel.setPriceInView(dishToPay.getPrice());
        checkoutViewModel.setDishNameInView(dishToPay.getName());
    }
}
