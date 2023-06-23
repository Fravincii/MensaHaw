package de.haw.mensahaw;

import org.junit.Test;
import static org.mockito.Mockito.*;

import de.haw.mensahaw.model.Database;
import de.haw.mensahaw.model.MQTTConnectionCallback;
import de.haw.mensahaw.model.MQTTManager;
import de.haw.mensahaw.model.MensaApplication;
import de.haw.mensahaw.model.ProcessManager;
import static org.junit.Assert.*;

import android.os.CountDownTimer;

import org.junit.*;

public class ProcessManagerUnitTest {

    ProcessManager processManager;
    @Before
    public void init(){
        processManager = new ProcessManager();
    }
    @Test
    public void initMQTT() {
        MQTTManager mqttManagerMock = mock(MQTTManager.class);
        Database databaseMock = mock(Database.class);
        CountDownTimer countDownTimerMock = mock(CountDownTimer.class);

        processManager.setCountDownTimer(countDownTimerMock);
        processManager.setMqttManager(mqttManagerMock);
        processManager.setDatabase(databaseMock);

        processManager.initMQTT();

        verify(mqttManagerMock).setDatabase(any(Database.class));
        verify(mqttManagerMock).connectToServer();
        verify(mqttManagerMock).setMqttConnectionCallback(any(MQTTConnectionCallback.class));
    }
}

