package de.haw.mensahaw.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import de.haw.mensahaw.model.ProcessManager;

public class PayConfirmation_ViewModel extends ViewModel {
    private ProcessManager processManager;
    public ProcessManager getProcessManager() {return processManager;}
    public void setProcessManager(@NonNull ProcessManager processManager) {this.processManager = processManager;}

    public void disconnectFromServer(){
        processManager.disconnectFromServer();
    }

}
