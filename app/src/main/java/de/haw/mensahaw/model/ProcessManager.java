package de.haw.mensahaw.model;

import android.os.CountDownTimer;
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
        //mqttManager.connectToLocalServer();
        mqttManager.connectToServer();
    }
    public void waitForQRCode(){

        startCountdown(database.QRSCANNER_QRCODE);
        mqttManager.subscribeToQRCode();

        mqttManager.setQRCallback(qrCode -> {
            List<String> qrNormalPlatesList = Arrays.asList(database.QRCode_NORMAL_PLATES);

            if(database.QRCode_Weighted_Plate.equals(qrCode)) waitForWeight();
            else if (qrNormalPlatesList.contains(qrCode)){
                int index = qrNormalPlatesList.indexOf(qrCode);
                Dish currentDish = database.TODAYS_DISHES[index];
                startPaying(currentDish);
            }
            else Log.error("No Result for this QRCode!");
        });
    }
    private void startCountdown(String topic){
        CountDownTimer timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                if(topic == database.SCALE_WEIGHT) {
                    mqttManager.unsubscribeFromWeight();
                }
                else if(topic == database.QRSCANNER_QRCODE){
                    mqttManager.unsubscribeFromQRCode();
                }
                if(checkoutViewModel != null) checkoutViewModel.backToWeightingView();
            }
        };
        timer.start();
    }
    public void waitForWeight(){
        mqttManager.subscribeToWeight();
        startCountdown(database.SCALE_WEIGHT);

        mqttManager.setScaleCallback(weight -> {
            Dish weightedDish = weightedDish(weight);
            mqttManager.publishPrice(weightedDish.getPrice());
            startPaying(weightedDish);
        });
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
        checkoutViewModel.setPriceInView(dishToPay.getPrice());
        checkoutViewModel.setDishNameInView(dishToPay.getName());
    }
    public void endProcess(){
        mqttManager.disconnectFromServer();
    }
}
