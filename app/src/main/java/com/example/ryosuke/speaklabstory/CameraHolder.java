package com.example.ryosuke.speaklabstory;

import android.app.Activity;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * Created by ryosuke on 17/04/09.
 */

public class CameraHolder implements SurfaceHolder.Callback{
    Camera mCamera = null;
    Activity mActivity;


    //Constructer
    public CameraHolder(Activity activity){
        mActivity = activity;
    }

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
        setCameraDisplayOrientation(id,mActivity);
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


    private void setCameraDisplayOrientation(int cameraId, Activity activity){
        if(cameraId <0){
            return;
        }
        //get camera infomation
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,cameraInfo);
        //get display orientation
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
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
}
