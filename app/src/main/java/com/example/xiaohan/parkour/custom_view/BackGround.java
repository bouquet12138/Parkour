package com.example.xiaohan.parkour.custom_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 背景图类 用于循环滚动
 */
public class BackGround {

    private static final String TAG = "BackGround";
    private int mStartY;//背景绘制的Y位置
    private Bitmap mBackground = null;//背景图
    private int mBackWidth, mBackHeight;//背景图宽，高

    private GameSurfaceView mView;
    private int mResId;//图片资源id
    private int mNowX;//当前X

    /**
     * 背景图类的构造方法
     *
     * @param view
     * @param resId
     */
    public BackGround(@Nullable GameSurfaceView view, @Nullable int resId, int startY, int endY) {
        mView = view;
        mResId = resId;
        mStartY = startY;
        mBackHeight = endY - startY;
        init();
    }

    /**
     * 初始化
     * 做了小图处理
     */
    private void init() {
        mBackground = BitmapFactory.decodeResource(mView.getResources(), mResId);
        int h = mBackground.getHeight();
        int w = mBackground.getWidth();

        mBackWidth = (int) ((float) mBackHeight / h * w);//图片宽
        mBackground = Bitmap.createScaledBitmap(mBackground, mBackWidth, mBackHeight, true);//压缩一下
        mBackWidth = mBackground.getWidth();//背景宽
        mBackHeight = mBackground.getHeight();//背景宽

        Log.d(TAG, "init: mBackWidth " + mBackWidth);
        Log.d(TAG, "init: mBackWidth " + mBackHeight);

        if (mBackWidth < mView.getWidth()) {
            int num = mView.getWidth() / mBackWidth;
            if (mBackWidth * num < mView.getWidth())
                num++;

            Bitmap endBackground = Bitmap.createBitmap(num * mBackWidth, mBackHeight,
                    mBackground.getConfig());
            Canvas canvas = new Canvas(endBackground);
            for (int i = 0; i < num; i++)
                canvas.drawBitmap(mBackground, mBackWidth * i, 0, null);
            mBackground = endBackground;//替换一下
            mBackWidth = mBackground.getWidth();
        }
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {
        int nowX = mNowX % mBackWidth;//当前移动

        if (nowX > 0) {
            canvas.drawBitmap(mBackground, -nowX, mStartY, null);
            if ((mBackWidth - nowX) < mView.getWidth())//能看到才去画
                canvas.drawBitmap(mBackground, (mBackWidth - nowX), mStartY, null);
        } else if (nowX == 0) {
            canvas.drawBitmap(mBackground, 0, mStartY, null);
        } /*else {
            if (mNowX > (mView.getWidth()))
                canvas.drawBitmap(mBackground, -mNowX, 0, null);
            canvas.drawBitmap(mBackground, -(mBackWidth + mNowX), 0, null);
        }*/
    }

    /**
     * 设置地图的当前位置
     *
     * @param nowX
     */
    public void setNowX(int nowX) {
        mNowX = nowX;
    }
}
