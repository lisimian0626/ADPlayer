package com.example.administrator.myapplication;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private Camera mCamera;
    private int mPreviewRotation = 0;
    private int mCamId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private PreviewCallback mPrevCb;
    private byte[] mYuvPreviewFrame;
    private int previewWidth;
    private int previewHeight;

    public interface PreviewCallback {
        void onGetYuvFrame(byte[] data);
    }

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPreviewRotation(int rotation) {
        mPreviewRotation = rotation;
    }

    public void setCameraId(int id) {
        mCamId = id;
    }

    public int getCameraId() {
        return mCamId;
    }

    public void setPreviewCallback(PreviewCallback cb) {
        mPrevCb = cb;
        getHolder().addCallback(this);
    }

    public void setPreviewResolution(int width, int height) {
        previewWidth = width;
        previewHeight = height;
    }

    public boolean startCamera() {
        if (mCamera != null) {
            return false;
        }
        if (mCamId > (Camera.getNumberOfCameras() - 1) || mCamId < 0) {
            return false;
        }
        stopCamera();
        try {
            mCamera = Camera.open(mCamId);
            mYuvPreviewFrame = new byte[previewWidth * previewHeight * 3 / 2];
            Camera.Parameters parameters = mCamera.getParameters();//获取各项参数
            Camera.Size previewSize;
            previewSize = findFitPreResolution(parameters);
            if(previewSize!=null){
                parameters.setPreviewSize(previewSize.width, previewSize.height);// 设置预览大小
                mCamera.setParameters(parameters);
            }
            if(mPreviewRotation!=0){
                mCamera.setDisplayOrientation(mPreviewRotation);
            }
            mCamera.getParameters().setPreviewSize(previewWidth,previewHeight);
            mCamera.addCallbackBuffer(mYuvPreviewFrame);
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.setPreviewDisplay(getHolder());
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        mPrevCb.onGetYuvFrame(data);
        camera.addCallbackBuffer(mYuvPreviewFrame);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
    }
    private Camera.Size findFitPreResolution(Camera.Parameters cameraParameters) throws Exception {
        List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPreviewSizes();

        Camera.Size resultSize = null;
        for (Camera.Size size : supportedPicResolutions) {
                if (resultSize == null) {
                    resultSize = size;
                } else if (size.width > resultSize.width) {
                    resultSize = size;
            }
        }
        if (resultSize == null) {
            return supportedPicResolutions.get(0);
        }
        return resultSize;
    }

}
