package de.haw.mensahaw.model;

import android.app.Application;

public class MensaApplication extends Application {
    private ProcessManager processManager;
    private Database database;

    public MensaApplication() {
        database = new Database();
        processManager = new ProcessManager();
        processManager.setMensaApplication(this);
        processManager.setDatabase(database);
    }

    public Database getDatabase() {
        return database;
    }


    public ProcessManager getProcessManager() {
        return processManager;
    }

    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }



}
