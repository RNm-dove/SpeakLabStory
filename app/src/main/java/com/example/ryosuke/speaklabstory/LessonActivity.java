package com.example.ryosuke.speaklabstory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class LessonActivity extends AppCompatActivity implements RecognitionListener, LessonFragment.OnFragmentInteractionListener {

    private FragmentAdapter mAdapter;
    private ViewPager mViewPager;
    private ArrayList<String > mList;
    private Iterator<String> mIterator;
    private Vibrator mVibrator;
    private String mSpeakText;
    private long[] pattern = {300,300,300,300}; //300ミリ秒ごとに振動を２回繰り返す。

    public static final String TARGET_KANA = "targetKANA";
    private final String JSON_FILE  = "data.json";


    /**
     * jsonファイルを読みだしてcontentクラスにセット。前のアクティビティから学習する単語を受け取り、それをfragmentAdapterに管理してもらう。
     * テキストはクリックするたびにarraylistの次に行くようにイテレーターを設定する。最後のテキストにきたらページを変える。
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JsonFactory jsonFactory = new JsonFactory();
        JSONObject json = jsonFactory.getJsonObject(getAssets(),JSON_FILE);
        Content content = new JsonContent(json);
        String targetKANA = getIntent().getStringExtra(TARGET_KANA);
        final ArrayList<Bundle> list = content.getListOf(targetKANA);

        ArrayList<String> array = list.get(0).getStringArrayList("texts");

        mIterator = array.iterator();
        if(mIterator.hasNext())mIterator.next(); //すでにテキスト[0]は表示してあるのでイテレーターをテキスト[1]からにしておく



        mViewPager = (ViewPager)findViewById(R.id.container);
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mAdapter.addList(list);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(list.get(position) != null){
                    mIterator = list.get(position).getStringArrayList("texts").iterator();
                    if(mIterator.hasNext())mIterator.next(); //すでにテキスト[0]は表示してあるのでイテレーターをテキスト[1]からにしておく
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);


    }

    /**
     * テキストがクリックされるたびに更新されます。
     * テキストが最後尾にきたとき音声認識可能であれば音声認識を行います
     * @param view フラグメントでクリックされたview
     * @param fragment 現在表示されているfragment
     */
    @Override
    public void onTextClick(View view,LessonFragment fragment) {

        switch (view.getId()){
            case R.id.main_text:
                if(mIterator.hasNext()) {
                    String s = mIterator.next();
                    ((TextView) view).setText(s);
                }else if(fragment.isRecognizable()){
                    SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
                    speechRecognizer.setRecognitionListener(this);
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                            getPackageName());
                    speechRecognizer.startListening(intent);
                    fragment.setEnableRecognize(false);
                } else {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                }
        }
    }


    /////////////RecognitonListener//////////////

    @Override
    public void onReadyForSpeech(Bundle params) {
        mVibrator.vibrate(300);
        mSpeakText = "じゅんびかんりょう　はなしてみて";
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        mVibrator.vibrate(pattern,-1);

    }

    @Override
    public void onError(int error) {
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                // 音声データ保存失敗
                mSpeakText = "音声データを端末に一時保存できませんでした";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                // Android端末内のエラー(その他)
                mSpeakText = "端末側のエラーだよ";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                // 権限無し
                mSpeakText = "音の録音が許可されてません";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                // ネットワークエラー(その他)
                mSpeakText = "ネットワークエラーが発生したよ！接続を確かめてね";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                // ネットワークタイムアウトエラー
                mSpeakText = "ネットワークがタイムアウトになったよ…";

                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                // 音声認識結果無し
                mSpeakText = "認識結果がなかったよ……";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                // RecognitionServiceへ要求出せず
                mSpeakText = "内部エラー（Recognition Busy）です";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                // Server側からエラー通知
                mSpeakText = "サーバーからのエラー通知です";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                // 音声入力無し
                mSpeakText = "聞き取れなかったよ><";
                break;
            default:
        }

        LessonFragment fragment = (LessonFragment)mAdapter.getCurrentFragment();
        fragment.setText(mSpeakText);

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> datalist = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String result = datalist.get(0);

        boolean right = false;
        StringBuilder sBuilder = new StringBuilder();

        switch (result) {
            case "青":
            case "あお":
                sBuilder.append("おお！　すごい。　ちゃんと　きこえたよ！");
                break;


            default:
                sBuilder.append("[").append(result).append("]と　きこえたよ,もういっかい　やってみよう");
                break;
        }
        mSpeakText = sBuilder.toString();
        LessonFragment fragment = (LessonFragment)mAdapter.getCurrentFragment();
        fragment.setText(mSpeakText);


    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }


}
