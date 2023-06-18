package de.haw.mensahaw.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Checkout_ViewModel extends ViewModel  {
    private MutableLiveData<Float> dishPrice = new MutableLiveData<>();
    private MutableLiveData<String> dishName = new MutableLiveData<>();


    public void setPriceInView(Float price) {
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
