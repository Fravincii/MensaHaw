package de.haw.mensahaw.model;

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.unsubscribe.Mqtt3Unsubscribe;

import java.util.function.Consumer;

public class MqttClient {
    private Mqtt3AsyncClient client;
    public void setClient(Mqtt3AsyncClient client) {this.client = client;}
    public Mqtt3AsyncClient getClient() {return client;}

    public void connectToBroker(String identifier, String host, int port, String username,  String password) {
        if(client == null) setClient(com.hivemq.client.mqtt.MqttClient.builder().useMqttVersion3().identifier(identifier).serverHost(host)
                                                  .serverPort(port).buildAsync());

        client.connectWith().simpleAuth().username(username).password(password.getBytes()).applySimpleAuth().send()
              .whenComplete((connAck, throwable) -> {
                  if (throwable != null) {
                      Log.error("Can't connect to " + host + port + ", Mes: " + throwable.getMessage());
                      throwable.printStackTrace();
                  } else {
                      Log.info(String.format("Connected to '%s:%d'", host, port));
                      mqttConnectionCallback.onConnectionSuccess();
                  }
              });
    }

    public void subscribe(String topic, Consumer<Mqtt3Publish> consumer) {
        client.subscribeWith().topicFilter(topic).callback(consumer).send().whenComplete((subAck, throwable) -> {
                  if (throwable != null) {
                      Log.info("Couldn't subscribe to " + topic + ", Error: " + throwable.getMessage());
                      throwable.printStackTrace();
                  } else {
                      Log.info(String.format("Subscribed to '%s'", topic));
                  }
              });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void publish(String topic, String message) {
        client.publishWith().topic(topic).payload(message.getBytes()).send().whenComplete((publish, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                Log.error("Couldn't publish on " + topic + " with: " +message + " ,Error: " + throwable.getMessage());
            }
            else{
                Log.info("Is publishing on " + topic + " with: " +message);
            }
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void unsubscribe(String topic) {
        client.unsubscribe(Mqtt3Unsubscribe.builder().topicFilter(topic).build()).whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                Log.error("Couldn't unsubscribe from " + topic + " ,Error: " + throwable.getMessage());
            }
            else{
                Log.info(String.format("Unsubscribed from '%s'", topic));
            }
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        String serverHost = client.getConfig().getServerHost();
        client.disconnect().whenComplete((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                Log.error("Couldn't disconnect from " + serverHost + "! ,Error: " + throwable.getMessage());
            }
            else {
                Log.info(String.format("Disconnected from '%s'", (serverHost)));
            }
        });
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private MQTTConnectionCallback mqttConnectionCallback;
    public void setMqttConnectionCallback(MQTTConnectionCallback mqttConnectionCallback) {this.mqttConnectionCallback = mqttConnectionCallback;}
    public MQTTConnectionCallback getMqttConnectionCallback() {return mqttConnectionCallback;}
    public void removeMqttConnectionCallback() {
        this.mqttConnectionCallback = null;
    }

}