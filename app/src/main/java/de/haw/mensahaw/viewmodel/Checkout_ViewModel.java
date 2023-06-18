package de.haw.mensahaw.viewmodel;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Checkout_ViewModel extends ViewModel  {
    private MutableLiveData<Float> dishPrice = new MutableLiveData<>();


    public void setPrice(Float price) {
        this.dishPrice.setValue(price);
    }

    public MutableLiveData<Float> getPrice() {
        return dishPrice;
    }

}
