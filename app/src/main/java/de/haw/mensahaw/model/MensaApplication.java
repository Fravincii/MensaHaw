package de.haw.mensahaw.model;

import android.app.Application;

public class MensaApplication extends Application {
    public void setProcessManager(ProcessManager processManager) {this.processManager = processManager;}
    public ProcessManager getProcessManager() {return processManager;}
    public void setDatabase(Database database) {this.database = database;}
    public Database getDatabase() {
        return database;
    }
    private ProcessManager processManager;
    private Database database;
    @Override
    public void onCreate() {
        super.onCreate();
        setDatabase(new Database());
    }
    public void initProcess(){
        if(processManager == null) processManager = new ProcessManager();
        processManager.setDatabase(database);
    }

}
