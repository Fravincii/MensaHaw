package de.haw.mensahaw;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;

import android.util.Log;

import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;

import org.junit.Before;
import org.junit.Test;

import de.haw.mensahaw.model.Database;
import de.haw.mensahaw.model.MQTTConnectionCallback;
import de.haw.mensahaw.model.MQTTManager;
import de.haw.mensahaw.model.MqttClient;
import de.haw.mensahaw.model.QRCallback;
import de.haw.mensahaw.model.ScaleCallBack;

@RunWith(MockitoJUnitRunner.class)
public class MQTTManagerUnitTest {
    private MQTTManager mqttManager;
    private MqttClient mqttClientMock;
    private Database databaseMock;
    @Before
    public void init(){
        mqttManager = new MQTTManager();
        mqttClientMock = mock(MqttClient.class);
        databaseMock = mock(Database.class);
    }
    @Test
    public void MQTTManager_activationTest(){

        assertNotNull(mqttManager.getMqttClient());
    }
    @Test
    public void generalServerConnection_test(){
        mqttClientMock = null;

        mqttManager.connectToServer();
        assertNotNull(mqttManager.getMqttClient());
    }
    @Test
    public void connectingTo_specificServer_withHivemq(){

        final String hostname = "broker.hivemq.com";
        final int portnumber = 1883;
        final String password = "my-password";
        final boolean connectToOnlineServer = true;

        mqttManager.setConnectToOnlineServer(connectToOnlineServer);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.connectToServer();
        verify(mqttClientMock).connectToBroker(anyString(),
                eq(hostname),
                eq(portnumber),
                anyString(),
                eq(password));
    }
    @Test
    public void connectingTo_specificLocalServer(){
        final String hostname = "10.0.2.2";
        final int portnumber = 1883;
        final String password = "my-password";
        final boolean connectToOnlineServer = false;

        mqttManager.setConnectToOnlineServer(connectToOnlineServer);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.connectToServer();
        verify(mqttClientMock).connectToBroker(anyString(),
                eq(hostname),
                eq(portnumber),
                anyString(),
                eq(password));
    }
    @Test
    public void test_connectionCallback(){
        final String hostname = "10.0.2.2";
        final int portnumber = 1883;
        final String passwort = "my-password";
        final boolean connectToOnlineServer = true;
        final MQTTConnectionCallback mockedMQTTConnectionCallback = mock(MQTTConnectionCallback.class);

        mqttManager.setMQTTConnectionCallback(mockedMQTTConnectionCallback);
        mqttManager.setConnectToOnlineServer(connectToOnlineServer);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.connectToServer();
        verify(mqttClientMock).setMqttConnectionCallback(any());

    }
    /*@Test   //TODO non functional
    public void connectToServer4(){
        final String hostname = "10.0.2.2";
        final int portnumber = 1883;
        final String passwort = "my-password";
        final boolean connectToOnlineServer = true;
        final MQTTConnectionCallback mockedMQTTConnectionCallback = mock(MQTTConnectionCallback.class);
        final MQTTConnectionCallback callback = mock(MQTTConnectionCallback.class);

        mqttManager.setMQTTConnectionCallback(mockedMQTTConnectionCallback);
        mqttManager.setConnectToOnlineServer(connectToOnlineServer);
        mqttManager.setMqttClient(mqttClientMock);
        mqttClientMock.setMqttConnectionCallback(callback);

        mqttManager.connectToServer();


        callback.onConnectionSuccess();
        //mockedMQTTConnectionCallback.onConnectionSuccess();

        verify(mockedMQTTConnectionCallback).onConnectionSuccess();
        verify(mqttClientMock).removeMqttConnectionCallback();
    }*/
    @Test
    public void subscribeToWeight(){

        final String topic = databaseMock.SCALE_WEIGHT;

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.subscribeToWeight();
        verify(mqttClientMock).subscribe(eq(topic), any());

    }

    @Test
    public void subscribeToQRCode(){
        final String topic = databaseMock.QRSCANNER_QRCODE;

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.subscribeToQRCode();
        verify(mqttClientMock).subscribe(eq(topic), any());

    }
    @Test
    public void handleWeightTest(){
        final String message = "123";
        final float expectedFloat = Float.parseFloat(message);
        final ScaleCallBack mockedScaleCallback = mock(ScaleCallBack.class);
        mqttManager.setDatabase(databaseMock);
        mqttManager.setScaleCallback(mockedScaleCallback);

        mqttManager.handleWeightMessage(message);

        verify(mockedScaleCallback).onWeightCallback(expectedFloat);

    }
    @Test
    public void handleQRCodeTest(){
        final String message = "1";
        final QRCallback mockedScaleCallback = mock(QRCallback.class);
        mqttManager.setDatabase(databaseMock);
        mqttManager.setQRCallback(mockedScaleCallback);

        mqttManager.handleQRCodeMessage(message);

        verify(mockedScaleCallback).onQRCallback(message);

    }
    @Test
    public void unsubscribeFromWeight(){
        final String topic = databaseMock.SCALE_WEIGHT;

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.unsubscribeFromWeight();
        verify(mqttClientMock).unsubscribe(topic);

    }
    @Test
    public void unsubscribeFromQRCode(){
        final String topic = databaseMock.QRSCANNER_QRCODE;

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.unsubscribeFromQRCode();
        verify(mqttClientMock).unsubscribe(topic);

    }
    @Test
    public void whenQRCodeValuePublished_thenQRCodeValuePublished(){
        final String topic = databaseMock.QRSCANNER_QRCODE;
        final String qrcode = "1";

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.publishQRCode(qrcode);
        verify(mqttClientMock).publish(topic,qrcode);

    }
    @Test
    public void whenPricePublished_thenPricePublished(){
        final String topic = databaseMock.SCALE_PRICE;
        final Float price = 2.5f;

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.publishPrice(price);
        verify(mqttClientMock).publish(topic,price.toString());
    }
    @Test
    public void whenWeightPublished_thenWeightPublished(){
        final String topic = databaseMock.SCALE_WEIGHT;
        final Float weight = 2.5f;

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.publishWeight(weight);
        verify(mqttClientMock).publish(topic,weight.toString());
    }
    @Test
    public void ensureMQTTInizialisation(){
        mqttClientMock = null;

        final String topic = databaseMock.SCALE_WEIGHT;
        final Float weight = 2.5f;

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.publishWeight(weight);
        assertNotNull(mqttManager.getMqttClient());
    }
    @Test
    public void setMQTTConnectionCallback(){
        final MQTTConnectionCallback expectedMockedMQTTConnectionCallback = mock(MQTTConnectionCallback.class);

        mqttManager.setMQTTConnectionCallback(expectedMockedMQTTConnectionCallback);
        final MQTTConnectionCallback actualMQTTConnectionCallback = mqttManager.getMqttConnectionCallback();

        assertEquals(expectedMockedMQTTConnectionCallback, actualMQTTConnectionCallback);
    }
    @Test
    public void removeMQTTConnectionCallback(){
        mqttManager.removeMqttConnectionCallback();
        final MQTTConnectionCallback actualMQTTConnectionCallback = mqttManager.getMqttConnectionCallback();

        assertNull(actualMQTTConnectionCallback);
    }
    @Test
    public void SetMQTTConnectionCallback_thenRemoveMQTTConnectionCallback(){
        final MQTTConnectionCallback expectedMockedMQTTConnectionCallback = mock(MQTTConnectionCallback.class);

        mqttClientMock.setMqttConnectionCallback(expectedMockedMQTTConnectionCallback);
        mqttManager.removeMqttConnectionCallback();

        final MQTTConnectionCallback actualMQTTConnectionCallback = mqttManager.getMqttConnectionCallback();

        assertNull( actualMQTTConnectionCallback);
    }
    @Test
    public void setQRCallback(){
        final QRCallback expectedMockedQRCallback = mock(QRCallback.class);

        mqttManager.setQRCallback(expectedMockedQRCallback);
        final QRCallback actualQRCallback = mqttManager.getQrCallback();

        assertEquals(expectedMockedQRCallback, actualQRCallback);
    }
    @Test
    public void removeQRCallback(){
        mqttManager.removeQRCallback();
        final QRCallback actualQRCallback = mqttManager.getQrCallback();

        assertNull(actualQRCallback);
    }
    @Test
    public void SetQRCallback_thenRemoveQRCallback(){
        final QRCallback expectedMockedQRCallback = mock(QRCallback.class);

        mqttManager.setQRCallback(expectedMockedQRCallback);
        mqttManager.removeQRCallback();
        final QRCallback actualQRCallback = mqttManager.getQrCallback();

        assertNull(actualQRCallback);
    }
    @Test
    public void setScaleCallback(){
        final ScaleCallBack expectedMockedScaleCallback = mock(ScaleCallBack.class);

        mqttManager.setScaleCallback(expectedMockedScaleCallback);
        final ScaleCallBack actualScaleCallback = mqttManager.getScaleCallBack();

        assertEquals(expectedMockedScaleCallback, actualScaleCallback);
    }
    @Test
    public void removeScaleCallback(){
        //final ScaleCallBack expectedMockedScaleCallback = mock(ScaleCallBack.class);

        //mqttManager.setScaleCallback(expectedMockedScaleCallback);
        mqttManager.removeScaleCallback();
        final ScaleCallBack actualScaleCallback = mqttManager.getScaleCallBack();

        assertNull(actualScaleCallback);
    }
    @Test
    public void SetScaleCallback_thenRemoveScaleCallback(){
        final ScaleCallBack expectedMockedScaleCallback = mock(ScaleCallBack.class);

        mqttManager.setScaleCallback(expectedMockedScaleCallback);
        mqttManager.removeScaleCallback();
        final ScaleCallBack actualScaleCallback = mqttManager.getScaleCallBack();

        assertNull(actualScaleCallback);
    }
    @Test
    public void disconnecting_fromServer(){
        mqttManager.setMqttClient(mqttClientMock);
        mqttManager.disconnectFromServer();

        verify(mqttClientMock).disconnect();
    }
    @Test
    public void getDatabase(){
        final Database expectedDatabase = databaseMock;
        mqttManager.setDatabase(expectedDatabase);

        final Database actualDatabase = mqttManager.getDatabase();

        assertEquals(expectedDatabase, actualDatabase);
    }
    @Test
    public void getMQTTClient(){
        final MqttClient expectedClient = new MqttClient();

        mqttManager.setMqttClient(expectedClient);
        assertEquals(expectedClient, mqttManager.getMqttClient());
    }
}
