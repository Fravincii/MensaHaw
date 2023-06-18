package de.haw.mensahaw.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import de.haw.mensahaw.R;

public class WeightingInputActivity extends AppCompatActivity {
    private Button weightingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weighting_input);

        setWeightingOnClick();
    }
    private void setWeightingOnClick(){
        weightingButton =  findViewById(R.id.weightbutton);
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
