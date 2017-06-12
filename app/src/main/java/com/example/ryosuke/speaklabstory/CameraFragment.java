package com.example.ryosuke.speaklabstory;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ryosuke on 17/06/10.
 */

public class CameraFragment extends LessonFragment {
    private SurfaceHolder.Callback mCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        ArrayList<String> list = bundle.getStringArrayList("texts");

        // View をつくる
        View layout = inflater.inflate(R.layout.fragment_camera, container, false);
        SurfaceView surfaceView = (SurfaceView)layout.findViewById(R.id.camera_view);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(mCallback);

        final LessonFragment fragment = this;

        mTextView = (TextView)layout.findViewById(R.id.camera_text);
        if(list != null){
            mTextView.setText(list.get(0));
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonPressed(v,fragment);
                }
            });
        }



        return layout;

    }

    public void setmCallback(SurfaceHolder.Callback callback){mCallback = callback;}




}
