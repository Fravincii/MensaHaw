package de.haw.mensahaw.model;

import android.os.CountDownTimer;
import android.os.Handler;

import java.util.Arrays;
import java.util.List;


import de.haw.mensahaw.viewmodel.Checkout_ViewModel;


public class ProcessManager {
    private MQTTManager mqttManager;
    private MensaApplication mensaApplication;
    private Database database;

    public void setDatabase(Database database) {
        this.database = database;
    }
    public void setMensaApplication(MensaApplication mensaApplication) {
        this.mensaApplication = mensaApplication;
    }

    //Gets called when User presses Button
    public void initMQTT(){
        mqttManager = new MQTTManager();
        mqttManager.setDatabase(database);
        mqttManager.connectToServer(true);
    }
    public void waitForQRCode(){

        startCountdown();
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
            else Log.error("No Result for this QRCode!");
            mqttManager.unsubscribeFromQRCode();
        });
    }

    public void waitForWeight(){
        mqttManager.subscribeToWeight();
        //CountDownTimer timer = startCountdown(database.SCALE_WEIGHT);

        mqttManager.setScaleCallback(weight -> {
            receivedWeight = true;
            Dish weightedDish = weightedDish(weight);
            mqttManager.publishPrice(weightedDish.getPrice());
            mqttManager.unsubscribeFromWeight();
            startPaying(weightedDish);
        });
    }
    private boolean receivedWeight;
    private CountDownTimer startCountdown(){
        CountDownTimer timer = new CountDownTimer(40000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Log.info("time: " + millisUntilFinished);
                //TODO: Delete Publishing for Testing
                //if(millisUntilFinished < 20000) mqttManager.publishQRCode("1");
                //if(millisUntilFinished < 10000) mqttManager.publishWeight(3.57f);
            }
            @Override
            public void onFinish() {
                if(!receivedWeight){
                    mqttManager.unsubscribeFromQRCode();
                    mqttManager.unsubscribeFromWeight();

                    if(checkoutViewModel != null) checkoutViewModel.backToWeightingView();
                }

            }
        }.start();
        return timer;
    }
    private Dish weightedDish(float weight){
        float endPrice = weight * database.PRICE_PERKG_WEIGHTED_PLATE;
        return new Dish("Salat Bar", endPrice);
    }

    private Checkout_ViewModel checkoutViewModel;
    public void setCheckoutViewModel(Checkout_ViewModel checkoutViewModel) {
        this.checkoutViewModel = checkoutViewModel;
    }

    public void startPaying(Dish dishToPay){
        if (checkoutViewModel == null) {startPaying(dishToPay); return;}
        Log.info("Dish is: " + dishToPay.getName());
        checkoutViewModel.setPriceInView(dishToPay.getPrice());
        checkoutViewModel.setDishNameInView(dishToPay.getName());
    }
    public void endProcess(){
        mqttManager.disconnectFromServer();
    }
}
