package de.haw.mensahaw.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.haw.mensahaw.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setStartOnClick();
        setHiddenToScannerOnClick();
    }
    private void setStartOnClick(){
        Button startButton;
        startButton =  findViewById(R.id.startingbutton);
        startButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openPlatePromptView();
            }
        });
    }
    private void setHiddenToScannerOnClick(){
        Button toScannerButton;
        toScannerButton = findViewById(R.id.toscannerbutton);
        toScannerButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openScannerView();
            }
        });
    }
    public void openPlatePromptView(){
        Intent changeView = new Intent(this, PlatePromptActivity.class);
        startActivity(changeView);
    }
    public void openScannerView(){
        Intent changeView = new Intent(this, ScannerActivity.class);
        startActivity(changeView);
    }
}