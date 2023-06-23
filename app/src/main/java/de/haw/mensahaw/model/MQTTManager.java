package de.haw.mensahaw.model;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MQTTManager {
    private MqttClient mqtt;
    private Database database;

    public void setDatabase(Database database) {
        this.database = database;
    }

    //TODO: Globale Application verbindungs instanz (Android FAQ)

    public void connectToServer(){
        if(mqtt != null) return;

        //Testing only
        final boolean connectToOnlineServer = false;
        final String randomId = UUID.randomUUID().toString();
        mqtt = new MqttClient();

        if(connectToOnlineServer)
            mqtt.connectToBroker(randomId,
                "broker.hivemq.com",
                1883,
                    randomId,
                "my-password");
        else
            mqtt.connectToBroker(randomId,
                    "10.0.2.2",
                    1883,
                        randomId,
                    "my-password");

        mqtt.setMqttConnectionCallback(() -> {
            this.mqttConnectionCallback.onConnectionSuccess();
            mqtt.removeMqttConnectionCallback();
        });
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
            connectToServer();
        }
    }
    //region MQTT Publishing
    public void publishPrice(Float price){
        ensureMQTTInitialized();
        mqtt.publish(database.SCALE_PRICE, price.toString());
    }
    //Testing only
    public void publishWeight(Float weight){
        ensureMQTTInitialized();
        mqtt.publish(database.SCALE_WEIGHT, weight.toString());
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
    private ScaleCallBack scaleCallBack;
    private QRCallback qrCallback;
    private MQTTConnectionCallback mqttConnectionCallback;

    public void setMqttConnectionCallback(MQTTConnectionCallback mqttConnectionCallback){
        this.mqttConnectionCallback = mqttConnectionCallback;
    }
    public void removeMqttConnectionCallback(){
        mqttConnectionCallback = null;
    }
    public void setScaleCallback(ScaleCallBack callback){
        this.scaleCallBack = callback;
    }
    public void removeScaleCallback(){this.scaleCallBack = null;};
    public void setQRCallback(QRCallback callback){this.qrCallback = callback;}
    public void removeQRCallback(){this.qrCallback= null;};
    //endregion

}
