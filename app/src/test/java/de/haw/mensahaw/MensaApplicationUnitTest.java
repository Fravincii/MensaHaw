package de.haw.mensahaw;

import static org.mockito.Mockito.*;

import org.junit.Test;

import de.haw.mensahaw.model.Database;
import de.haw.mensahaw.model.MensaApplication;
import de.haw.mensahaw.model.ProcessManager;
import static org.junit.Assert.*;

import org.junit.*;
public class MensaApplicationUnitTest {
    private MensaApplication mensaApplication;
    @Before
    public void initMensaApp(){
        mensaApplication = new MensaApplication();
    }


    @Test
    public void startOncreate_equalsLaterOncreate(){
        final Database expectedDatabase = new Database();
        mensaApplication.setDatabase(expectedDatabase);
        mensaApplication.onCreate();

        final Database actualDatabase = mensaApplication.getDatabase();
        assertEquals(expectedDatabase, actualDatabase);
    }
    @Test
    public void process_initialised(){
        final ProcessManager processManagerMock = mock(ProcessManager.class);
        final Database databaseMock = mock(Database.class);

        mensaApplication.setDatabase(databaseMock);

        mensaApplication.setProcessManager(processManagerMock);
        mensaApplication.initProcess();

        verify(processManagerMock).setDatabase(any(Database.class));
    }
    @Test
    public void whenProcessManagerCreate_thenProcessManagerCreated(){
        final ProcessManager processManager = new ProcessManager();

        mensaApplication.setProcessManager(processManager);

        assertEquals(processManager,mensaApplication.getProcessManager());

    }
    @Test
    public void whenDatabaseCreate_thenDatabaseCreated(){
        final Database database = new Database();

        mensaApplication.setDatabase(database);

        assertEquals(database,mensaApplication.getDatabase());

    }

}
