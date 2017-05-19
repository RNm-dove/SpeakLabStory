package com.example.ryosuke.speaklabstory;

import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ryosuke on 17/05/18.
 */

public class JsonFactory {


    public JsonFactory() {
    }

    public JSONObject getJsonObject(AssetManager assets, String JSON_FILE){
        InputStream in;
        JSONObject json;
        String jsonString;
        in = null;
        json = null;
        jsonString = null;


        try{
            //JsonFileの読み出し
            in = assets.open(JSON_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            jsonString = stringBuilder.toString();
        } catch (IOException e){
            throw new RuntimeException("Couldn't load jsonFile from asset' " + JSON_FILE + "'");
        } finally {
            if(in != null){
                try {
                    in.close();
                }catch (IOException e){

                }
            }
        }
        try{
            json = new JSONObject(jsonString); //jsonObjectにjsonSringを格納
        }catch (JSONException e){
            e.printStackTrace();
        }

        return json;

    }

}
