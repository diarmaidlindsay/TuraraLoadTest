package jp.pulseanddecibels.tularaloadtest.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import jp.pulseanddecibels.tularaloadtest.R;
import jp.pulseanddecibels.tularaloadtest.util.Constants;
import jp.pulseanddecibels.tularaloadtest.util.LoginManager;
import jp.pulseanddecibels.tularaloadtest.util.Setting;
import jp.pulseanddecibels.tularaloadtest.util.Util;

public class LoginActivity extends AppCompatActivity {

    EditText userNameField;
    EditText passwordField;
    Button loginButton;

    private final Handler HANDLER = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameField = (EditText) findViewById(R.id.field_username);
        passwordField = (EditText) findViewById(R.id.field_password);
        loginButton = (Button) findViewById(R.id.button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        Context context = getApplicationContext();
        Setting setting = new Setting();

        final String userName = Util.getText(userNameField);
        final String password = Util.getText(passwordField);

        setting.saveAccount(context, userName, password);
        setting.saveServerInfo(context, Constants.REMOTE_SERVER);

        LoginManager loginManager = new LoginManager(this);
        loginManager.login(HANDLER);
    }
}
