package com.example.ryosuke.speaklabstory;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ryosuke on 17/04/10.
 */

public class SpeechListener implements RecognitionListener {
    TextView textView;
    String text;


    public SpeechListener(TextView textView){
        this.textView = textView;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        text="はなしてみよう！";
    }

    @Override
    public void onBeginningOfSpeech() {
        text="聞き取り中";
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }
    ;
    @Override
    public void onEndOfSpeech() {
        text="認識完了！！";
    }

    @Override
    public void onError(int error) {
        switch (error){
            case SpeechRecognizer.ERROR_AUDIO:
                // 音声データ保存失敗
                text="音声データを端末に一時保存できませんでした";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                // Android端末内のエラー(その他)
                text="端末側のエラーだよ";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                // 権限無し
                text="音の録音が許可されてません";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                // ネットワークエラー(その他)
                text="ネットワークエラーが発生したよ！接続を確かめてね";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                // ネットワークタイムアウトエラー
                text="ネットワークがタイムアウトになったよ…";

                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                // 音声認識結果無し
                text="認識結果がなかったよ……";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                // RecognitionServiceへ要求出せず
                text="内部エラー（Recognition Busy）です";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                // Server側からエラー通知
                text="サーバーからのエラー通知です";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                // 音声入力無し
              text="聞き取れなかったよ><";
                break;
            default:

        }


    }

    @Override
    public void onResults(Bundle results) {

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }


}
