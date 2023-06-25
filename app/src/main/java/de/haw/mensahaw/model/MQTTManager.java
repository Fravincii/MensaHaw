package de.haw.mensahaw.model;

import androidx.annotation.NonNull;

import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MQTTManager {
    public MQTTManager() {
        setMqttClient(new MqttClient());
    }

    private MqttClient mqttClient;
    public MqttClient getMqttClient() {
        return mqttClient;
    }
    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    private Database database;
    public Database getDatabase() {return database;}
    public void setDatabase(Database database) {
        this.database = database;
    }

    private boolean connectToOnlineServer = true;
    public void setConnectToOnlineServer(boolean connectToOnlineServer) {this.connectToOnlineServer = connectToOnlineServer;}

    public void connectToServer(){
        if(mqttClient == null) setMqttClient(new MqttClient());
        if(mqttClient.getClient() != null && mqttClient.getClient().getState().isConnectedOrReconnect()) return;

        final String randomId = UUID.randomUUID().toString();
        if(connectToOnlineServer)
            mqttClient.connectToBroker(randomId,
                "broker.hivemq.com",
                1883,
                    randomId,
                "my-password");
        else
            mqttClient.connectToBroker(randomId,
                    "10.0.2.2",
                    1883,
                        randomId,
                    "my-password");

        mqttClient.setMqttConnectionCallback(() -> {
            this.mqttConnectionCallback.onConnectionSuccess();
            mqttClient.removeMqttConnectionCallback();
        });
    }

    //region Un-/Subscribe MQTT
    private String mqttpublishToString(Mqtt3Publish message){return new String(message.getPayloadAsBytes(), StandardCharsets.UTF_8);}
    public void subscribeToQRCode(){
        ensureMQTTInitialized();
        mqttClient.subscribe(database.QRSCANNER_QRCODE, message -> handleQRCodeMessage(mqttpublishToString(message)));
    }
    public void handleQRCodeMessage(String message){
        if(qrCallback == null) return;
        qrCallback.onQRCallback(message);
        //Log.info(String.format("MESSAGE: QR Code is %s", convertedMessageContent));
    }

    public void subscribeToWeight(){
        ensureMQTTInitialized();
        mqttClient.subscribe(database.SCALE_WEIGHT, message -> handleWeightMessage(mqttpublishToString(message)));
    }

    public void handleWeightMessage(String message){
        final float value = Float.parseFloat(message);
        scaleCallBack.onWeightCallback(value);
        //Log.info(String.format("MESSAGE: The Scale weight: %skg", convertedMessageContent));
    }

    public void unsubscribeFromWeight(){
        mqttClient.unsubscribe(database.SCALE_WEIGHT);}
    public void unsubscribeFromQRCode(){
        mqttClient.unsubscribe(database.QRSCANNER_QRCODE);}

    //endregion
    private void ensureMQTTInitialized(){
        if (mqttClient == null) {
            //Log.error("MQTT Client is not inizialized!");
            setMqttClient(new MqttClient());
            connectToServer();
        }
    }
    //region MQTT Publishing
    public void publishPrice(@NonNull Float price){
        ensureMQTTInitialized();
        mqttClient.publish(database.SCALE_PRICE, price.toString());
    }
    //Testing only
    public void publishWeight(@NonNull Float weight){
        ensureMQTTInitialized();
        mqttClient.publish(database.SCALE_WEIGHT, weight.toString());
    }
    public void publishQRCode(String QRCode){
        ensureMQTTInitialized();
        mqttClient.publish(database.QRSCANNER_QRCODE, QRCode);
    }
    //endregion

    public void disconnectFromServer(){mqttClient.disconnect();}

    //region Internal Callbacks

    private MQTTConnectionCallback mqttConnectionCallback;
    public MQTTConnectionCallback getMqttConnectionCallback() {return mqttConnectionCallback;}
    public void setMQTTConnectionCallback(MQTTConnectionCallback mqttConnectionCallback){this.mqttConnectionCallback = mqttConnectionCallback;}
    public void removeMqttConnectionCallback(){mqttConnectionCallback = null;}

    private ScaleCallBack scaleCallBack;
    public ScaleCallBack getScaleCallBack() {return scaleCallBack;}
    public void setScaleCallback(ScaleCallBack callback){this.scaleCallBack = callback;}
    public void removeScaleCallback(){this.scaleCallBack = null;};

    private QRCallback qrCallback;
    public QRCallback getQrCallback() {return qrCallback;}
    public void setQRCallback(QRCallback callback){this.qrCallback = callback;}
    public void removeQRCallback(){this.qrCallback= null;}
    //endregion

}
