package com.xcheng.scanner.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.xcheng.scanner.demo.scanner.XChengScanner;
import com.xcheng.scanner.demo.utils.Log;
import com.xcheng.scanner.demo.widgets.PreviewInterfaceLine;

public class PreviewScannerActivity extends Activity
        implements TextureView.SurfaceTextureListener,
        XChengScanner.IResultsListener {
    private boolean isDown;

    private TextView mResults;
    private XChengScanner mScanner;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null)
                return;

            String results = (String) msg.obj;
            mResults.setText(results);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_preview_scan);

        // request permission of camera
        if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
            return;
        }

        this.mResults = this.findViewById(R.id.scan_preview_results);
        CheckBox checkBox = this.findViewById(R.id.scan_preview_open_flush_light);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mScanner.flushLight(b);
            }
        });

        TextureView textureView = this.findViewById(R.id.scan_preview);
        textureView.setSurfaceTextureListener(this);

        PreviewInterfaceLine line = this.findViewById(R.id.scan_preview_line);
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f
        );
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        line.startAnimation(animation);
    }

    @Override
    public void result(String sym, String content) {
        Message msg = new Message();
        msg.obj = content;
        this.mHandler.sendMessage(msg);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.info("onSurfaceTextureAvailable");
        this.mScanner = new XChengScanner.Builder(new XChengScanner.CameraParams(this, false, surfaceTexture))
                .addListener(this)
                .build();
        this.mScanner.init();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        this.mScanner.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mScanner.release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                || (keyCode == KeyEvent.KEYCODE_FUNCTION)
                || (keyCode == KeyEvent.KEYCODE_F11)) {

            if (this.isDown)
                return true;

            this.mScanner.decoding();
            this.isDown = true;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                || (keyCode == KeyEvent.KEYCODE_FUNCTION)
                || (keyCode == KeyEvent.KEYCODE_F11)) {

            if (!this.isDown)
                return true;

            this.mScanner.stop();
            this.isDown = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
