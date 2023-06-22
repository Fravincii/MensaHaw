package de.haw.mensahaw;

import org.junit.Test;

import static org.junit.Assert.*;

import de.haw.mensahaw.model.MQTTManager;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class QRCodeManagerUnitTest {
    @Test
    public void connectToServer_withoutClient_isTrue() {
        MQTTManager mqttManager = new MQTTManager();

        //assertEquals(true, mqttManager.connectToServer(true));
    }
}
