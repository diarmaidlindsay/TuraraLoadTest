package jp.pulseanddecibels.tularaloadtest.util;

import android.content.Context;

import jp.pulseanddecibels.tularaloadtest.data.AsteriskAccount;
import jp.pulseanddecibels.tularaloadtest.model.File;

/**
 * Created by Diarmaid Lindsay on 2016/05/20.
 * Copyright Pulse and Decibels 2016
 */
public class Setting {
    private static final String TAG_USER_NAME = "TAG_USER_NAME";
    private static final String TAG_PASSWORD = "TAG_PASSWORD";
    private static final String TAG_SERVER_DOMAIN = "TAG_SERVER_DOMAIN";
    private static final String TAG_ASTERISK_ID = "TAG_ASTERISK_ID";
    private static final String TAG_ASTERISK_PASS = "TAG_ASTERISK_PASS";
    private static final String TAG_ASTERISK_GROUP_ID = "TAG_ASTERISK_GROUP_ID";

    public void saveAccount(Context context, String userName, String password) {
        File.saveData(context, TAG_USER_NAME, userName);
        File.saveData(context, TAG_PASSWORD, password);
    }

    public void saveServerInfo(Context context, String rDomain) {
        File.saveData(context, TAG_SERVER_DOMAIN, rDomain);
    }

    public String loadServerInfo(Context context) {
        return File.getValue(context, TAG_SERVER_DOMAIN);
    }

    public void saveAsteriskAccount(Context context, AsteriskAccount asteriskAccount) {
        File.saveData(context, TAG_ASTERISK_ID, asteriskAccount.sipId);
        File.saveData(context, TAG_ASTERISK_PASS, asteriskAccount.sipPass);
        File.saveData(context, TAG_ASTERISK_GROUP_ID, asteriskAccount.sipGroupId);
    }

    public AsteriskAccount loadAsteriskAccount(Context context) {
        AsteriskAccount asteriskAccount = new AsteriskAccount();
        asteriskAccount.sipId = File.getValue(context, TAG_ASTERISK_ID);
        asteriskAccount.sipPass = File.getValue(context, TAG_ASTERISK_PASS);
        asteriskAccount.sipGroupId = File.getValue(context, TAG_ASTERISK_GROUP_ID);
        return asteriskAccount;
    }

    public String loadUserName(Context context) {
        return File.getValue(context, TAG_USER_NAME);
    }

    public String loadPassword(Context context) {
        return File.getValue(context, TAG_PASSWORD);
    }
}
