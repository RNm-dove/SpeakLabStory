package com.example.ryosuke.speaklabstory;

import android.os.Bundle;
import android.view.View;

/**
 * Created by ryosuke on 17/05/19.
 */

public class Screen  {

    String explain;
    String uri;
    int CURRENT_STATE;

    public Screen(int state) {
        this.CURRENT_STATE = state;
    }


    public void setExplain(String s) {
        this.explain = s;
    }


    public void setViewUri(String s) {
        this.uri = s;
    }

    public String getExplain(){return this.explain;}
    public String getUri(){return this.uri;}


    public boolean hasView() {
        if(this.uri != null){
            return false;
        }
        return true;
    }

    public int getCURENT_STATE(){
        return CURRENT_STATE;
    }


    public Bundle saveCurrentState() {
        Bundle savedInstance = new Bundle();
        savedInstance.putString("explain",explain);
        savedInstance.putString("uri",uri);
        return savedInstance;
    }
}
