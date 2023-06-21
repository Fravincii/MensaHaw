package de.haw.mensahaw.model;

import java.nio.charset.StandardCharsets;

public class MQTTManager {
    private MqttClient mqtt;
    private Database database;

    public void setDatabase(Database database) {
        this.database = database;
    }

    //TODO: Globale Application verbindungs instanz (Android FAQ)

    public boolean connectToServer(){
        if(mqtt != null) return false;

        final boolean connectToOnlineServer = true;
        mqtt = new MqttClient();
        if(connectToOnlineServer)

        mqtt.connectToBroker("my-mqtt-client-id",
                "broker.hivemq.com",
                1883,
                "my-user",
                "my-password");
        else
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

    //region Un-/Subscribe MQTT

    public void unsubscribeFromWeight(){mqtt.unsubscribe(database.SCALE_WEIGHT);}
    public void unsubscribeFromQRCode(){mqtt.unsubscribe(database.QRSCANNER_QRCODE);}
    /*
    public void subscribeToWeight(){
        subscribeToTopic(database.SCALE_WEIGHT);}
    public void subscribeToQRCode(){
        subscribeToTopic(database.QRSCANNER_QRCODE);}
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
    */
    public void subscribeToWeight(){
        mqtt.subscribe(database.SCALE_WEIGHT, message -> {
            String convertedMessageContent = new String(message.getPayloadAsBytes(), StandardCharsets.UTF_8);
            float value = Float.parseFloat(convertedMessageContent);
            scaleCallBack.onWeightCallback(value);
            Log.info(String.format("MESSAGE: The Scale weight: %skg", convertedMessageContent));
        });
    }
    public void subscribeToQRCode(){
        mqtt.subscribe(database.QRSCANNER_QRCODE, message -> {
            String convertedMessageContent = new String(message.getPayloadAsBytes(), StandardCharsets.UTF_8);
            qrCallback.onQRCallback(convertedMessageContent);
            Log.info(String.format("MESSAGE: QR Code is %s", convertedMessageContent));

        });
    }
    //endregion
    /*
    private void receiveData(String topic, String message) {
        try {
            if(topic == database.SCALE_WEIGHT){
                float value = Float.parseFloat(message);
                scaleCallBack.onWeightCallback(value);
                Log.info(String.format("MESSAGE: The Scale weight: %skg", message));
            }
            else if(topic == database.QRSCANNER_QRCODE){
                Log.info(String.format("MESSAGE: QR Code is %s", message));
                qrCallback.onQRCallback(message);
            }
            else if(topic == database.SCALE_PRICE){
                Log.info(String.format("MESSAGE: The Price of the Meal is: %s€ (Can be ignored)", message));
            }
            else {
                Log.error(String.format("MESSAGE: The Topic was wrong!", message));
            }
        } catch (Exception e) {
            Log.error(String.format("Message on %s was not a float value : %s", topic, message));
        }
    }
*/
    private void ensureMQTTInitialized(){
        if (mqtt == null) {
            Log.error("MQTT Client is not inizialized!");
            connectToServer();
        }
    }
    //region MQTT Publishing
    public void publishPrice(Float price){
        ensureMQTTInitialized();
        mqtt.publish(database.SCALE_PRICE, price.toString());
    }
    public void publishWeight(Float weigth){
        ensureMQTTInitialized();
        mqtt.publish(database.SCALE_WEIGHT, weigth.toString());
    }
    public void publishQRCode(String QRCode){
        ensureMQTTInitialized();
        mqtt.publish(database.QRSCANNER_QRCODE, QRCode);
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
