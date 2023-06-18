package de.haw.mensahaw.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.haw.mensahaw.R;
import de.haw.mensahaw.viewmodel.Checkout_ViewModel;

public class CheckoutActivity extends AppCompatActivity {
    private Button payButton;
    private Button cancelButton;
    public Checkout_ViewModel checkoutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        checkoutViewModel = new ViewModelProvider(this).get(Checkout_ViewModel.class);
        checkoutViewModel.setPriceInView(33.22f);
        checkoutViewModel.setDishNameInView("Hack mit Hack");

        setPayOnClick();
        setCancelOnClick();
        activateDishNameObserver();
        activatePriceObserver();
    }

    private void activatePriceObserver(){
        TextView priceView = findViewById(R.id.price);
        priceView.setText(String.valueOf(checkoutViewModel.getPrice()));
        checkoutViewModel.getPrice().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float updatedPrice) {
                priceView.setText(String.valueOf(updatedPrice));
            }
        });

    }

    private void activateDishNameObserver(){
        TextView dishNameView = findViewById(R.id.dish);
        dishNameView.setText(String.valueOf(checkoutViewModel.getDishName()));
        checkoutViewModel.getDishName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String updatedName) {
                dishNameView.setText(String.valueOf(updatedName));
            }
        });
    }
    private void setCancelOnClick(){
        cancelButton = findViewById(R.id.cancelbutton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openStartMenuView();
            }
        });
    }
    private void setPayOnClick(){
        payButton =  findViewById(R.id.paybutton);
        payButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openCheckoutConfirmationView();
            }
        });
    }
    public void openCheckoutConfirmationView(){
        Intent changeView = new Intent(this, CheckoutConfirmationActivity.class);
        startActivity(changeView);
    }
    public void openStartMenuView() {
        Intent changeView = new Intent(this, ProcessDescriptionActivity.class);
        startActivity(changeView);
    }
}