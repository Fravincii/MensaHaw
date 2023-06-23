package de.haw.mensahaw.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import de.haw.mensahaw.R;
import de.haw.mensahaw.model.MQTTManager;
import de.haw.mensahaw.model.MensaApplication;

public class ScanActivity extends AppCompatActivity {
    private MQTTManager mqttManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mqttInit();
        setReturnOnClick();

        if (checkMissingCameraPermission())ActivityCompat.requestPermissions(
                this, new String[] {Manifest.permission.CAMERA},123);
        else startScanning();

    }

    private void mqttInit(){
        mqttManager = new MQTTManager();
        MensaApplication mensaApplication = (MensaApplication)getApplication();
        mqttManager.setDatabase(mensaApplication.getDatabase());
        mqttManager.connectToServer(false);

    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mCodeScanner.releaseResources();
    }
    private void setReturnOnClick() {
        Button returnButton = findViewById(R.id.backtostart);
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openStartMenuView();
            }
        });
    }
    public void openStartMenuView(){
        Intent changeView = new Intent(this, ProcessDescriptionActivity.class);
        startActivity(changeView);
    }

    //region Camera Permission
    private boolean checkMissingCameraPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
                startScanning();
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }
    //endregion
    //region Scanning
    private CodeScanner mCodeScanner;
    private void startScanning(){
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.startPreview();
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        mqttManager.publishQRCode(result.getText());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }
   //endregion


}