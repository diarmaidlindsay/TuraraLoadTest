package jp.pulseanddecibels.tularaloadtest.activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.pjsip.pjsua2.CallInfo;

import jp.pulseanddecibels.tularaloadtest.R;
import jp.pulseanddecibels.tularaloadtest.data.AsteriskAccount;
import jp.pulseanddecibels.tularaloadtest.data.TelNumber;
import jp.pulseanddecibels.tularaloadtest.model.IncomingCallControl;
import jp.pulseanddecibels.tularaloadtest.model.IncomingCallItem;
import jp.pulseanddecibels.tularaloadtest.model.JsonParser;
import jp.pulseanddecibels.tularaloadtest.model.LibOperator;
import jp.pulseanddecibels.tularaloadtest.model.SoundPlayer;
import jp.pulseanddecibels.tularaloadtest.pjsip.TularaCall;
import jp.pulseanddecibels.tularaloadtest.util.LoginManager;
import jp.pulseanddecibels.tularaloadtest.util.Setting;
import jp.pulseanddecibels.tularaloadtest.util.Util;
import jp.pulseanddecibels.tularaloadtest.util.VibratorControl;
import jp.pulseanddecibels.tularaloadtest.util.VolleyOperator;

/**
 * Created by Diarmaid Lindsay on 2016/05/23.
 * Copyright Pulse and Decibels 2016
 */
public class MainActivity extends AppCompatActivity {
    public final static LibOperator LIB_OP = new LibOperator();
    private static final Handler MAIN_HANDLER = new Handler();
    public static MainActivity me = null;
    static AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            //do nothing
        }
    };
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    Button logoutButton;
    ToggleButton speakerButton;
    Button hangupButton;
    Button answerButton;
    Button callButton;
    TextView testStatus;
    IncomingCallItem callItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        me = this;

        final LoginManager loginManager = new LoginManager(this);

        logoutButton = (Button) findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus("Logging out...");
                loginManager.logout();
                LIB_OP.logout();
                finish();
            }
        });

        speakerButton = (ToggleButton) findViewById(R.id.button_speaker);
        speakerButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LIB_OP.speeker(me.getApplicationContext(), isChecked);
            }
        });

        hangupButton = (Button) findViewById(R.id.button_hangup);
        hangupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LIB_OP.endCall();
            }
        });

        answerButton = (Button) findViewById(R.id.button_answer);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callItem != null) {
                    IncomingCallControl.INSTANCE.answerTo(callItem.callId, callItem.call);
                } else {
                    displayToast("No call to answer", Toast.LENGTH_SHORT);
                }
            }
        });

        View dialpad = findViewById(R.id.dialpad_content);
        Util.initDialpad(dialpad);

        assert dialpad != null;
        final TextView numberField = (TextView) dialpad.findViewById(R.id.field_number_entry);

        callButton = (Button) dialpad.findViewById(R.id.button_call);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelNumber telNum = new TelNumber(numberField.getText().toString());
                if(telNum.isEmpty()) {
                    displayToast("The number is empty", Toast.LENGTH_SHORT);
                } else {
                    updateStatus("Calling...");
                    LIB_OP.startCall(telNum, MainActivity.this);
                }
            }
        });

        testStatus = (TextView) findViewById(R.id.text_status);
        resetStatus();
        changeButtonState(false);
    }

    public void updateStatus(final String text) {
        if (testStatus != null) {
            MAIN_HANDLER.post(new Runnable() {
                public void run()
                {
                    testStatus.setText(text);
                }
            });
        }
    }

    public void resetStatus() {
        resetStatus(null);
    }

    public void resetStatus(final String status) {
        Setting setting = new Setting();
        AsteriskAccount asteriskAccount = setting.loadAsteriskAccount(this);
        final String user = asteriskAccount.sipId;

        MAIN_HANDLER.post(new Runnable() {
            public void run() {
                if (status != null) {
                    updateStatus(status + ", " + user + " waiting for call...");
                } else {
                    updateStatus(user + " waiting for call...");
                }
            }
        });
    }

    public void displayToast(final String message, final int length) {
        MAIN_HANDLER.post(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, message, length).show();
            }
        });
    }

    public void setEventStartCall() {
        SoundPlayer.INSTANCE.stop();
        getAudioFocus(me);
        updateStatus("On call");

        changeButtonState(true);
    }

    public void setEventEndCall() {
        resetStatus("Call ended");
        SoundPlayer.INSTANCE.startSyuuryou(me.getApplicationContext());
        releaseAudioFocus(me);

        changeButtonState(false);
    }

    private void changeButtonState(final boolean onCall) {
        MAIN_HANDLER.post(new Runnable() {
            public void run() {
                if (onCall) {
                    speakerButton.setEnabled(true);
                    speakerButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_dark));
                    hangupButton.setEnabled(true);
                    hangupButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_dark));
                    answerButton.setEnabled(false);
                    answerButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
                    logoutButton.setEnabled(false);
                    logoutButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
                    callButton.setEnabled(false);
                    callButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
                } else {
                    speakerButton.setEnabled(false);
                    speakerButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
                    hangupButton.setEnabled(false);
                    hangupButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
                    answerButton.setEnabled(true);
                    answerButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_dark));
                    logoutButton.setEnabled(true);
                    logoutButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_dark));
                    callButton.setEnabled(true);
                    callButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_dark));
                }
            }
        });
    }

    private void getAudioFocus(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        // Request audio focus for playback
        am.requestAudioFocus(mOnAudioFocusChangeListener,
                // Voice call Stream
                AudioManager.STREAM_VOICE_CALL,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
    }

    private void releaseAudioFocus(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        am.abandonAudioFocus(mOnAudioFocusChangeListener);
    }

    public void onIncomingCall(final TularaCall call) {
        Log.d(LOG_TAG, "onIncomingCall 2");

        try {
            CallInfo info = call.getInfo();
            final String callId = Integer.toString(call.getId());
            final String remoteContact = info.getRemoteContact();
            final String remoteUri = info.getRemoteUri();
            final String localUri = info.getLocalUri();
            String callIdString = info.getCallIdString(); //maybe can be used for something
            //force delete to ensure we don't get PJSIP non-registered thread crash
            info.delete();

            //"2809" <sip:2809@192.168.1.230>
            final TelNumber telNum = new TelNumber(remoteContact.substring(remoteContact.indexOf(":")+1, remoteContact.indexOf("@")));

            final Response.ErrorListener err = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(LOG_TAG, "onErrorResponse");
                    IncomingCallItem callItem =
                            new IncomingCallItem(callId, remoteContact, telNum, remoteUri, localUri, remoteContact, call);
                    me.onIncomingCall(callItem);
                }
            };

            // 成功時
            final Response.Listener ok = new Response.Listener() {
                @Override
                public void onResponse(final Object response) {
                    Log.d(LOG_TAG, "Response.Listener ok1");
                    String name = null;
                    try {
                        String json = response.toString();
                        name = new JsonParser().parceJsonForSerchName(json, telNum);
                    } catch (Exception ex) { }
                    if(TextUtils.isEmpty(name)){
                        name = remoteContact;
                    }

                    IncomingCallItem callItem =
                            new IncomingCallItem(callId, remoteContact, telNum, remoteUri, localUri, name, call);
                    me.onIncomingCall(callItem);
                    Log.d(LOG_TAG, "Response.Listener ok2");
                }
            };

            Log.d(LOG_TAG, "onIncomingCall 3");
            VolleyOperator.resolverName(me.getApplicationContext(), telNum, ok, err);
            Log.d(LOG_TAG, "onIncomingCall 4");
            //if we don't gc before answering the call, and gc happens when call is answered, the app will crash!
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOnCall() {
        return testStatus.getText().equals("In Call");
    }

    private void onIncomingCall(IncomingCallItem callItem){
        Log.d("MainService", "onIncomingCall 1");
        boolean isFirstIncomingCall = !IncomingCallControl.INSTANCE.isDuringIncomingCall();

        // 通話中は、画面表示はしない
        if (isOnCall()) {
            //send busy signal if we're already on a call
            LIB_OP.busyCall(callItem.call);
            return;
        }

        IncomingCallControl.INSTANCE.addItem(callItem);

        // 初回着信時は、着信画面を表示
        if (isFirstIncomingCall) {
//            me.showIncomingCallActivity();
            updateStatus("Incoming Call from " + callItem.telNum.getBaseString());
            this.callItem = callItem;
            // 2回め以降の着信時は、着信画面を更新
        } else {
            displayToast("Ignored incoming call since there is already a call incoming", Toast.LENGTH_SHORT);
//            IncomingCallActivity.resetIncomingCallList();
        }
    }

    public void OnIncomingCallRingingStop(int callId) {
//			android.util.Log.e(Util.LOG_TAG, "--OnIncomingCallRingingStop--");

        // 切れた着信を削除
        IncomingCallControl.INSTANCE.removeItem(Integer.toString(callId));

        // 着信が無くなっていれば、
//		if (!IncomingCallControl.INSTANCE.isDuringIncomingCall()) {
        // 呼び出し用バイブレーター終了
        VibratorControl.stop(me.getApplicationContext());

        // 着信音を止める
        SoundPlayer.INSTANCE.stop();

        // 着信画面を終了させる
//        IncomingCallActivity.end();

        // 他の着信が残っていれば、着信リストを更新
//		}
//		else {
//			IncomingCallActivity.resetIncomingCallList();
//		}
    }

    @Override
    public void onBackPressed() {
        if (logoutButton.isEnabled()) {
            logoutButton.performClick();
        }
    }
}
