package com.xcheng.scanner.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.TextView;

import com.xcheng.scanner.demo.scanner.XChengScanner;
import com.xcheng.scanner.demo.utils.Log;

public class ContinuousScannerActivity extends Activity implements XChengScanner.IResultsListener {
    private TextView mTimeCount;
    private TextView mAverageTime;
    private TextView mTimesPerMinute;
    private TextView mFormat;
    private TextView mResult;

    private int mCount;
    private double mTimeStart;
    private double mTimeCallback;

    private boolean isDown;
    private XChengScanner mScanner;

    private static class ScanResult {
        private final String mSym;
        private final String mContent;

        public ScanResult(String sym, String content) {
            this.mSym = sym;
            this.mContent = content;
        }

        public String getSym() {
            return this.mSym;
        }

        public String getContent() {
            return this.mContent;
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            ScanResult result = (ScanResult) msg.obj;

            // count scan times
            ++mCount;
            // average decoding time
            int average = (int) ((mTimeCallback - mTimeStart) / mCount);
            // decoding times per minute
            int perMinus = (int) (mCount / ((System.currentTimeMillis() - mTimeStart) / 1000) * 60);

            mTimeCount.setText(mCount + "");
            mAverageTime.setText(average + "");
            mTimesPerMinute.setText(perMinus + "");

            // decoding format
            if (result.getSym() != null)
                mFormat.setText(result.getSym());
            else
                mFormat.setText("");

            // decoding content
            if (result.getContent() != null)
                mResult.setText(result.getContent());
            else
                mResult.setText("");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_continous_scan);

        // request permissions of camera
        if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
            return;
        }

        this.mTimeCount = this.findViewById(R.id.show_count);
        this.mAverageTime = this.findViewById(R.id.show_average_time);
        this.mTimesPerMinute = this.findViewById(R.id.show_times_per_minute);
        this.mFormat = this.findViewById(R.id.show_format);
        this.mResult = this.findViewById(R.id.show_result);
    }

    @Override
    public void result(String sym, String content) {
        this.mTimeCallback = System.currentTimeMillis();

        Message message = new Message();
        message.obj = new ScanResult(sym, content);
        this.mHandler.sendMessage(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // must build in here, the activity lifecycle always call it
        this.mScanner = new XChengScanner.Builder(new XChengScanner.CameraParams(this))
                .isContinuous()
                .addListener(this)
                .build();
        this.mScanner.init();
        Log.info("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // must release in here, the activity lifecycle always call it
        this.mScanner.release();
        this.isDown = false;
        Log.info("onPause");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                || (keyCode == KeyEvent.KEYCODE_FUNCTION)
                || (keyCode == KeyEvent.KEYCODE_F11)) {
            this.isDown = !this.isDown;
            if (this.isDown) {
                this.mScanner.decoding();

                this.mTimeStart = System.currentTimeMillis();
                this.mCount = 0;
            } else {
                this.mScanner.stop();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
