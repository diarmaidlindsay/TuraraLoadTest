package jp.pulseanddecibels.tularaloadtest.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import jp.pulseanddecibels.tularaloadtest.util.Util;

/**
 * データ保存用ファイル
 */
public class File {

    private final static String PREFERENCES_FILE_NAME = "TULARAPreferencesFile";    // プレファレンスファイル名

    /**
     * プレファレンスファイルに保存する
     *
     * @param context コンテキスト
     * @param key     キー
     * @param value   値
     */
    public static void saveData(Context context, String key, String value) {
        SharedPreferences saveFile = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);

        Editor saveFileEditor = saveFile.edit();
        if (saveFileEditor == null) {
            return;
        }

        saveFileEditor.putString(key, value);
        saveFileEditor.commit();
    }

    /**
     * プレファレンスファイルに保存する
     *
     * @param context コンテキスト
     * @param key     キー
     * @param value   値
     */
    public static void saveData(Context context, String key, int value) {
        SharedPreferences saveFile = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);

        Editor saveFileEditor = saveFile.edit();
        if (saveFileEditor == null) {
            return;
        }

        saveFileEditor.putInt(key, value);
        saveFileEditor.commit();
    }

    /**
     * プレファレンスファイルよりキーをもとに値を取得する
     *
     * @param context コンテキスト
     * @param key     キー
     * @return 値
     */
    public static String getValue(Context context, String key) {
        SharedPreferences saveFile = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        String str = saveFile.getString(key, Util.STRING_EMPTY);
        return str.trim();
    }

    /**
     * プレファレンスファイルよりキーをもとに値を取得する
     *
     * @param context コンテキスト
     * @param key     キー
     * @return 値
     */
    public static int getInt(Context context, String key) {
        SharedPreferences saveFile = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return saveFile.getInt(key, Util.INT_EMPTY);
    }
}