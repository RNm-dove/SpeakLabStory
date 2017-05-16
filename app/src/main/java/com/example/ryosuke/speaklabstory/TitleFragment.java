package com.example.ryosuke.speaklabstory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TitleFragment.MyListener} interface
 * to handle interaction events.
 */
public class TitleFragment extends Fragment implements View.OnClickListener {

    private MyListener mListener;

    public TitleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //各ボタンにリスナーを設置
        view.findViewById(R.id.StoryBTN).setOnClickListener(this);
        view.findViewById(R.id.PracticeBTN).setOnClickListener(this);
        view.findViewById(R.id.SettingBTN).setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_title, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyListener) {
            mListener = (MyListener) context;
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

    public interface MyListener {
        void onClicked(View view);
    }

    //クリックリスナー mainActivityで定義
    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onClicked(v);
        }
    }
}
