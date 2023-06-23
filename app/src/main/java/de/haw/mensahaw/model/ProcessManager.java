package de.haw.mensahaw.model;

import android.os.CountDownTimer;

import java.util.Arrays;
import java.util.List;


import de.haw.mensahaw.viewmodel.Checkout_ViewModel;

public class ProcessManager {
    public MQTTManager getMqttManager() {
        return mqttManager;
    }

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
        mqttManager.setMqttConnectionCallback(() ->{
            waitForQRCode();
            mqttManager.removeMqttConnectionCallback();
        });

    }
    public void waitForQRCode(){

        mqttManager.subscribeToQRCode();

        mqttManager.setQRCallback(qrCode -> {
            List<String> qrNormalPlatesList = Arrays.asList(database.QRCode_NORMAL_PLATES);

            if(database.QRCode_Weighted_Plate.equals(qrCode)) waitForWeight();
            else if (qrNormalPlatesList.contains(qrCode)){
                int index = qrNormalPlatesList.indexOf(qrCode);
                Dish currentDish = database.TODAYS_DISHES[index];
                receivedWeight = true;
                startPaying(currentDish);
            }
            else {Log.error("No Result for this QRCode!"); return;}
            mqttManager.removeQRCallback();
            mqttManager.unsubscribeFromQRCode();
        });
    }

    private void waitForWeight(){
        mqttManager.subscribeToWeight();

        mqttManager.setScaleCallback(weight -> {
            receivedWeight = true;
            Dish weightedDish = weightedDish(weight);

            mqttManager.publishPrice(weightedDish.getPrice());

            mqttManager.removeScaleCallback();
            mqttManager.unsubscribeFromWeight();

            startPaying(weightedDish);
        });
    }
    private boolean receivedWeight;

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    private CountDownTimer countDownTimer;
    public void startCountdown(){
        CountDownTimer timer = new CountDownTimer(45000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.info("timeLeft:" + millisUntilFinished);
                if(millisUntilFinished < 40000) mqttManager.publishQRCode("1");
            }
            @Override
            public void onFinish() {
                if(!receivedWeight){
                    mqttManager.removeMqttConnectionCallback();
                    mqttManager.removeQRCallback();
                    mqttManager.removeScaleCallback();

                    mqttManager.unsubscribeFromQRCode();
                    mqttManager.unsubscribeFromWeight();
                    if(checkoutViewModel != null) checkoutViewModel.openPlatePromptView();
                }

            }
        }.start();
    }
    private Dish weightedDish(float weight){
        float endPrice = weight * database.PRICE_PERKG_WEIGHTED_PLATE;
        return new Dish(database.todaysWeightedDishName, endPrice);
    }

    private Checkout_ViewModel checkoutViewModel;
    public void setCheckoutViewModel(Checkout_ViewModel checkoutViewModel) {
        this.checkoutViewModel = checkoutViewModel;
    }

    private void startPaying(Dish dishToPay){
        if (checkoutViewModel == null) {startPaying(dishToPay); return;}
        Log.info("Dish is: " + dishToPay.getName());
        checkoutViewModel.showCheckout();
        checkoutViewModel.setPriceInView(dishToPay.getPrice());
        checkoutViewModel.setDishNameInView(dishToPay.getName());
    }
}
