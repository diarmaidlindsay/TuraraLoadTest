package jp.pulseanddecibels.tularaloadtest.model;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import jp.pulseanddecibels.tularaloadtest.data.AsteriskAccount;
import jp.pulseanddecibels.tularaloadtest.data.TelNumber;
import jp.pulseanddecibels.tularaloadtest.util.Util;


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

    public String parceJsonForSerchName(String json, TelNumber telNum){
        try {
            BaseJsonData baseData = new BaseJsonData(json);
            String jsonStr = baseData.data;

            String rusult			= Util.STRING_EMPTY;
            String group			= Util.STRING_EMPTY;
            String myAddresses		= Util.STRING_EMPTY;
            String name				= Util.STRING_EMPTY;
            String num				= Util.STRING_EMPTY;
            String groupName		= Util.STRING_EMPTY;
            String groupNum			= Util.STRING_EMPTY;


            // 解析
            JSONObject singleJsonObj = new JSONObject(jsonStr);
            group = singleJsonObj.getString("group");
            myAddresses = singleJsonObj.getString("myAddresses");

            JSONArray jsonArray;
            if(!TextUtils.isEmpty(myAddresses) && !"[]".equals(myAddresses)){
                jsonArray = new JSONArray(myAddresses);
                JSONObject groupJsonObj = jsonArray.getJSONObject(0);
                name = groupJsonObj.getString("name");
                num  = groupJsonObj.getString("num");
            }

            if(!TextUtils.isEmpty(group) && !"[]".equals(group)){
                jsonArray = new JSONArray(group);
                JSONObject myAddressesJsonObj = jsonArray.getJSONObject(0);
                groupName = myAddressesJsonObj.getString("groupName");
                groupNum  = myAddressesJsonObj.getString("groupNum");
            }


            // ラベル情報の作成
            if (telNum.isInternal()) {
                rusult = "内線";
                if (!TextUtils.isEmpty(name)) {
                    rusult += "\n" + name;
                }
            } else {
                if (!TextUtils.isEmpty(name)) {
                    rusult += name;
                }
                if (!TextUtils.isEmpty(rusult)) {
                    rusult += "\n";
                }
                if (!TextUtils.isEmpty(num)) {
                    rusult += num;
                }
                if (!TextUtils.isEmpty(groupName) && !groupName.equals("null")) {
                    rusult += "\n(回線: " + groupName + ")";
                } else if (!TextUtils.isEmpty(groupNum) && !groupNum.equals("null")) {
                    rusult += "\n(回線: " + groupNum  + ")";
                }
            }

            rusult = rusult.replace("anonymous", "非通知");
            return rusult;
        } catch (Exception e) {
            return "";
        }
    }

    public String parseLicenceKey(String json) throws JSONException {
        BaseJsonData baseData = new BaseJsonData(json);
        return baseData.data;
    }
}



