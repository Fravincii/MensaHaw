package de.haw.mensahaw.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.haw.mensahaw.model.ProcessManager;
import de.haw.mensahaw.view.CheckoutActivity;

public class Checkout_ViewModel extends ViewModel  {
    private ProcessManager processManager;
    public ProcessManager getProcessManager() {return processManager;}
    public void setProcessManager(@NonNull ProcessManager processManager) {this.processManager = processManager;}

    public CheckoutActivity getCheckoutActivity() {return checkoutActivity;}
    private CheckoutActivity checkoutActivity;
    public void setCheckoutActivity(CheckoutActivity checkoutActivity) {this.checkoutActivity = checkoutActivity;}


    public void initMQTT(){
        processManager.setCheckoutViewModel(this);
        processManager.initMQTT();
    }
    private MutableLiveData<Float> dishPrice = new MutableLiveData<>();

    public void setDishPrice(MutableLiveData<Float> dishPrice) {this.dishPrice = dishPrice;}
    public void setDishName(MutableLiveData<String> dishName) {this.dishName = dishName;}

    private MutableLiveData<String> dishName = new MutableLiveData<>();

    public void openPlatePromptView(){checkoutActivity.openStartView();}
    public void showCheckout(){checkoutActivity.runOnUiThread(() -> checkoutActivity.quitLoadingScreen());}

    public void setPriceInView(float price) {this.dishPrice.postValue(price);}
    public void setDishNameInView(String name) {
        this.dishName.postValue(name);
    }

    public MutableLiveData<Float> getPrice() {
        return dishPrice;
    }
    public MutableLiveData<String> getDishName() {
        return dishName;
    }
}
