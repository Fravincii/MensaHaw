package de.haw.mensahaw.model;

import android.app.Application;

public class MensaApplication extends Application {
    private ProcessManager processManager;
    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = new Database();
    }
    public Database getDatabase() {
        return database;
    }
    public ProcessManager getProcessManager() {return processManager;}
    public void initProcess(){
        processManager = new ProcessManager();
        processManager.setDatabase(database);
    }



}
