package de.haw.mensahaw.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.haw.mensahaw.R;
import de.haw.mensahaw.model.Log;

public class CheckoutActivity extends AppCompatActivity {
    private Button payButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


        payButton =  findViewById(R.id.paybutton);
        cancelButton = findViewById(R.id.cancelbutton);

        payButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openCheckoutConfirmationView();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openStartMenuView();
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