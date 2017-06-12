package com.example.ryosuke.speaklabstory;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
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
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LessonActivity extends AppCompatActivity implements RecognitionListener, LessonFragment.OnFragmentInteractionListener {

    private FragmentAdapter mAdapter;
    private ViewPager mViewPager;
    private ArrayList<String > mList;
    private Iterator<String> mIterator;
    private Vibrator mVibrator;
    private String mSpeakText;
    private long[] pattern = {300,300,300,300}; //300ミリ秒ごとに振動を２回繰り返す。

    private Camera mCamera;
    private float mDist = 0;

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
        mAdapter.setCallback(callback);
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
        mViewPager.setOnTouchListener();




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Get the pointer ID
        Camera.Parameters params = mCamera.getParameters();
        int action = event.getAction();


        if (event.getPointerCount() > 1) {
            // handle multi-touch events
            if (action == MotionEvent.ACTION_POINTER_DOWN) {
                mDist = getFingerSpacing(event);
            } else if (action == MotionEvent.ACTION_MOVE && params.isZoomSupported()) {
                mCamera.cancelAutoFocus();
                handleZoom(event, params);
            }
        } else {
            // handle single touch events
            if (action == MotionEvent.ACTION_UP) {
                handleFocus(event, params);
            }
        }
        return true;
    }

    private void handleZoom(MotionEvent event, Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            //zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            //zoom out
            if (zoom > 0)
                zoom--;
        }
        mDist = newDist;
        params.setZoom(zoom);
        mCamera.setParameters(params);
    }

    public void handleFocus(MotionEvent event, Camera.Parameters params) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                    // currently set to auto-focus on single touch
                }
            });
        }
    }

    /** Determine the space between the first two fingers */
    private float getFingerSpacing(MotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
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

    /**
     *
      */
    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            int frontCameraId = -1;
            int backCameraId = -1;
            int numberOfCameras = Camera.getNumberOfCameras();
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            for (int i = 0; i < numberOfCameras; i++) {
                // 指定したカメラの情報を　　　　　
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    backCameraId = i;
                }
                else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    frontCameraId = i;
                }
            }
            int id = -1;
            if (frontCameraId != -1) { // フロントカメラを指定
                id = frontCameraId;
            }
            // if (backCameraId != -1) { / バックカメラを指定
            // id = backCameraId;
            //}
            if (id >= 0) {
                mCamera = Camera.open(id);
            }
            else {
                mCamera = Camera.open();
            }
            try {
                //set display orientation.
                setCameraDisplayOrientation(id);
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mCamera.stopPreview();
            //set the size of preview display
            Camera.Parameters params = mCamera.getParameters();
            params.setPreviewSize(width,height);
            List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
            Camera.Size size = previewSizes.get(0); //maximum
            params.setPreviewSize(size.width,size.height);
            mCamera.setParameters(params);

            // restart preview
            mCamera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.startPreview();
            mCamera.release();
            mCamera = null;

        }


        private void setCameraDisplayOrientation(int cameraId){
            if(cameraId <0){
                return;
            }
            //get camera infomation
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId,cameraInfo);
            //get display orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int degrees = 0;
            switch (rotation){
                case Surface.ROTATION_0:degrees = 0; break;
                case Surface.ROTATION_90:degrees=90; break;
                case Surface.ROTATION_180:degrees=180;break;
                case Surface.ROTATION_270:degrees=270;break;
            }

            int result;
            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (cameraInfo.orientation + degrees) % 360;
                result = (360 - result) % 360; //compensate the mirror
            }else { // back-facing
                result = (cameraInfo.orientation - degrees + 360) %360;
            }

            mCamera.setDisplayOrientation(result);

        }
    };


}
