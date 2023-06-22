package de.haw.mensahaw.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import de.haw.mensahaw.R;
import de.haw.mensahaw.model.*;
import de.haw.mensahaw.viewmodel.*;

public class WeightingInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weighting_input);

        processInit();

        setWeightingOnClick();
    }
    private void processInit(){
        MensaApplication mensaApplication = (MensaApplication) getApplication();
        mensaApplication.setProcessManager(new ProcessManager());
    }

    private void setWeightingOnClick(){
        Button weightingButton;
        weightingButton = findViewById(R.id.weightbutton);
        weightingButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                openCheckoutView();
            }
        });
    }
    public void openCheckoutView(){
        Intent changeView = new Intent(this, CheckoutActivity.class);
        startActivity(changeView);
    }
}
