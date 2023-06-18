package de.haw.mensahaw.model;

import android.os.CountDownTimer;

import java.util.Arrays;
import java.util.List;

public class ProcessManager {
    private MQTTManager mqttManager;

    //Gets called when User presses Button
    public void startAsUser(){
        mqttManager = new MQTTManager();
        //mqttManager.connectToLocalServer();
        mqttManager.connectToHAWServer();

    }
    //Gets called by secret Button

    public void ResetApp(){

    }

    public void startScanningPlate(){
        waitForQRCode();
    }

    public void waitForQRCode(){
        startCountdown(Database.QRSCANNER_QRCODE);
        mqttManager.setQRCallback(new QRCallback() {
            @Override
            public void onQRCallback(String qrCode) {
                List<String> qRNormalPlatesList = Arrays.asList(Database.QRCode_NORMAL_PLATES);

                if(Database.QRCode_Weighted_Plate.equals(qrCode)) startWeighing();
                else if (qRNormalPlatesList.contains(qrCode)){
                    int index = qRNormalPlatesList.indexOf(qrCode);
                    Dish currentDish = Database.TODAYS_DISHES[index];
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
                if(topic == Database.SCALE_WEIGHT) {
                    mqttManager.unsubscribeFromWeight();
                }
                else if(topic == Database.QRSCANNER_QRCODE){
                    mqttManager.unsubscribeFromQRCode();
                }
            }
        };
    }
    void startWeighing(){
        waitForWeight();
    }
    public void waitForWeight(){
        startCountdown(Database.SCALE_WEIGHT);
        mqttManager.setScaleCallback(new ScaleCallBack() {
            @Override
            public void onWeightCallback(float weight) {
                Dish weightedDish = weightedDish(weight);
                startPaying(weightedDish);
            }
        });
    }
    private Dish weightedDish(float weight){
        float endPrice = weight * Database.PRICE_PERKG_WEIGHTED_PLATE;
        return new Dish("Salat Bar", endPrice);
    }



    public void startPaying(Dish dishToPay){
        //TODO: Give front-end data
    }
    public void endProcess(){

        mqttManager.disconnectFromServer();
    }
}
