package de.haw.mensahaw.model;

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.unsubscribe.Mqtt3Unsubscribe;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class MqttClient {

    private Mqtt3AsyncClient client;

    public boolean connectToBroker(String identifier, String host, int port, String username, String password) {
        client = com.hivemq.client.mqtt.MqttClient.builder().useMqttVersion3().identifier(identifier).serverHost(host)
                                                  .serverPort(port).buildAsync();

        AtomicBoolean connectionSuccessful = new AtomicBoolean(false);
        client.connectWith().simpleAuth().username(username).password(password.getBytes()).applySimpleAuth().send()
              .whenComplete((connAck, throwable) -> {
                  if (throwable != null) {
                      Log.error("Can't connect to " + host + port + ", Mes: " + throwable.getMessage());
                      connectionSuccessful.set(false);
                      throwable.printStackTrace();
                  } else {
                      Log.info(String.format("Connected to '%s:%d'", host, port));
                      connectionSuccessful.set(true);
                      mqttConnectionCallback.onConnectionSuccess();
                  }
              });
        return connectionSuccessful.get();
    }

    public boolean subscribe(String topic, Consumer<Mqtt3Publish> consumer) {
        AtomicBoolean subscriptionSuccess = new AtomicBoolean(false);
        client.subscribeWith().topicFilter(topic).callback(consumer).send().whenComplete((subAck, throwable) -> {
                  if (throwable != null) {
                      Log.info("Couldn't subscribe to " + topic + ", Error: " + throwable.getMessage());
                      throwable.printStackTrace();
                      subscriptionSuccess.set(false);
                  } else {
                      Log.info(String.format("Subscribed to '%s'", topic));
                      subscriptionSuccess.set(true);
                  }
              });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return subscriptionSuccess.get();
    }

    public boolean publish(String topic, String message) {
        AtomicBoolean publishSuccess = new AtomicBoolean(false);

        client.publishWith().topic(topic).payload(message.getBytes()).send().whenComplete((publish, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                Log.error("Couldn't publish on " + topic + " with: " +message + " ,Error: " + throwable.getMessage());
                publishSuccess.set(false);
            }
            else{
                Log.info("Is publishing on " + topic + " with: " +message);
                publishSuccess.set(true);
            }
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return publishSuccess.get();
    }
    public boolean unsubscribe(String topic) {
        AtomicBoolean unsubscribeSuccess = new AtomicBoolean(false);
        client.unsubscribe(Mqtt3Unsubscribe.builder().topicFilter(topic).build()).whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                Log.error("Couldn't unsubscribe from " + topic + " ,Error: " + throwable.getMessage());
                unsubscribeSuccess.set(false);
            }
            else{
                Log.info(String.format("Unsubscribed from '%s'", topic));
                unsubscribeSuccess.set(true);
            }
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return unsubscribeSuccess.get();
    }

    public boolean disconnect() {
        AtomicBoolean disconnectSuccess = new AtomicBoolean(false);
        String serverHost = client.getConfig().getServerHost();
        client.disconnect().whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                Log.error("Couldn't disconnect from " + serverHost + "! ,Error: " + throwable.getMessage());
                disconnectSuccess.set(false);
            }
            else {
                Log.info(String.format("Disconnected from '%s'", (serverHost)));
                disconnectSuccess.set(true);
            }
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
       return disconnectSuccess.get();
    }

    public void setMqttConnectionCallback(MQTTConnectionCallback mqttConnectionCallback) {
        this.mqttConnectionCallback = mqttConnectionCallback;
    }
    public void removeMqttConnectionCallback() {
        this.mqttConnectionCallback = null;
    }
    private MQTTConnectionCallback mqttConnectionCallback;

}