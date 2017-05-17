package com.example.ryosuke.speaklabstory;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LearningFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LearningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearningFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String targetKANA; //選択された仮名を引数にとる
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private final String JSON_FILE = "data.json"; //jsonfileにuriなどをおいた
    private MyContent mContent;

    private View containerView; //layoutViewを取得

    public LearningFragment() {
        // Required empty public constructor
    }

    AssetManager assets;

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

        JSONObject json = getJsonFile();
        mContent = new MyContent(json);


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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view;

    }

    @Override
    public void onStart() {
        super.onStart();
        startLearning(targetKANA);
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

    private JSONObject getJsonFile(){
        InputStream in;
        JSONObject json;
        String jsonString;
        in = null;
        json = null;
        jsonString = null;
        AssetManager assets = getActivity().getAssets();

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

    /*
      ここからがメインの内容となります
     */
    private void startLearning(String targetKANA){
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

        StringBuffer sb = new StringBuffer();
        sb.append(word).append("のURIは").append(imageUri1).append("です。");

        iFactory = new ImageFactory(getContext(),assets);
        ImageView imageView = iFactory.newImageView(imageUri1); //渡したuriをassetから取得、imageViewにする

        TextView contentText = (TextView)containerView.findViewById(R.id.contentText);
        contentText.setText(sb.toString());

        ViewGroup views = (ViewGroup)containerView.findViewById(R.id.learningFragmentView);
        views.addView(imageView);





        contentText.setText(sb.toString());


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
}
