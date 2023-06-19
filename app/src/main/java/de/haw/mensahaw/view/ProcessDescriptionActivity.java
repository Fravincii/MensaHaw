package de.haw.mensahaw.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.haw.mensahaw.R;

public class ProcessDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_description);

        setStartOnClick();
        setQROnClick();
    }

    private void setStartOnClick(){
        Button startButton;
        startButton =  findViewById(R.id.startingbutton);
        startButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openWeightingView();
            }
        });
    }
    private void setQROnClick(){
        Button qrButton;
        qrButton = findViewById(R.id.qrbutton);
        qrButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openQR();
            }
        });
    }
    public void openWeightingView(){
        Intent changeView = new Intent(this, WeightingInputActivity.class);
        startActivity(changeView);
    }
    public void openQR(){
        Intent changeView = new Intent(this, ScanActivity.class);
        startActivity(changeView);
    }
}