package de.haw.mensahaw.model;

import android.os.CountDownTimer;

import java.util.Arrays;
import java.util.List;

public class ProcessManager {
    private MQTTManager mqttManager;
    private DishManager dishManager;
    public void startApp(){

    }

    //Gets called when User presses Button
    public void startAsUser(){
        mqttManager = new MQTTManager();
        dishManager = new DishManager();
        //mqttManager.connectToLocalServer();
        mqttManager.connectToHAWServer();

    }


    //Gets called by secret Button
    public void startAsQRScanner(){

    }

    public void startScanningPlate(){
        waitForQRCode();
    }

    public void waitForQRCode(){
        final String[] QR_Normal_Plate = {"1", "2", "3"};
        final String QR_Weighted_Plate = "0";

        startCountdown("qrcode");
        mqttManager.setQRCallback(new QRCallback() {
            @Override
            public void onQRCallback(String code) {
                if(QR_Weighted_Plate.equals(code)) { startWeighing(); return;}

                List<String> liste = Arrays.asList(QR_Normal_Plate);
                if (liste.contains(code)){
                    int index = liste.indexOf(code);
                    Dish currentDish = dishManager.getDishById(index);
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
                if(topic == "weight")
                mqttManager.unsubscribeFromWeight();
                else if(topic == "qrcode"){
                    mqttManager.unsubscribeFromQRCode();
                }
            }
        };
    }
    public void waitForWeight(){
        startCountdown("weight");
        mqttManager.setScaleCallback(new ScaleCallBack() {
            @Override
            public void onWeightCallback(float weight) {
                startPaying(weightedDish(weight));
            }
        });
    }
    private Dish weightedDish(float weight){
        float pricePerKG = 2.75f;
        float endPrice = weight * pricePerKG;
        return new Dish("Salat Bar", endPrice);
    }

    void startWeighing(){
        waitForWeight();
    }

    public void startPaying(Dish dishToPay){

    }
    public void endProcess(){

        mqttManager.disconnectFromServer();
    }
}
