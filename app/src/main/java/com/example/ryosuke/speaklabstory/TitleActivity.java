package com.example.ryosuke.speaklabstory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class TitleActivity extends AppCompatActivity {
    private final int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        permissionCheck();
    }

    private void getContentsInfo(){
        Button button = (Button)findViewById(R.id.titleButton);
        final Intent intent = new Intent(this,LessonActivity.class);
        intent.putExtra(LessonActivity.TARGET_KANA,"あ");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

    }


    private void permissionCheck(){ //if more than android6.0 ,request permission.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.RECORD_AUDIO ) == PackageManager.PERMISSION_GRANTED &&checkSelfPermission(Manifest.permission.VIBRATE ) == PackageManager.PERMISSION_GRANTED ){
                getContentsInfo();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.VIBRATE}, PERMISSIONS_REQUEST_CODE);

                getContentsInfo();
            }

        } else{
            getContentsInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_CODE){
            getContentsInfo();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
