package de.haw.mensahaw.model;

import android.app.Application;
import android.util.Log;

public class MensaApplication extends Application {
    private ProcessManager processManager;
    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = new Database();
    }
    public MensaApplication() {

    }

    public Database getDatabase() {
        return database;
    }


    public ProcessManager getProcessManager() {
        return processManager;
    }

    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
        processManager.setMensaApplication(this);
        processManager.setDatabase(database);
    }



}
