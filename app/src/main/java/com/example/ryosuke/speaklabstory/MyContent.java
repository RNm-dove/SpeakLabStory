package com.example.ryosuke.speaklabstory;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ryosuke on 17/05/15.
 */

public class MyContent {
    ConsonantSound cSound;
    public ArrayList<Bundle> a;
    public ArrayList<Bundle> ka;
    public ArrayList<Bundle> sa;
    public ArrayList<Bundle> ta;
    public ArrayList<Bundle> na;
    public ArrayList<Bundle> ha;
    public ArrayList<Bundle> ma;
    public ArrayList<Bundle> ya;
    public ArrayList<Bundle> ra;
    public ArrayList<Bundle> wa;
    private final String A_TAG = "line_a";
    private final String KA_TAG = "line_ka";
    private final String SA_TAG = "line_sa";
    private final String TA_TAG = "line_ta";
    private final String NA_TAG = "line_na";
    private final String HA_TAG = "line_ha";
    private final String MA_TAG = "line_ma";
    private final String YA_TAG = "line_ya";
    private final String RA_TAG = "line_ra";
    private final String WA_TAG = "line_wa";


    public MyContent(JSONObject json){
        this.cSound = new ConsonantSound(json);
        try{
            a = cSound.getList(A_TAG);
            ka = cSound.getList(KA_TAG);
            sa = cSound.getList(SA_TAG);
            ta = cSound.getList(TA_TAG);
            na = cSound.getList(NA_TAG);
            ha = cSound.getList(HA_TAG);
            ma = cSound.getList(MA_TAG);
            ya = cSound.getList(YA_TAG);
            ra = cSound.getList(RA_TAG);
            wa = cSound.getList(WA_TAG);
        } catch (JSONException e){
            e.printStackTrace();
        }

    }




    public class ConsonantSound{
        JSONObject json;
        JSONObject subJson;//json objectの中のjsonObject
        JSONArray jArray;
        String content; //jsonoオブジェクトの中身

        ConsonantSound(JSONObject json){
            this.json = json;
        }

        public ArrayList<Bundle> getList(String tag) throws JSONException{

                jArray = json.getJSONArray(tag);

                ArrayList<Bundle> array = new ArrayList<Bundle>();


                for (int i = 0; i < jArray.length(); i++) {

                    subJson = jArray.getJSONObject(i);
                    Iterator<String> iterator = subJson.keys();
                    Bundle bundle = new Bundle();

                    while (iterator.hasNext()) {
                        final String key = iterator.next();
                        bundle.putString(key, subJson.getString(key));
                    }

                    array.add(bundle);
                }


            return array;

        }

    }

}
