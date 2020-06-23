package com.xcheng.scanner.demo.scanner.android;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.xcheng.scanner.demo.R;
import com.xcheng.scanner.demo.scanner.IAudioBeepAction;

public class AudioBeepImpl implements IAudioBeepAction, SoundPool.OnLoadCompleteListener {
    private static final float BEEP_VOLUME = 0.10f;

    private final int mSoundID;
    private final SoundPool mSoundPool;

    public AudioBeepImpl(Context v) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        this.mSoundPool = new SoundPool.Builder()
                .setMaxStreams(128)
                .setAudioAttributes(attributes)
                .build();

        this.mSoundPool.setOnLoadCompleteListener(this);
        this.mSoundID = this.mSoundPool.load(v, R.raw.barcode_success, 1);
    }

    @Override
    public void play() {
        this.mSoundPool.play(this.mSoundID, BEEP_VOLUME, BEEP_VOLUME, 1, 0, 1);
    }

    @Override
    public void release() {
        this.mSoundPool.autoPause();
        this.mSoundPool.unload(this.mSoundID);
        this.mSoundPool.release();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {

    }
}
