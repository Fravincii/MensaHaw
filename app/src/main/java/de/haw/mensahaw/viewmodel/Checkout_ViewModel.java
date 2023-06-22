package de.haw.mensahaw.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Closeable;

import de.haw.mensahaw.model.Dish;
import de.haw.mensahaw.model.ProcessManager;
import de.haw.mensahaw.view.CheckoutActivity;

public class Checkout_ViewModel extends ViewModel  {
    private ProcessManager processManager;
    private CheckoutActivity checkoutActivity;
    public void setCheckoutActivity(CheckoutActivity checkoutActivity) {
        this.checkoutActivity = checkoutActivity;
    }
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
        processManager.setCheckoutViewModel(this);
    }

    private MutableLiveData<Float> dishPrice = new MutableLiveData<>();
    private MutableLiveData<String> dishName = new MutableLiveData<>();

    public void backToWeightingView(){
        checkoutActivity.openStartMenuView();
    }
    public void showCheckout(){
        checkoutActivity.quitLoadingScreen();
    }

    public void setPriceInView(float price) {
        //this.dishPrice.setValue(price);
        this.dishPrice.postValue(price);

    }
    public void setDishNameInView(String name) {
        //this.dishName.setValue(name);
        this.dishName.postValue(name);
    }

    public MutableLiveData<Float> getPrice() {
        return dishPrice;
    }
    public MutableLiveData<String> getDishName() {
        return dishName;
    }
}
