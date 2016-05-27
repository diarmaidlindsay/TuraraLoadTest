package jp.pulseanddecibels.tularaloadtest.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import jp.pulseanddecibels.tularaloadtest.activity.MainActivity;
import jp.pulseanddecibels.tularaloadtest.data.AsteriskAccount;
import jp.pulseanddecibels.tularaloadtest.model.JsonParser;

/**
 * Created by Diarmaid Lindsay on 2016/05/20.
 * Copyright Pulse and Decibels 2016
 */
public class LoginManager {
    Context mContext;

    public LoginManager(Context mContext) {
        this.mContext = mContext;
    }

    public void login(final Handler handler) {
        getAsteriskAccount(handler);
    }

    public void logout() {
        VolleyOperator.forceLogout(mContext);
    }

    private void getAsteriskAccount(final Handler handler) {
        // 成功時
        Response.Listener ok = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                // 取得情報より、Asteriskアカウントを解析する
                AsteriskAccount asteriskAccount;
                try {
                    String json = response.toString();
                    asteriskAccount = new JsonParser().parseAsteriskAccount(json);
                } catch (Exception ex) {
                    MainActivity.me.displayToast(ex.getMessage(), Toast.LENGTH_LONG);
                    Log.e("LoginManager", "Error Parsing Asterisk Account 1");
                    return;
                }

                // 取得に成功した場合は、保存
                asteriskAccount.save(mContext);
                getLicenseKey(handler);
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        };

        // 失敗時
        Response.ErrorListener err = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LoginManager", "Error Parsing Asterisk Account 2");
                MainActivity.me.displayToast(error.getMessage(), Toast.LENGTH_LONG);
            }
        };

        // 通信開始
        VolleyOperator.getAsteriskAccount(mContext, ok, err);
    }

    private void getLicenseKey(final Handler handler) {
        // 成功時
        Response.Listener ok = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                // 取得したライセンスキーを登録
                try {
                    String json = response.toString();
                    String licenceKey = new JsonParser().parseLicenceKey(json).trim();
                    MainActivity.LIB_OP.setKey(licenceKey);
                    new Setting().saveLicenceKey(mContext, licenceKey);
                } catch (Exception ex) {
                    MainActivity.me.displayToast("Error while getting License Key : "+ex.getMessage(), Toast.LENGTH_LONG);
                    return;
                }

                // 少し遅らせAsteriskへログイン
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        loginAsterisk(handler);
                    }
                };
                handler.postDelayed(run, 1500);
            }
        };

        // 失敗時
        Response.ErrorListener err = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.me.displayToast("Error while getting license key", Toast.LENGTH_SHORT);
            }
        };

        // 通信開始
        VolleyOperator.getLicenceKey(mContext, ok, err);
    }

    private void loginAsterisk(final Handler handler) {
        Setting setting = new Setting();
        AsteriskAccount asteriskAccount = setting.loadAsteriskAccount(mContext);
        String user = asteriskAccount.sipId;
        String pass = asteriskAccount.sipPass;
        String server = setting.loadServerInfo(mContext);

        // ログイン
        boolean success = MainActivity.LIB_OP.login(user, pass, server);
        if (!success) {
            MainActivity.me.displayToast("Login to Asterisk Server failed", Toast.LENGTH_SHORT);
            return;
        }
    }
}
