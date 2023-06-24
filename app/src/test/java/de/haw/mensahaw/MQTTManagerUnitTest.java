package de.haw.mensahaw;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import de.haw.mensahaw.model.Database;
import de.haw.mensahaw.model.MQTTManager;
import de.haw.mensahaw.model.MqttClient;
@RunWith(MockitoJUnitRunner.class)
public class MQTTManagerUnitTest {

    private MQTTManager mqttManager;
    private MqttClient mqttClientMock;
    private Database databaseMock;
    private Log log;
    @Before
    public void init(){
        mqttManager = new MQTTManager();
        mqttClientMock = mock(MqttClient.class);
        databaseMock = mock(Database.class);
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
    public void publishQRCode(){
        final String topic = databaseMock.QRSCANNER_QRCODE;
        final String qrcode = "1";

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);



        mqttManager.publishQRCode(qrcode);
        verify(mqttClientMock).publish(topic,qrcode);

    }
    @Test
    public void publishPrice(){
        final String topic = databaseMock.SCALE_PRICE;
        final Float price = 2.5f;

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.publishPrice(price);
        verify(mqttClientMock).publish(topic,price.toString());
    }
    @Test
    public void publishWeight(){
        final String topic = databaseMock.SCALE_WEIGHT;
        final Float weight = 2.5f;

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.publishWeight(weight);
        verify(mqttClientMock).publish(topic,weight.toString());
    }
    @Test
    public void ensureMQTTinit(){
        mqttClientMock = null;

        final String topic = databaseMock.SCALE_WEIGHT;
        final Float weight = 2.5f;

        mqttManager.setDatabase(databaseMock);
        mqttManager.setMqttClient(mqttClientMock);

        mqttManager.publishWeight(weight);
        assertNotNull(mqttManager.getMqttClient());
    }
}
