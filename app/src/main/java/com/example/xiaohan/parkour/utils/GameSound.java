package com.example.xiaohan.parkour.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.example.xiaohan.parkour.R;

/**
 * 游戏音效类
 */
public class GameSound {

    private Context mContext;
    private MediaPlayer mMediaPlayer;

    private SoundPool mSoundPool;//音效池
    private int mGoldSound;//吃金币声音

    public GameSound(Context context) {
        mContext = context;
        initSound();
    }


    /**
     * 初始化音乐
     */
    private void initSound() {

        mMediaPlayer = MediaPlayer.create(mContext, R.raw.bgm_01);
        mMediaPlayer.setLooping(true);//循环播放
        mMediaPlayer.setVolume(0.2f, 0.2f);

        mSoundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 100);
        mGoldSound = mSoundPool.load(mContext, R.raw.gold, 0);
    }

    /**
     * 播放金币声音
     */
    public void playGoldSound() {
        mSoundPool.play(mGoldSound, 1, 1, 0, 0, 1);
    }

    /**
     * 播放背景音乐
     */
    public void playBGM() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    /**
     * 暂停BGM
     */
    public void pauseBGM() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    /**
     * 关闭BGM
     */
    public void stopBGM() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            Log.d("GameSound ", "stopBGM: 回收资源 ");
        }
    }

}
