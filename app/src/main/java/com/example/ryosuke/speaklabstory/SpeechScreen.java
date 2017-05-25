package com.example.ryosuke.speaklabstory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;

/**
 * Created by ryosuke on 17/05/25.
 */

public class SpeechScreen extends Screen implements RecognitionListener{
    Boolean isEnable = false;
    Context context;
    Boolean isReady = false;
    String text;
    SpeechRecognizer mSpeechRecognizer;
    Intent mIntent = null;

    public SpeechScreen(int state, Context context) {
        super(state);
        this.context = context;

    }

    public boolean isEnable(){
        return isEnable;

    }

    public void setEnable(){
        isEnable = true;
    }

    public void setDisable(){
        isEnable = false;
    }

    public boolean doSpeech(){
        if(isEnable ==true){

            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            mSpeechRecognizer.setRecognitionListener(this);
            mIntent = new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
            mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            mIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
            mSpeechRecognizer.startListening(mIntent);

            return true;
        }

        return false;

    }

    public void stopSpeech(){
        mSpeechRecognizer.stopListening();

    }

    public void reStartSpeech(){
        if(mSpeechRecognizer != null && mIntent != null){
            mSpeechRecognizer.startListening(mIntent);
        }
    }




    @Override
    public void onReadyForSpeech(Bundle params) {
        isReady = true;
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

    }

    @Override
    public void onError(int error) {
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                // 音声データ保存失敗
                text = "音声データを端末に一時保存できませんでした";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                // Android端末内のエラー(その他)
                text = "端末側のエラーだよ";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                // 権限無し
                text = "音の録音が許可されてません";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                // ネットワークエラー(その他)
                text = "ネットワークエラーが発生したよ！接続を確かめてね";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                // ネットワークタイムアウトエラー
                text = "ネットワークがタイムアウトになったよ…";

                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                // 音声認識結果無し
                text = "認識結果がなかったよ……";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                // RecognitionServiceへ要求出せず
                text = "内部エラー（Recognition Busy）です";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                // Server側からエラー通知
                text = "サーバーからのエラー通知です";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                // 音声入力無し
                text = "聞き取れなかったよ><";
                break;
            default:
        }


    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> datalist = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        text = datalist.get(0);

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}
