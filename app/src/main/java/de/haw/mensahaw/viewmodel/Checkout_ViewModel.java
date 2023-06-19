package de.haw.mensahaw.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.haw.mensahaw.model.Dish;
import de.haw.mensahaw.model.ProcessManager;

public class Checkout_ViewModel extends ViewModel  {
    private ProcessManager processManager;
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
        processManager.setCheckoutViewModel(this);
    }

    private MutableLiveData<Float> dishPrice = new MutableLiveData<>();
    private MutableLiveData<String> dishName = new MutableLiveData<>();


    public void setPriceInView(float price) {
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
