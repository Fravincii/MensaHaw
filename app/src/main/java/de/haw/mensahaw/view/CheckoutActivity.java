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
import de.haw.mensahaw.model.Log;
import de.haw.mensahaw.viewmodel.Checkout_ViewModel;

public class CheckoutActivity extends AppCompatActivity {
    private Button payButton;
    private Button cancelButton;
    public Checkout_ViewModel priceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        TextView priceView = findViewById(R.id.price);

        setPayOnClick();
        setCancelOnClick();

        priceViewModel = new ViewModelProvider(this).get(Checkout_ViewModel.class);
        priceViewModel.setPrice(33.22f);
        priceView.setText(String.valueOf(priceViewModel.getPrice()));
        priceViewModel.getPrice().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float updatedPrice) {
                priceView.setText(String.valueOf(updatedPrice));
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