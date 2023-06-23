package de.haw.mensahaw;

import static org.mockito.Mockito.*;

import org.junit.Test;

import de.haw.mensahaw.model.Database;
import de.haw.mensahaw.model.MensaApplication;
import de.haw.mensahaw.model.ProcessManager;
import static org.junit.Assert.*;

import android.app.Application;

import org.junit.*;
public class MensaApplicationUnitTest {

    MensaApplication mensaApplication;
    @Before
    public void initMensaApp(){
        mensaApplication = new MensaApplication();
    }
    @Test
    public void initProcess(){
        ProcessManager processManagerMock = mock(ProcessManager.class);
        Database databaseMock = mock(Database.class);

        mensaApplication.setDatabase(databaseMock);

        mensaApplication.setProcessManager(processManagerMock);
        mensaApplication.initProcess();

        verify(processManagerMock).setDatabase(any(Database.class));
    }
    @Test
    public void setProcessManager(){
        ProcessManager processManager = new ProcessManager();

        mensaApplication.setProcessManager(processManager);

        assertEquals(processManager,mensaApplication.getProcessManager());

    }
    @Test
    public void setDatabase(){
        Database database = new Database();

        mensaApplication.setDatabase(database);

        assertEquals(database,mensaApplication.getDatabase());

    }

}
