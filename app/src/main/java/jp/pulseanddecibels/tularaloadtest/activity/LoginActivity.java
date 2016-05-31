package jp.pulseanddecibels.tularaloadtest.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import jp.pulseanddecibels.tularaloadtest.R;
import jp.pulseanddecibels.tularaloadtest.model.User;
import jp.pulseanddecibels.tularaloadtest.util.LoginManager;
import jp.pulseanddecibels.tularaloadtest.util.Setting;
import jp.pulseanddecibels.tularaloadtest.util.Util;

public class LoginActivity extends AppCompatActivity {

    private final Handler HANDLER = new Handler();
    EditText userNameField;
    EditText passwordField;
    EditText serverField;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameField = (EditText) findViewById(R.id.field_username);
        passwordField = (EditText) findViewById(R.id.field_password);
        serverField = (EditText) findViewById(R.id.field_server);
        loginButton = (Button) findViewById(R.id.button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner_users);
        UsersAdapter adapter = new UsersAdapter(this);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final User user = (User) parent.getItemAtPosition(position);
                userNameField.setText(user.getUserName());
                passwordField.setText(user.getPassword());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void login() {
        Context context = getApplicationContext();
        Setting setting = new Setting();

        final String userName = Util.getText(userNameField);
        final String password = Util.getText(passwordField);
        final String server = Util.getText(serverField);

        setting.saveAccount(context, userName, password);
        setting.saveServerInfo(context, server);

        LoginManager loginManager = new LoginManager(this);
        loginManager.login(HANDLER);
    }

}
