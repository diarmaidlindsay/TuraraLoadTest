package jp.pulseanddecibels.tularaloadtest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.pulseanddecibels.tularaloadtest.R;
import jp.pulseanddecibels.tularaloadtest.util.LoginManager;

/**
 * Created by Diarmaid Lindsay on 2016/05/23.
 * Copyright Pulse and Decibels 2016
 */
public class MainActivity extends AppCompatActivity {
    Button logoutButton;
    Button startTestButton;
    TextView testStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoginManager loginManager = new LoginManager(this);

        logoutButton = (Button) findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus("Logging out...");
                loginManager.logout();
                finish();
            }
        });

        startTestButton = (Button) findViewById(R.id.button_start_test);

        testStatus = (TextView) findViewById(R.id.text_status);
    }

    private void updateStatus(String text) {
        if (testStatus != null) {
            testStatus.setText(text);
        }
    }
}
