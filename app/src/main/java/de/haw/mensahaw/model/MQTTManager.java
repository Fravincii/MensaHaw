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

    public void connectToServer(boolean asUser){
        if(mqtt != null) return;

        this.asUser = asUser;
        final boolean connectToOnlineServer = false;
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

        mqtt.setMqttConnectionCallback(() -> {
            this.mqttConnectionCallback.onConnectionSuccess();
            mqtt.removeMqttConnectionCallback();
        });
    }

    //region Un-/Subscribe MQTT

    public boolean unsubscribeFromWeight(){return mqtt.unsubscribe(database.SCALE_WEIGHT);}
    public boolean unsubscribeFromQRCode(){return mqtt.unsubscribe(database.QRSCANNER_QRCODE);}

    public boolean subscribeToWeight(){
        return mqtt.subscribe(database.SCALE_WEIGHT, message -> {
            String convertedMessageContent = new String(message.getPayloadAsBytes(), StandardCharsets.UTF_8);
            float value = Float.parseFloat(convertedMessageContent);
            scaleCallBack.onWeightCallback(value);
            Log.info(String.format("MESSAGE: The Scale weight: %skg", convertedMessageContent));
        });
    }
    public boolean subscribeToQRCode(){
       return mqtt.subscribe(database.QRSCANNER_QRCODE, message -> {
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
    public boolean publishPrice(Float price){
        ensureMQTTInitialized();
        return mqtt.publish(database.SCALE_PRICE, price.toString());
    }
    public boolean publishWeight(Float weigth){
        ensureMQTTInitialized();
        return mqtt.publish(database.SCALE_WEIGHT, weigth.toString());
    }
    public boolean publishQRCode(String QRCode){
        ensureMQTTInitialized();
        return mqtt.publish(database.QRSCANNER_QRCODE, QRCode);
    }
    //endregion

    public boolean disconnectFromServer(){
        return mqtt.disconnect();
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
