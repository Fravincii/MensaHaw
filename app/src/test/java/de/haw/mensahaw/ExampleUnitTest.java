package de.haw.mensahaw;

import org.junit.Test;

import de.haw.mensahaw.model.MQTTManager;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void connectToServer_withoutClient_isTrue() {
        MQTTManager mqttManager = new MQTTManager();

        //assertEquals(true, mqttManager.connectToServer(true));
    }

    @Test
    public void connectToServer_withClient_isFalse() {
        MQTTManager mqttManager = new MQTTManager();

       // assertEquals(false, mqttManager.connectToServer(true));
    }
    @Test
    public void dishManagerInit_currentDishesSize_isThree() {

    }
}