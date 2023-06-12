package de.haw.mensahaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;

import java.nio.charset.StandardCharsets;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private static final String SCALE_WEIGHT = "Scale/Weight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MqttClient mqtt = new MqttClient();
        mqtt.connectToBroker("myClientId",
                "10.0.2.2",
                1883,
                "my-user",
                "my-password");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //subscribed einem Topic
        mqtt.subscribe(SCALE_WEIGHT, (message) -> {
            //Wird gecalled wenn eine Nachricht kommt
            try {
                String convertedMessageContent = new String(message.getPayloadAsBytes(), StandardCharsets.UTF_8);
                receiveData(SCALE_WEIGHT, convertedMessageContent);
            } catch (Exception e) {
                //returns message as bytes
                Log.error(String.format("Message from %s: %s", message.getTopic(), message.getPayloadAsBytes()) + ", error: " + e.getMessage());
            }
        });

        //Vom Arduino geschickt
        mqtt.publish(SCALE_WEIGHT, "0.25");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        mqtt.unsubscribe(SCALE_WEIGHT);
        mqtt.disconnect();






    }


    private void QRCodeSetup(){
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final com.google.zxing.Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
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
        @Override
        protected void onResume() {
            super.onResume();
            //mCodeScanner.startPreview();
        }

        @Override
        protected void onPause() {
            mCodeScanner.releaseResources();
            super.onPause();
        }

    @Override
    protected void onDestroy() {
        mCodeScanner.stopPreview();
        super.onDestroy();
    }

    private void receiveData(String topic, String message) {
        try {
            float value = Float.parseFloat(message);
            if(topic == SCALE_WEIGHT){
                Log.info(String.format("The Scale weight: %skg", message));
            }

        } catch (Exception e) {
            Log.error(String.format("Message on %s was not a float value : %s", topic, message));
        }
    }
}