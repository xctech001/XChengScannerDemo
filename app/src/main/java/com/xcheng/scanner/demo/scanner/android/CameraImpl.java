package com.xcheng.scanner.demo.scanner.android;

import java.util.Collections;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.*;
import android.view.Surface;

import com.xcheng.scanner.demo.scanner.ICameraAction;

public class CameraImpl extends CameraDevice.StateCallback implements ICameraAction {
    private final int mCameraID;
    private final boolean isOpenFlushLight;
    private final Context mContext;
    private final CameraManager mManager;

    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;

    private final SurfaceTexture mSurfaceTexture;
    private final Surface mSurface;

    private final CameraCaptureSession.StateCallback mCameraState = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            mCameraCaptureSession = session;

            try {
                CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                builder.addTarget(mSurface);

                if (isOpenFlushLight) {
                    builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                    builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
                }

                session.setRepeatingRequest(builder.build(), null, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

        }
    };

    public CameraImpl(Context context, int id, int width, int height, boolean isOpenFlushLight, SurfaceTexture view) {
        this.mCameraID = id;
        this.isOpenFlushLight = isOpenFlushLight;
        this.mContext = context;
        this.mManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        if (view == null) {
            this.mSurfaceTexture = new SurfaceTexture(0);
            this.mSurface = new Surface(this.mSurfaceTexture);
            this.mSurfaceTexture.setDefaultBufferSize(width, height);
        } else {
            this.mSurfaceTexture = view;
            this.mSurface = new Surface(this.mSurfaceTexture);
            this.mSurfaceTexture.setDefaultBufferSize(width, height);
        }
    }

    // Camera state listener onOpened
    @Override
    public void onOpened(CameraDevice camera) {
        this.mCameraDevice = camera;

        try {
            camera.createCaptureSession(Collections.singletonList(this.mSurface), this.mCameraState, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Camera state listener onDisconnected
    @Override
    public void onDisconnected(CameraDevice cameraDevice) {

    }

    // Camera state listener onError
    @Override
    public void onError(CameraDevice cameraDevice, int i) {

    }

    @Override
    public void create() {
        if (this.mContext.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            this.mManager.openCamera(String.valueOf(this.mCameraID), this, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        if (this.mCameraCaptureSession != null)
            this.mCameraCaptureSession.close();
        if (this.mCameraDevice != null)
            this.mCameraDevice.close();
        if (this.mSurfaceTexture != null)
            this.mSurfaceTexture.release();
        this.mSurface.release();
    }

    @Override
    public void flushLight(boolean v) {
        try {
            CaptureRequest.Builder builder = this.mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            builder.addTarget(this.mSurface);
            builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);

            if (v)
                builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
            else
                builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF);

            this.mCameraCaptureSession.setRepeatingRequest(builder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            CaptureRequest.Builder builder = this.mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            builder.addTarget(this.mSurface);
            this.mCameraCaptureSession.setRepeatingRequest(builder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            if (this.mCameraCaptureSession != null)
                this.mCameraCaptureSession.stopRepeating();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
