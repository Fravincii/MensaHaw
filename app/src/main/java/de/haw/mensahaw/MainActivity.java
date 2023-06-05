package de.haw.mensahaw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.nio.charset.StandardCharsets;
public class MainActivity extends AppCompatActivity {

    private static final String ROOM_TEMPERATURE = "Room/Temperature";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MqttClient mqtt = new MqttClient();
        mqtt.connectToBroker("myClientId", "localhost", 1883, "my-user", "my-password");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        mqtt.subscribe(ROOM_TEMPERATURE, (message) -> {
            try {
                String convertedMessageContent = new String(message.getPayloadAsBytes(), StandardCharsets.UTF_8);
                Log.info(String.format("Message received from topic '%s': '%s'%n", message.getTopic(),
                        convertedMessageContent));
            } catch (Exception e) {
                Log.error(String.format("Message from %s: %s", message.getTopic(), message.getPayloadAsBytes()));
            }
        });
        mqtt.publish(ROOM_TEMPERATURE, "Hallo Welt!");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        mqtt.unsubscribe(ROOM_TEMPERATURE);
        mqtt.disconnect();
    }

    private void receiveData(String topic, String message) {
        Log.info(String.format("Received message '%s' from topic '%s'", message, topic));
        try {
            float value = Float.parseFloat(message);
            //TODO: Hier kann was mit der Nachricht gemacht werden.
        } catch (Exception e) {
            Log.error(String.format("Message on %s was not a float value : %s", topic, message));
        }
    }

}