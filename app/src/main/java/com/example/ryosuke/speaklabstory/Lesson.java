package com.example.ryosuke.speaklabstory;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ryosuke on 17/05/18.
 */

public class Lesson {
    private  MyContent mContent;
    private final String JSON_FILE = "data.json"; //jsonfileにuriなどをおいた
    private AssetManager assets;
    private LessonUpdater mListener;

    public Lesson(AssetManager assets) {

        this.assets = assets;
        JsonFactory jF = new JsonFactory();
        JSONObject json = jF.getJsonObject(assets,JSON_FILE);
        mContent = new MyContent(json);
    }

    public void startLearning(String targetKANA){
        ImageFactory iFactory;
        String sound;
        ArrayList<Bundle> array;
        Bundle bundle;
        String word;
        String imageUri1;
        String imageUri2;


        if(targetKANA ==null)return;
        sound = KANA.checkSoundOf(targetKANA);
        if(sound == null){
            throw new RuntimeException("targetKana is not kana or defined in KANA class");
        }
        array = getArraylistOf(sound);
        bundle = array.get(KANA.checkPositionOf(targetKANA));

        word = bundle.getString("word");
        imageUri1 = bundle.getString("imageURI");


        if(mListener != null){
            mListener.update();
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

    public interface LessonUpdater{
        public void update();
    }
}
