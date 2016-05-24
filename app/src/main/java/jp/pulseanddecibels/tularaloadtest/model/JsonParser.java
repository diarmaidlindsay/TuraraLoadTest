package jp.pulseanddecibels.tularaloadtest.model;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import jp.pulseanddecibels.tularaloadtest.data.AsteriskAccount;


/**
 */
public class JsonParser {

    /**
     * Asteriskのアカウントを解析する
     *
     * @param json 解析するJSON
     * @return Asteriskのアカウント
     * @throws JSONException 解析中の例外
     */
    public AsteriskAccount parseAsteriskAccount(String json) throws JSONException {
        BaseJsonData baseData = new BaseJsonData(json);

        Type collectionType = new TypeToken<ArrayList<AsteriskAccount>>() {
        }.getType();
        ArrayList<AsteriskAccount> list = new Gson().fromJson(baseData.data, collectionType);
        return list.get(0);
    }

    /**
     * パルスでのJSONの基本階層
     * <p/>
     * ※ 問題なくデータが取得できた場合は、isDataOkがtrueとなる
     */
    private class BaseJsonData {

        /**
         * データが正しい値で有るか
         */
        public boolean isDataOk;

        /**
         * 実データ
         */
        public String data;


        /**
         * コンストラクタ
         */
        public BaseJsonData(String json) throws JSONException {
            JSONObject jo = new JSONObject(json);
            this.isDataOk = jo.getBoolean("result");

            // falseの場合は例外とする
            if (!isDataOk) {
                throw new RuntimeException(jo.getString("error"));
            }

            this.data = jo.getString("data");
        }
    }
}



