package com.example.ryosuke.speaklabstory;

import android.content.Context;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
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
    private Screen mScreen;
    private Context mContext;

    private Integer CURRENT_STATE = 1; //画面の番号
    private int COUNT_LIMIT =1; // 画面の枚数

    public Lesson(AssetManager assets, Fragment fragment,Context context) {

        this.assets = assets;
        this.mContext = context;
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
        String[] explains;


        if(targetKANA ==null)return;
        sound = KANA.checkSoundOf(targetKANA); //soundは子音のことです。かきくけこ->ka
        if(sound == null){
            throw new RuntimeException("targetKana is not kana or defined in KANA class");
        }
        array = getArraylistOf(sound);      // getArrayListOf()はこのクラスのメソッド　子音に応じたarraylistを返す。
        soundBox = array.get(KANA.checkPositionOf(targetKANA)); //子音の番号に応じたBundleを取得。「せ」であればarray.get(3)
        explain = soundBox.getString("explain"+CURRENT_STATE);
        explains = explain.split(",");

        imageUri = soundBox.getString("imageURI"+CURRENT_STATE);
        COUNT_LIMIT = Integer.valueOf(soundBox.getString("count"));


        Screen firstScreen = new Screen(CURRENT_STATE);
        firstScreen.setViewUri(imageUri);
        firstScreen.setExplains(explains);
        map = new HashMap<Integer,Screen>();
        map.put(CURRENT_STATE,firstScreen);


        if(mListener != null){
            mListener.update(firstScreen); //fragmentにUIをいじってもらいます。
        }



    }

    public void onSpeechLesson(SpeechScreen s){



    }

    public void onForwardButtonCliked(){
        CURRENT_STATE++;
        if(CURRENT_STATE <= COUNT_LIMIT){
            if(map.get(CURRENT_STATE) != null){
                mScreen = map.get(CURRENT_STATE);
            } else {
                mScreen = nextScreen(CURRENT_STATE);
            }
            if(mListener != null){
                mListener.update(mScreen);
            }
        } else if(CURRENT_STATE == COUNT_LIMIT + 1) {
             CameraScreen cScreen = getCameraScreen(CURRENT_STATE);
            if(mListener != null){
                mListener.update(cScreen);
            }
        } else if(CURRENT_STATE == COUNT_LIMIT + 2){
            SpeechScreen sScreen = getSpeechScreen(CURRENT_STATE,mContext);
            if(mListener != null){
                mListener.update(sScreen);
            }
            sScreen.doSpeech();
        } else {
            CURRENT_STATE--;
        }


    }

    public void onBackButtonClicked(){
        CURRENT_STATE--;
        if(CURRENT_STATE >0){
            mScreen = map.get(CURRENT_STATE);

            if(mListener != null){
                mListener.update(mScreen);
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
        String explain = soundBox.getString(textkey);
        String[] explains = explain.split(",");
        nextScreen.setExplains(explains);
        nextScreen.setViewUri(soundBox.getString(imagekey));
        map.put(currentstate,nextScreen);
        return nextScreen;
    }

    private CameraScreen getCameraScreen(int currentstate){ //camera用のscreenを返す
        CameraScreen camScreen = new CameraScreen(currentstate);
        String textkey = "camera";
        String explain = soundBox.getString(textkey);
        String[] explains = explain.split(",");
        camScreen.setExplains(explains);
        map.put(currentstate,camScreen);
        return camScreen;
    }

        private SpeechScreen getSpeechScreen(int currentstate,Context context){
            SpeechScreen spcScreen = new SpeechScreen(currentstate,context);
            String textkey = "speech";
            String explain = soundBox.getString(textkey);
            String[] explains = explain.split(",");
            spcScreen.setExplains(explains);
            map.put(currentstate,spcScreen);
            return spcScreen;
        }



    public interface LessonUpdater{ //LearningFragment にUIを描画してもらう。
        public void update(Screen screen);
    }
}
