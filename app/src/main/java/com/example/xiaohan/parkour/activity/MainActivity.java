package com.example.xiaohan.parkour.activity;

import android.os.Bundle;

import com.example.xiaohan.parkour.custom_view.GameSurfaceView;
import com.example.xiaohan.parkour.utils.GameSound;

public class MainActivity extends BaseActivity {

    private GameSurfaceView mGameSurfaceView;
    private GameSound mGameSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGameSound = new GameSound(MainActivity.this);
        mGameSurfaceView = new GameSurfaceView(MainActivity.this, mGameSound);
        setContentView(mGameSurfaceView);//设置下视图
    }

    /**
     * 重新准备就绪时
     */
    @Override
    protected void onResume() {
        super.onResume();
        mGameSound.playBGM();
    }

    /**
     * 暂停时
     */
    @Override
    protected void onPause() {
        super.onPause();
        mGameSound.pauseBGM();
    }

    @Override
    protected void onDestroy() {
        mGameSound.stopBGM();
        super.onDestroy();
    }
}
