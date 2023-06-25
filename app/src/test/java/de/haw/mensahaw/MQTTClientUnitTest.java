package de.haw.mensahaw;

import static org.mockito.Mockito.*;

import org.junit.Test;

import de.haw.mensahaw.model.Database;
import de.haw.mensahaw.model.MQTTConnectionCallback;
import de.haw.mensahaw.model.MQTTManager;
import de.haw.mensahaw.model.MensaApplication;
import de.haw.mensahaw.model.MqttClient;
import de.haw.mensahaw.model.ProcessManager;
import static org.junit.Assert.*;

import android.app.Application;

import androidx.annotation.NonNull;

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3SubscribeBuilder;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public class MQTTClientUnitTest {

    //TODO: connect, sub, pub, unsub, disco???
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
    @Test
    public void subscribe(){
        final Mqtt3AsyncClient mqtt3AsyncClientMock = mock(Mqtt3AsyncClient.class);

        mqttClient.setClient(mqtt3AsyncClientMock);
        final Database database = new Database();
        final String topic = database.SCALE_WEIGHT;

        mqttClient.subscribe(topic,message ->{

        });
        verify(mqtt3AsyncClientMock).subscribeWith().topicFilter(topic).callback(any()).send();
    }

    @Test
    public void setClient(){

        Mqtt3AsyncClient predictedMockedMQTT3AsyncClient = mock(Mqtt3AsyncClient.class);
        mqttClient.setClient(predictedMockedMQTT3AsyncClient);
        Mqtt3AsyncClient actualMQTT3AsyncClient = mqttClient.getClient();

        assertEquals(predictedMockedMQTT3AsyncClient, actualMQTT3AsyncClient);
    }
    @Test
    public void setMQTTConnectionCallback(){
        MQTTConnectionCallback expectedMockedMQTTConnectionCallback = mock(MQTTConnectionCallback.class);

        mqttClient.setMqttConnectionCallback(expectedMockedMQTTConnectionCallback);
        MQTTConnectionCallback actualMQTTConnectionCallback = mqttClient.getMqttConnectionCallback();

        assertEquals(expectedMockedMQTTConnectionCallback, actualMQTTConnectionCallback);
    }
    @Test
    public void setMQTTConnectionCallback1(){
        MQTTConnectionCallback expectedMockedMQTTConnectionCallback = mock(MQTTConnectionCallback.class);

        mqttClient.setMqttConnectionCallback(expectedMockedMQTTConnectionCallback);
        mqttClient.removeMqttConnectionCallback();

        MQTTConnectionCallback actualMQTTConnectionCallback = mqttClient.getMqttConnectionCallback();

        assertNull(actualMQTTConnectionCallback);
    }
}
