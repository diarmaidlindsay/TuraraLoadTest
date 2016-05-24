package jp.pulseanddecibels.tularaloadtest.activity;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Diarmaid Lindsay on 2016/05/20.
 * Copyright Pulse and Decibels 2016
 */
public class LoadApplication extends Application {
    private static Context mContext;
    // ----------- Volley -----------
    private RequestQueue requestQueue;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        System.loadLibrary("pjsua2");
    }

    public static RequestQueue getVolleyRequestQueue(Context context) {
        LoadApplication me = (LoadApplication) context.getApplicationContext();

        if (me.requestQueue == null) {
            me.requestQueue = Volley.newRequestQueue(me);
        }

        return me.requestQueue;
    }
}
