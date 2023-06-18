package de.haw.mensahaw.model;

public class QRCodeManager {
    private MQTTManager mqttManager;

    public QRCodeManager() {
        mQTTSetup();
    }
    public void mQTTSetup(){
        mqttManager = new MQTTManager();
        mqttManager.connectToLocalServer();
    }

    public void SendQRCode(String qrCode){
        mqttManager.publishQRCode(qrCode);
    }
    public void endconnection(){
        mqttManager.disconnectFromServer();
    }
}
