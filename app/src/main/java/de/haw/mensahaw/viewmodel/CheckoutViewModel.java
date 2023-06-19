package de.haw.mensahaw.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.haw.mensahaw.model.ProcessManager;

public class CheckoutViewModel extends ViewModel  {
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    ProcessManager processManager;
    private MutableLiveData<Float> dishPrice = new MutableLiveData<>();
    private MutableLiveData<String> dishName = new MutableLiveData<>();




    public void setPrice(Float price) {
        this.dishPrice.setValue(price);
    }
    public void setDishNameInView(String name) {
        this.dishName.setValue(name);
    }

    public MutableLiveData<Float> getPrice() {
        return dishPrice;
    }
    public MutableLiveData<String> getDishName() {
        return dishName;
    }
}
