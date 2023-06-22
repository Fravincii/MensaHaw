package de.haw.mensahaw.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.haw.mensahaw.R;
import de.haw.mensahaw.viewmodel.*;
import de.haw.mensahaw.model.MensaApplication;

public class CheckoutActivity extends AppCompatActivity {
    public Checkout_ViewModel checkoutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


        checkoutViewModel = new ViewModelProvider(this).get(Checkout_ViewModel.class);

        MensaApplication mensaApplication = (MensaApplication) getApplication();
        checkoutViewModel.setProcessManager(mensaApplication.getProcessManager());
        checkoutViewModel.setCheckoutActivity(this);
        checkoutViewModel.init();

        //getApplication().getConnect()
        // viewmodel.setConnect(connection);


        deactivateButtons();
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
        Button cancelButton;
        cancelButton = findViewById(R.id.cancelbutton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openStartMenuView();
            }
        });
    }
    private void setPayOnClick(){
        Button payButton;
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
        Toast.makeText(this, "Couldn't connect to Server, please check your internet connection!", Toast.LENGTH_LONG).show();
        Intent changeView = new Intent(this, ProcessDescriptionActivity.class);
        startActivity(changeView);
    }
    public void quitLoadingScreen(){
        View loadingScreen = findViewById(R.id.loadingscreen);
        View loadingScreenText = findViewById(R.id.loadingtexthead);
        View loadingScreenHead = findViewById(R.id.loadinscreentext);
        View payButton = findViewById(R.id.paybutton);
        View cancelButton = findViewById(R.id.cancelbutton);

        loadingScreen.setVisibility(View.GONE);
        loadingScreenText.setVisibility(View.GONE);
        loadingScreenHead.setVisibility(View.GONE);
        payButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
    }
    public void deactivateButtons(){
        View paybutton = findViewById(R.id.paybutton);
        View cancelbutton = findViewById(R.id.cancelbutton);

        paybutton.setVisibility(View.GONE);
        cancelbutton.setVisibility(View.GONE);
    }
}