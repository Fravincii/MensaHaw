package de.haw.mensahaw.model;

import android.os.CountDownTimer;

import java.util.Arrays;
import java.util.List;

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
    public void startAsUser(){
        mqttManager = new MQTTManager();
        mqttManager.setDatabase(database);
        //mqttManager.connectToLocalServer();
        mqttManager.connectToHAWServer();
    }

    public void startScanningPlate(){
        waitForQRCode();
    }

    public void waitForQRCode(){

        startCountdown(database.QRSCANNER_QRCODE);
        mqttManager.subscribeToQRCode();
        mqttManager.setQRCallback(new QRCallback() {
            @Override
            public void onQRCallback(String qrCode) {
                List<String> qRNormalPlatesList = Arrays.asList(database.QRCode_NORMAL_PLATES);

                if(database.QRCode_Weighted_Plate.equals(qrCode)) startWeighing();
                else if (qRNormalPlatesList.contains(qrCode)){
                    int index = qRNormalPlatesList.indexOf(qrCode);
                    Dish currentDish = database.TODAYS_DISHES[index];
                    startPaying(currentDish);
                }
                else Log.error("No Result for this QRCode!");
            }
        });
    }
    private void startCountdown(String topic){
        CountDownTimer timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                //TODO: Reset zum vorherigen View
                if(topic == database.SCALE_WEIGHT) {
                    mqttManager.unsubscribeFromWeight();
                }
                else if(topic == database.QRSCANNER_QRCODE){
                    mqttManager.unsubscribeFromQRCode();
                }
            }
        };
    }
    void startWeighing(){
        waitForWeight();
    }
    public void waitForWeight(){
        mqttManager.subscribeToWeight();
        startCountdown(database.SCALE_WEIGHT);
        mqttManager.setScaleCallback(new ScaleCallBack() {
            @Override
            public void onWeightCallback(float weight) {
                Dish weightedDish = weightedDish(weight);
                mqttManager.publishPrice(weightedDish.getPrice());
                startPaying(weightedDish);
            }
        });
    }
    private Dish weightedDish(float weight){
        float endPrice = weight * database.PRICE_PERKG_WEIGHTED_PLATE;
        return new Dish("Salat Bar", endPrice);
    }

    public Dish getDishToPay() {
        return dishToPay;
    }

    private Dish dishToPay;

    public void startPaying(Dish dishToPay){
        //TODO: Give front-end data


    }
    public void endProcess(){

        mqttManager.disconnectFromServer();
    }
}
