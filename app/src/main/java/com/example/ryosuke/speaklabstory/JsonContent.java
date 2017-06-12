package com.example.ryosuke.speaklabstory;

import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ryosuke on 17/05/15.
 */

public class JsonContent extends Content{
    JSONObject json;
    JSONObject subJson;//json objectの中のjsonObject
    JSONArray jArray;


    public JsonContent(JSONObject json){
        this.json = json;
    }

    /**
     * ほしい仮名をわたしてjsonファイルから該当のデータをarraylist<Bundle>で返す。
     * @param targetKANA　「あ」などの仮名
     * @return
     * @throws JSONException
     */
    @Override
    public ArrayList<Bundle> getListOf(String targetKANA) {

        try{
            jArray = json.getJSONArray(targetKANA);
        }catch (JSONException e){
            e.printStackTrace();
        }

        ArrayList<Bundle> array = new ArrayList<Bundle>();


        for (int i = 0; i < jArray.length(); i++) {

            try{
                subJson = jArray.getJSONObject(i);
            }catch (JSONException e){
                e.printStackTrace();
            }

            Iterator<String> iterator = subJson.keys();
            Bundle bundle = new Bundle();

            while (iterator.hasNext()) {
                final String key = iterator.next();
                try{
                    if(key.equals("texts")){
                        ArrayList<String> list = new ArrayList<String>();

                        String[] texts = subJson.getString(key).split(",");
                        for(String text :texts){
                            list.add(text);
                        }
                        bundle.putStringArrayList(key,list);
                    } else {
                        bundle.putString(key, subJson.getString(key));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }

            array.add(bundle);
        }


        return array;

    }



}
