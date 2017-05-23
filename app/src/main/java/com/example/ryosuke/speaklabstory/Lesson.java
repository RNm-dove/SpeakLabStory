package com.example.ryosuke.speaklabstory;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ryosuke on 17/05/18.
 */

public class Lesson {
    /*
    * このクラスは実際のレッスンを実装したクラスとなります。
     */
    private  MyContent mContent;
    private final String JSON_FILE = "data.json"; //jsonfileの名前 assetフォルダを参照
    private AssetManager assets;
    private LessonUpdater mListener;
    private String targetKANA;
    private HashMap<Integer,Screen> map;
    private Bundle soundBox;

    private Integer CURRENT_STATE = 1; //画面の番号
    private int COUNT_LIMIT =1; // 画面の枚数

    public Lesson(AssetManager assets, Fragment fragment) {

        this.assets = assets;
        JsonFactory jF = new JsonFactory();
        JSONObject json = jF.getJsonObject(assets,JSON_FILE);
        mContent = new MyContent(json);
        if (fragment instanceof LessonUpdater) {
            mListener = (LessonUpdater) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement LessonUpdater");
        }

    }

    public void startLearning(String targetKANA){
        ImageFactory iFactory;
        String sound;
        ArrayList<Bundle> array;
        String word;
        String imageUri;
        String explain;


        if(targetKANA ==null)return;
        sound = KANA.checkSoundOf(targetKANA); //soundは子音のことです。かきくけこ->ka
        if(sound == null){
            throw new RuntimeException("targetKana is not kana or defined in KANA class");
        }
        array = getArraylistOf(sound);      // getArrayListOf()はこのクラスのメソッド　子音に応じたarraylistを返す。
        soundBox = array.get(KANA.checkPositionOf(targetKANA)); //子音の番号に応じたBundleを取得。「せ」であればarray.get(3)

        explain = soundBox.getString("explain"+CURRENT_STATE);
        imageUri = soundBox.getString("imageURI"+CURRENT_STATE);
        COUNT_LIMIT = Integer.valueOf(soundBox.getString("count"));


        Screen firstScreen = new Screen(CURRENT_STATE);
        firstScreen.setViewUri(imageUri);
        firstScreen.setExplain(explain);
        map = new HashMap<Integer,Screen>();
        map.put(CURRENT_STATE,firstScreen);


        if(mListener != null){
            mListener.update(firstScreen); //fragmentにUIをいじってもらいます。
        }



    }

    public void onForwardButtonCliked(){
        CURRENT_STATE++;
        if(CURRENT_STATE <= COUNT_LIMIT){
            Screen nextScreen = nextScreen(CURRENT_STATE);
            if(mListener != null){
                mListener.update(nextScreen);
            }
        } else {
            CURRENT_STATE--;
        }


    }

    public void onBackButtonClicked(){
        CURRENT_STATE--;
        if(CURRENT_STATE >0){
            Screen backScreen = map.get(CURRENT_STATE);

            if(mListener != null){
                mListener.update(backScreen);
            }
        } else {
            //TODO
            CURRENT_STATE++;
        }


    }

    private ArrayList<Bundle> getArraylistOf(String sound){
        ArrayList<Bundle> array = null;
        switch (sound){
            case "a": array = mContent.a; break;
            case "ka": array = mContent.ka; break;
            case "sa": array = mContent.sa; break;
            case "ta": array = mContent.ta; break;
            case "na": array = mContent.na; break;
            case "ha": array = mContent.ha; break;
            case "ma": array = mContent.ma; break;
            case "ya": array = mContent.ya; break;
            case "ra": array = mContent.ra; break;
            case "wa": array = mContent.wa; break;
        }
        return array;
    }

    private Screen nextScreen(int currentstate){
        Screen nextScreen = new Screen(currentstate);
        String textkey = "explain" + currentstate;
        String imagekey = "imageURI" + currentstate;
        nextScreen.setExplain(soundBox.getString(textkey));
        nextScreen.setViewUri(soundBox.getString(imagekey));
        map.put(currentstate,nextScreen);
        return nextScreen;
    }

    public interface LessonUpdater{
        public void update(Screen screen);
    }
}
