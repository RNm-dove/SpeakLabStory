package com.example.ryosuke.speaklabstory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by ryosuke on 17/06/06.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Bundle> mList;
    private String mText;
    private Fragment mFragment;
    private SurfaceHolder.Callback mCallback;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle =  mList.get(position);
        LessonFragment fragment;

        if(position<mList.size()-2){
             fragment = new LessonFragment();
        } else if (position < mList.size()-1){
             fragment = new CameraFragment();
            ((CameraFragment)fragment).setmCallback(mCallback);

        } else{
             fragment = new LessonFragment();
             fragment.setEnableRecognize(true);
        }
        fragment.setArguments(bundle);

        mFragment = fragment;

        return fragment;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public Fragment getCurrentFragment(){
        return mFragment;
    }


    public void addList(ArrayList<Bundle> array){
        mList = array;
    }
    public void addText(String text){mText = text;
    }

    public void setCallback(SurfaceHolder.Callback callback){mCallback = callback;}


}
