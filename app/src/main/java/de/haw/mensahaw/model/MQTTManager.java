package de.haw.mensahaw.model;

import java.nio.charset.StandardCharsets;

public class MQTTManager {
    private MqttClient mqtt;
    private Database database;

    private boolean asUser;
    public void setDatabase(Database database) {
        this.database = database;
    }

    //TODO: Globale Application verbindungs instanz (Android FAQ)

    public boolean connectToServer(boolean asUser){
        if(mqtt != null) return false;

        this.asUser = asUser;
        final boolean connectToOnlineServer = true;
        boolean success;
        
        mqtt = new MqttClient();
        if(connectToOnlineServer)

        success = mqtt.connectToBroker(asUser? "my-mqtt-client-id" : "my-mqtt-client-id1",
                "broker.hivemq.com",
                1883,
                "my-user1",
                "my-password");
        else
            success = mqtt.connectToBroker(asUser? "my-mqtt-client-id" : "my-mqtt-client-id1",
                    "10.0.2.2",
                    1883,
                    "my-user",
                    "my-password");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return success;
    }

    //region Un-/Subscribe MQTT

    public void unsubscribeFromWeight(){mqtt.unsubscribe(database.SCALE_WEIGHT);}
    public void unsubscribeFromQRCode(){mqtt.unsubscribe(database.QRSCANNER_QRCODE);}

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

    private void ensureMQTTInitialized(){
        if (mqtt == null) {
            Log.error("MQTT Client is not inizialized!");
            connectToServer(asUser);
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
