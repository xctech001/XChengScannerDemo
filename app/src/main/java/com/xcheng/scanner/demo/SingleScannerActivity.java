package com.xcheng.scanner.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xcheng.scanner.sdk.ICameraAction;
import com.xcheng.scanner.sdk.XChengScanner;
import com.xcheng.scanner.demo.utils.Log;

public class SingleScannerActivity extends Activity implements XChengScanner.IResultsListener {
    private boolean isDown;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private XChengScanner mXChengScanner;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String content = (String) msg.obj;

            if (content == null)
                return;

            // to add scan results and refresh interface in listview
            mAdapter.add(content);
            mListView.deferNotifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_single_scan_list);

        // request permissions of camera
        if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
            return;
        }

        this.mListView = this.findViewById(R.id.scanner_results);
        this.mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        this.mListView.setAdapter(this.mAdapter);
    }

    @Override
    public void result(String sym, String content) {
        // will to be send scan results in listview
        Message msg = new Message();
        msg.obj = content;
        this.mHandler.sendMessage(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // must build in here, the activity lifecycle always call it
        this.mXChengScanner = new XChengScanner.Builder(new XChengScanner.CameraParams(this))
                .setCamera(new ICameraAction() {
                    @Override
                    public void create() {

                    }

                    @Override
                    public void release() {

                    }

                    @Override
                    public void flushLight(boolean b) {

                    }

                    @Override
                    public void start() {

                    }

                    @Override
                    public void stop() {

                    }
                })
                .addListener(this)
                .build();
        this.mXChengScanner.init();
        Log.info("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // must release in here, the activity lifecycle always call it
        this.mXChengScanner.release();
        Log.info("onPause");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                || (keyCode == KeyEvent.KEYCODE_FUNCTION)
                || (keyCode == KeyEvent.KEYCODE_F11)) {

            if (this.isDown)
                return true;

            this.mXChengScanner.decoding();
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

            this.mXChengScanner.stop();
            this.isDown = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
