package de.haw.mensahaw.model;

import java.nio.charset.StandardCharsets;

public class MQTTManager {


    private MqttClient mqtt;

    public boolean connectToLocalServer(){
        if(mqtt != null) return false;
        mqtt = new MqttClient();
        mqtt.connectToBroker("myClientId",
                "10.0.2.2",
                1883,
                "my-user",
                "my-password");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    public boolean connectToHAWServer(){
        if(mqtt != null) return false;
        mqtt = new MqttClient();
        mqtt.connectToBroker("my-mqtt-client-id",
                "broker.hivemq.com",
                1883,
                "my-user",
                "my-password");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    //region Un-/Subscribe MQTT

    public void subcribeToWeight(){
        subscribeToTopic(Database.SCALE_WEIGHT);}
    public void subcribeToQRCode(){
        subscribeToTopic(Database.QRSCANNER_QRCODE);}
    public void subscribeToPrice(){
        subscribeToTopic(Database.SCALE_PRICE);}

    public void unsubscribeFromWeight(){mqtt.unsubscribe(Database.SCALE_WEIGHT);}
    public void unsubscribeFromQRCode(){mqtt.unsubscribe(Database.QRSCANNER_QRCODE);}
    public void unsubscribeFromPrice(){mqtt.unsubscribe(Database.SCALE_PRICE);}

    private void subscribeToTopic(String topic){
        backUp();
        mqtt.subscribe(topic, (message) -> {
            try {
                String convertedMessageContent = new String(message.getPayloadAsBytes(), StandardCharsets.UTF_8);
                receiveData(topic, convertedMessageContent);
            } catch (Exception e) {
                //returns message as bytes
                Log.error(String.format("Message from %s: %s", message.getTopic(), message.getPayloadAsBytes()) + ", error: " + e.getMessage());
            }
        });
    }

    //endregion
    private void receiveData(String topic, String message) {
        try {
            if(topic == Database.SCALE_WEIGHT){
                float value = Float.parseFloat(message);
                scaleCallBack.onWeightCallback(value);
                Log.info(String.format("MESSAGE: The Scale weight: %skg", message));
            }
            else if(topic == Database.QRSCANNER_QRCODE){
                Log.info(String.format("MESSAGE: QR Code is %s", message));
                qrCallback.onQRCallback(message);
            }
            else if(topic == Database.SCALE_PRICE){
                Log.info(String.format("MESSAGE: The Price of the Meal is: %s€ (Can be ignored)", message));
            }
            else {
                Log.error(String.format("MESSAGE: The Topic was wrong!", message));
            }
        } catch (Exception e) {
            Log.error(String.format("Message on %s was not a float value : %s", topic, message));
        }
    }
    private void backUp(){
        if (mqtt == null) {
            Log.error("MQTT Client is not inizialized!");
            connectToLocalServer();
        }
    }
    //region MQTT Publishing
    public void publishPrice(Float price){
        backUp();
        mqtt.publish(Database.SCALE_PRICE, price.toString());
    }

    public void publishWeight(Float weight){
        backUp();
        mqtt.publish(Database.SCALE_WEIGHT, weight.toString());
    }
    public void publishQRCode(String QRCode){
        backUp();
        mqtt.publish(Database.QRSCANNER_QRCODE, QRCode);
    }
    //endregion

    public void disconnectFromServer(){
        mqtt.disconnect();
    }

    //region Internal Callbacks
    ScaleCallBack scaleCallBack;
    QRCallback qrCallback;
    public void setScaleCallback(ScaleCallBack callback){
        this.scaleCallBack = callback;
    }
    public void removeScaleCallback(){this.scaleCallBack = null;};
    public void setQRCallback(QRCallback callback){this.qrCallback = callback;}
    public void removeQRCallback(){this.qrCallback= null;};
    //endregion



}
