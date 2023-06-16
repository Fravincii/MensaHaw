package de.haw.mensahaw.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.haw.mensahaw.R;

public class ProcessDescriptionActivity extends AppCompatActivity {
    private Button startButton;

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
    }
    public void openWeightingView(){
        Intent changeView = new Intent(this, WeightingInputActivity.class);
        startActivity(changeView);
    }
}