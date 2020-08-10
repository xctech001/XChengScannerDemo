package com.xcheng.scanner.demo.scanner;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.xcheng.scanner.demo.scanner.android.AudioBeepImpl;
import com.xcheng.scanner.demo.scanner.android.Camera1Impl;
import com.xcheng.scannere3.XCScanner;

public class XChengScanner implements XCScanner.Result {
    public interface IResultsListener {
        void result(String sym, String content);
    }

    public static class CameraParams {
        private static final int DEF_CAMERA_ID = 0;
        private static final int DEF_WIDTH = 844;
        private static final int DEF_HEIGHT = 640;

        private final int mCameraID;
        private final int mWidth;
        private final int mHeight;
        private final boolean isOpenFlushLight;

        private final Context mContext;
        private final SurfaceTexture mSurfaceView;

        public CameraParams(Context v) {
            this(v, DEF_CAMERA_ID, DEF_WIDTH, DEF_HEIGHT, false, null);
        }

        public CameraParams(Context context, boolean openFlushLight, SurfaceTexture view) {
            this(context, DEF_CAMERA_ID, DEF_WIDTH, DEF_HEIGHT, openFlushLight, view);
        }

        public CameraParams(
                Context context,
                int id,
                int width,
                int height,
                boolean openFlushLight,
                SurfaceTexture view
        ) {
            this.mContext = context;
            this.mCameraID = id;
            this.mWidth = width;
            this.mHeight = height;
            this.isOpenFlushLight = openFlushLight;
            this.mSurfaceView = view;
        }
    }

    public static class Builder {
        private final List<IResultsListener> mResults = new ArrayList<>();

        private boolean isContinuous;
        private ICameraAction mCameraAction;
        private IAudioBeepAction mAudioBeepAction;

        public Builder(CameraParams params) {
            this.mCameraAction = new Camera1Impl(
                    params.mContext,
                    params.mCameraID,
                    params.mWidth,
                    params.mHeight,
                    params.isOpenFlushLight,
                    params.mSurfaceView
            );

            this.mAudioBeepAction = new AudioBeepImpl(params.mContext);
        }

        public Builder isContinuous() {
            this.isContinuous = true;
            return this;
        }

        public Builder addListener(IResultsListener v) {
            this.mResults.add(v);
            return this;
        }

        public Builder setCamera(ICameraAction v) {
            this.mCameraAction = v;
            return this;
        }

        public Builder setAudioBeep(IAudioBeepAction v) {
            this.mAudioBeepAction = v;
            return this;
        }

        public XChengScanner build() {
            return new XChengScanner(this);
        }
    }

    private static final int CMD_CAMERA_CREATE = 1;
    private static final int CMD_CAMERA_RELEASE = 2;
    private static final int CMD_CAMERA_FLUSH_LIGHT = 3;

    private static final int PARAMS_DEFAULT_TIMEOUTS = 15000;

    private static final HandlerThread HTBeep = new HandlerThread("PlayBeep");
    private static final HandlerThread HTCamera = new HandlerThread("RunCamera");

    private boolean isContinuous;
    private XCScanner mScanner;

    private final Builder mBuilder;
    private final Handler mHandleBeep;
    private final Handler mHandleCamera;

    @SuppressLint("HandlerLeak")
    public XChengScanner(Builder v) {
        this.mBuilder = v;
        if (HTBeep.getState().equals(Thread.State.NEW))
            HTBeep.start();
        if (HTCamera.getState().equals(Thread.State.NEW))
            HTCamera.start();

        this.mHandleBeep = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mBuilder.mAudioBeepAction.play();
            }
        };

        this.mHandleCamera = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CMD_CAMERA_CREATE:
                        mBuilder.mCameraAction.create();
                        break;
                    case CMD_CAMERA_RELEASE:
                        mBuilder.mCameraAction.release();
                        break;
                    case CMD_CAMERA_FLUSH_LIGHT:
                        mBuilder.mCameraAction.flushLight((Boolean) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void scanBeep() {
        this.mHandleBeep.sendEmptyMessage(0);
    }

    @Override
    public void scanStart() {
        if ((this.isContinuous) && (this.mScanner != null))
            this.mScanner.startDecode();
    }

    @Override
    public void scanResult(String sym, String content) {
        for (IResultsListener listener : this.mBuilder.mResults)
            listener.result(sym, content);
    }

    public void init() {
        this.init(PARAMS_DEFAULT_TIMEOUTS);
    }

    public void init(int ms) {
        this.mHandleCamera.sendEmptyMessage(CMD_CAMERA_CREATE);
        this.mScanner = XCScanner.newInstance();
        this.mScanner.onScanListener(this);
        this.mScanner.setRoundTimeout(ms);
    }

    public void release() {
        this.mHandleCamera.sendEmptyMessage(CMD_CAMERA_RELEASE);
        this.mBuilder.mAudioBeepAction.release();
        this.mScanner.deleteInstance();
    }

    public void flushLight(boolean v) {
        Message message = new Message();
        message.what = CMD_CAMERA_FLUSH_LIGHT;
        message.obj = v;
        this.mHandleCamera.sendMessage(message);
    }

    public void decoding() {
        this.mScanner.startDecode();
        if (this.mBuilder.isContinuous)
            this.isContinuous = true;
    }

    public void stop() {
        this.mScanner.stopDecode();
        if (this.mBuilder.isContinuous)
            this.isContinuous = false;
    }
}
