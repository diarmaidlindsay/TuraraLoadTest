package jp.pulseanddecibels.tularaloadtest.util;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import jp.pulseanddecibels.tularaloadtest.activity.LoadApplication;
import jp.pulseanddecibels.tularaloadtest.data.AsteriskAccount;

/**
 * Created by Diarmaid Lindsay on 2016/05/20.
 * Copyright Pulse and Decibels 2016
 */
public class VolleyOperator {
    public static synchronized void getAsteriskAccount(final Context context,
                                                       Response.Listener ok,
                                                       Response.ErrorListener err) {

        // 設定により接続先URLを設定する
        String url;
        String sipServer;

        final Setting setting = new Setting();
        sipServer = setting.loadServerInfo(context);
        url = String.format("https://%s:50443/v1_0/address/login.php", sipServer);

        // リクエストを作成
        StringRequest sr = new StringRequest(Request.Method.POST, url, ok, err) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postParams = new HashMap<>();
                postParams.put("user_id", setting.loadUserName(context));
                postParams.put("user_pass", setting.loadPassword(context));
                return postParams;
            }
        };
        sr = setRetryPolicy(sr);

        // キューにリクエストを追加
        RequestQueue requestQueue = LoadApplication.getVolleyRequestQueue(context);
        requestQueue.add(sr);
    }

    public static synchronized void forceLogout(final Context context) {
        // 成功時
        Response.Listener ok = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show();
            }
        };

        // 失敗時
        Response.ErrorListener err = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Logout Failed", Toast.LENGTH_SHORT).show();
            }
        };

        // 設定により接続先URLを設定する
        String url;
        final Setting setting = new Setting();
        String sipServer = setting.loadServerInfo(context);
        url = String.format("https://%s:50443/v1_0/address/logout.php", sipServer);

        // リクエストを作成
        StringRequest sr = new StringRequest(Request.Method.POST, url, ok, err) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postParams = new HashMap<>();
                AsteriskAccount asteriskAccount = setting.loadAsteriskAccount(context);
                postParams.put("user_id", setting.loadUserName(context));
                postParams.put("user_pass", setting.loadPassword(context));
                postParams.put("sip_id", asteriskAccount.sipId);
                return postParams;
            }
        };
        sr = setRetryPolicy(sr);

        // キューにリクエストを追加
        RequestQueue requestQueue = LoadApplication.getVolleyRequestQueue(context);
        requestQueue.add(sr);
    }

    private static synchronized StringRequest setRetryPolicy(StringRequest request) {
        /* タイムアウトを10秒に変更 */
        int timeoutMilliSec = 10 * 1000;

        DefaultRetryPolicy policy = new DefaultRetryPolicy(
                timeoutMilliSec,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );

        request.setRetryPolicy(policy);
        return request;
    }
}
