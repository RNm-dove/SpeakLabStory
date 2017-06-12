package com.example.ryosuke.speaklabstory;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LessonFragment extends Fragment{
    private int color;
    private OnFragmentInteractionListener mListener;
    protected TextView mTextView;

    private boolean RECOGNIZABLE = false;


    public LessonFragment() {
        // Required empty public constructor
    }

    public void setText(String text){
        mTextView.setText(text);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        ArrayList<String> list = bundle.getStringArrayList("texts");
        String image = bundle.getString("image","line_a/0.jpg");


        // View をつくる
        View layout = inflater.inflate(R.layout.fragment_lesson, container, false);

        ImageView imageView = (ImageView)layout.findViewById(R.id.main_image);
        ImageFactory imageFactory = new ImageFactory(getContext());
        Bitmap bitmap = imageFactory.getBitmap(image);
        imageView.setImageBitmap(bitmap);

        final LessonFragment fragment = this;


        mTextView = (TextView)layout.findViewById(R.id.main_text);
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

    public boolean isRecognizable(){
        return RECOGNIZABLE;
    }

    public void setEnableRecognize(Boolean tf){
        RECOGNIZABLE = tf;
    }


    public void onButtonPressed(View view,LessonFragment fragment) {
        if (mListener != null) {
            mListener.onTextClick(view,fragment);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LessonFragment.OnFragmentInteractionListener) {
            mListener = (LessonFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * アクティビティで実装
     */
    public interface OnFragmentInteractionListener {
        void onTextClick(View view, LessonFragment fragment);
    }


}
