package de.haw.mensahaw.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import de.haw.mensahaw.R;

public class PayConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_confirmation);

        setReturnOnClick();

    }
    private void setReturnOnClick(){
        Button returnButton;
        returnButton = findViewById(R.id.backtomenu);
        returnButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openStartView();
            }
        });
    }
    public void openStartView(){
        Intent changeView = new Intent(this, StartActivity.class);
        startActivity(changeView);
    }
}
