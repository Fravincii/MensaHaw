package de.haw.mensahaw;

import static org.mockito.Mockito.*;

import org.junit.Test;

import de.haw.mensahaw.model.Database;
import de.haw.mensahaw.model.MQTTConnectionCallback;
import de.haw.mensahaw.model.MensaApplication;
import de.haw.mensahaw.model.MqttClient;
import de.haw.mensahaw.model.ProcessManager;
import static org.junit.Assert.*;

import android.app.Application;

import androidx.annotation.NonNull;

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;

import org.junit.*;
public class MQTTClientUnitTest {

    private MqttClient mqttClient;
    @Before
    public void init(){
        mqttClient = new MqttClient();
    }
    @Test
    public void connectToBroker(){
        final Mqtt3AsyncClient mqtt3AsyncClientMock = mock(Mqtt3AsyncClient.class);
        mqttClient.setClient(mqtt3AsyncClientMock);
        final MQTTConnectionCallback mqttConnectionCallbackMock = mock(MQTTConnectionCallback.class);

        final String identifier = "testid";
        final String host = "10.0.2.2";
        final int port = 1883;
        final String username = "testUser";
        final String password = "my-password";

        mqttClient.setClient(mqtt3AsyncClientMock);
        mqttClient.setMqttConnectionCallback(mqttConnectionCallbackMock);
/*
        mqttClient.connectToBroker(identifier,host,port,username,password);
        verify(mqtt3AsyncClientMock).connectWith().simpleAuth().username(username).password(password.getBytes()).applySimpleAuth().send()
                .whenComplete(any());*/
    }
}
