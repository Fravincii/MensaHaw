package de.haw.mensahaw.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import de.haw.mensahaw.R;
import de.haw.mensahaw.model.ProcessManager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ProcessManager processManager = new ProcessManager();
        //processManager.startAsUser();

    }

}