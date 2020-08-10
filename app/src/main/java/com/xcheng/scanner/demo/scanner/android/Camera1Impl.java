package com.xcheng.scanner.demo.scanner.android;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import com.xcheng.scanner.demo.scanner.ICameraAction;
import com.xcheng.scanner.demo.scanner.common.UtilsCamera;
import com.xcheng.scanner.demo.utils.Log;

public class Camera1Impl implements ICameraAction {
    private final Context mContext;

    private final int mCameraID;
    private final int mCameraWidth;
    private final int mCameraHeight;
    private final boolean mCameraDefaultOpenFlushLight;
    private final SurfaceTexture mCameraSurfaceTexture;

    private Camera mCamera;

    public Camera1Impl(Context context, int id, int width, int height, boolean isOpenFlushLight, SurfaceTexture view) {
        this.mContext = context;
        this.mCameraID = id;
        this.mCameraWidth = width;
        this.mCameraHeight = height;
        this.mCameraDefaultOpenFlushLight = isOpenFlushLight;
        this.mCameraSurfaceTexture = view;
    }

    @Override
    public void create() {
        this.mCamera = Camera.open(this.mCameraID);
        List<Camera.Size> supportedPreviewSizes = this.mCamera.getParameters().getSupportedPreviewSizes();
        Camera.Size previewSize = UtilsCamera.getCameraOneOptimalPreviewSize(
                supportedPreviewSizes,
                this.mCameraWidth,
                this.mCameraHeight
        );

        Camera.Parameters parameters = this.mCamera.getParameters();
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        if (this.mCameraDefaultOpenFlushLight)
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        int fps = UtilsCamera.getCameraOneDisplayFps(parameters);
        parameters.setPreviewFpsRange(fps, fps);
        this.mCamera.setParameters(parameters);
        this.mCamera.setDisplayOrientation(90);

        try {
            this.mCamera.setPreviewTexture(this.mCameraSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mCamera.startPreview();
    }

    @Override
    public void release() {
        if (this.mCamera == null)
            return;
        try {
            this.mCamera.stopPreview();
            this.mCamera.setPreviewDisplay(null);
            this.mCamera.setPreviewCallback(null);
            this.mCamera.release();
            this.mCameraSurfaceTexture.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void flushLight(boolean v) {
        if (this.mCamera == null)
            return;

        Camera.Parameters parameters = this.mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        if (v)
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        else
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        this.mCamera.setParameters(parameters);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
