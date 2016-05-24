package jp.pulseanddecibels.tularaloadtest.util;

import android.widget.EditText;

/**
 * Created by Diarmaid Lindsay on 2016/05/20.
 * Copyright Pulse and Decibels 2016
 */
public class Util {
    public static final String STRING_EMPTY = "";
    public static final int INT_EMPTY = -9999;

    public static String getText(EditText editText) {
        try {
            return editText.getText().toString().trim();
        } catch (Exception ex) {
            return STRING_EMPTY;
        }
    }
}
