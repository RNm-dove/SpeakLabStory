package com.example.ryosuke.speaklabstory;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.LeadingMarginSpan;
import android.view.View;

public class MainActivity extends AppCompatActivity implements TitleFragment.MyListener,LearningFragment.OnFragmentInteractionListener,MapFragment.OnMapFragmentListener {
    FragmentManager fManager;
    Fragment mainFragment;
    private final String STORY_FRAGMENT ="lFragment" ;
    private final String TITLE_FRAGMENT = "tFragment";
    private final String MAP_FRAGMENT = "mFragmnet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fManager = getSupportFragmentManager();
        Fragment fragment = new TitleFragment();
        fManager.beginTransaction().add(R.id.containerFragment,fragment,TITLE_FRAGMENT).commit();
    }


    //interface...タイトルフラグメントのボタンが押下されたときのアクション
    @Override
    public void onClicked(View view) {
        switch (view.getId()){
            case R.id.StoryBTN:
                fManager.beginTransaction().replace(R.id.containerFragment,MapFragment.newInstance("",""),MAP_FRAGMENT).commit(); //ここでマップ画面にうつるフラグメントをセット　引数は（フラグメントをセットするVieｗID、Fragment, 識別タグ）
                break;
            case R.id.PracticeBTN:
                break;
            case R.id.SettingBTN:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }

    //mapFragment interface
    @Override
    public void onButtonClickedInMap(View v) {
        fManager.beginTransaction().replace(R.id.containerFragment,LearningFragment.newInstance("あ",""),STORY_FRAGMENT).commit();
    }

    public ImageFactory getImageFactory(){
        return new ImageFactory(getApplicationContext(),getAssets());
    }
}
