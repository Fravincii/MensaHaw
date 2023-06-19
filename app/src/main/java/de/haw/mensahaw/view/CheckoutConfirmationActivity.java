package de.haw.mensahaw.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import de.haw.mensahaw.R;

public class CheckoutConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_confirmation);

        setReturnOnClick();

    }
    private void setReturnOnClick(){
        Button returnButton;
        returnButton = findViewById(R.id.backtomenu);
        returnButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openStartMenuView();
            }
        });
    }
    public void openStartMenuView(){
        Intent changeView = new Intent(this, ProcessDescriptionActivity.class);
        startActivity(changeView);
    }
}
