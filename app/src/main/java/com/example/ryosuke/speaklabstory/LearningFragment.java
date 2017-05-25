package com.example.ryosuke.speaklabstory;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LearningFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LearningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearningFragment extends Fragment implements Lesson.LessonUpdater {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static boolean TEXT_LAST =false;

    private String targetKANA; //選択された仮名を引数にとる
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LearningFragment() {
        // Required empty public constructor
    }

    AssetManager assets;
    private TextView tView;
    private View mView;
    private String[] text;
    private String uri;
    private ImageFactory iFactory;
    private RelativeLayout rLayout;
    private Lesson mLesson;

    private final int CAMERA_ID = 1; //frontCamera


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param targetKANA Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LearningFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LearningFragment newInstance(String targetKANA, String param2) {
        LearningFragment fragment = new LearningFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, targetKANA);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            targetKANA = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mLesson = new Lesson(assets,this,getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_learning, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        assets = context.getAssets();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onStart() {
        super.onStart();
        iFactory = new ImageFactory(getContext(),assets);
        mLesson.startLearning(targetKANA); // Lessonclass がこのアプリのメインになる
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button bButton = (Button)view.findViewById(R.id.backButton);
        Button fButton = (Button)view.findViewById(R.id.forwardButton);
        bButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mLesson.onBackButtonClicked();
            }
        });
        fButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mLesson.onForwardButtonCliked();
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //Lesson class で呼ばれる。このタイミングで描画を入れ替える。

    @Override
    public void update(Screen s) {
        View v = getView();
        TEXT_LAST = false;
        rLayout = (RelativeLayout)v.findViewById(R.id.learningFragmentView);
        rLayout.removeAllViews();
        tView = (TextView) v.findViewById(R.id.contentText);
        if(s instanceof CameraScreen) {
            text = s.getExplains();

            SurfaceView sView = new SurfaceView(getContext());
            SurfaceHolder mSurfaceHolder = sView.getHolder();
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mSurfaceHolder.addCallback(new CameraHolder(getActivity()));
            rLayout.addView(sView);

        } else if (s instanceof SpeechScreen) {
            text = s.getExplains();

        } else {
            text = s.getExplains();
            uri = s.getUri();
            if(uri != null){
                mView = iFactory.newImageView(uri);
            }
            rLayout.addView(mView);

        }
        tView.setText(text[0]);



        tView.setOnClickListener(new View.OnClickListener(){
            int i=1;

            @Override
            public void onClick(View v){
                if(i < text.length){
                    tView.setText(text[i]);
                    i++;
                } if(i==text.length){
                    TEXT_LAST = true;
                }

            }
        });






    }
}
