package de.haw.mensahaw.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import de.haw.mensahaw.R;
import de.haw.mensahaw.model.MensaApplication;
import de.haw.mensahaw.viewmodel.PayConfirmation_ViewModel;

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
                backToStart();
            }
        });
    }
    public void openStartView(){
        Intent changeView = new Intent(this, StartActivity.class);
        startActivity(changeView);
    }
    private void backToStart(){
        PayConfirmation_ViewModel payConfirmation_viewModel = new ViewModelProvider(this).get(PayConfirmation_ViewModel.class);
        MensaApplication mensaApplication = (MensaApplication) getApplication();

        payConfirmation_viewModel.setProcessManager(mensaApplication.getProcessManager());
        payConfirmation_viewModel.disconnectFromServer();
        openStartView();
    }
}
