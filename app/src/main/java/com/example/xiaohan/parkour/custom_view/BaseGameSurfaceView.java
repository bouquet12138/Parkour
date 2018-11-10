package com.example.xiaohan.parkour.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.example.xiaohan.parkour.utils.GameSound;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseGameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "BaseGameSurfaceView";

    private boolean mFirstCreate = true;//第一次创建

    protected GameSound mGameSound;
    protected SurfaceHolder mHolder;
    private Canvas mCanvas;

    private boolean mDraw = false;//是否进行绘制
    private ExecutorService mSingleThreadPool;//线程池
    private static final int TIME_IN_FRAME = 36;//40毫秒重绘一次

    public BaseGameSurfaceView(Context context, GameSound gameSound) {
        super(context);
        mGameSound = gameSound;
        mHolder = getHolder();//获取SurfaceHolder对象
        mHolder.addCallback(this);//注册监听
        mSingleThreadPool = Executors.newSingleThreadExecutor();//构建线程池
        mGameSound.playBGM();//播放背景音乐
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mFirstCreate) {
            onCreate(width, height);
            mFirstCreate = false;
        }
        mDraw = true;
        mSingleThreadPool.execute(this);
    }

    protected abstract void onCreate(int width, int height);

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mDraw = false;
    }

    @Override
    public void run() {
        while (mDraw) {
            long startTime = System.currentTimeMillis(); //取得更新之前的时间
            try {
                mCanvas = mHolder.lockCanvas();
                synchronized (mCanvas) {
                    calculate();
                    drawView(mCanvas);//绘制
                }
            } catch (Exception e) {
                Log.e("BaseGameSurfaceView", "run: " + e);
                Log.e("BaseGameSurfaceView ", "run: " + e.fillInStackTrace());
            } finally {
                if (mCanvas != null)
                    mHolder.unlockCanvasAndPost(mCanvas);//保证每次都将绘图的内容提交
            }


            long endTime = System.currentTimeMillis();//取得更新结束的时间
            int diffTime = (int) (endTime - startTime);//得到差异时间

            Log.d(TAG, "run: diffTime " + diffTime);

            while (diffTime <= TIME_IN_FRAME) {//确保每次更新时间
                diffTime = (int) (System.currentTimeMillis() - startTime);
                Thread.yield();
            }
        }
    }

    /**
     * 先计算
     */
    protected abstract void calculate();

    /**
     * 绘制
     *
     * @param canvas
     */
    protected abstract void drawView(Canvas canvas);

    /**
     * 得到音效
     *
     * @return
     */
    public GameSound getGameSound() {
        return mGameSound;
    }
}
