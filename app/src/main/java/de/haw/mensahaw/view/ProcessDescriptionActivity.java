package de.haw.mensahaw.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.haw.mensahaw.R;
import de.haw.mensahaw.model.Log;

public class ProcessDescriptionActivity extends AppCompatActivity {
    private Button startButton;
    private Button devButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_description);

        startButton =  findViewById(R.id.startingbutton);
        startButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openWeightingView();
            }
        });

        devButton = findViewById(R.id.developerbutton);
        devButton.setOnClickListener(new View.OnClickListener(){
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