package com.example.xiaohan.parkour.custom_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.xiaohan.parkour.base_view.BaseButton;

/**
 * Created by xiaohan on 2018/11/7.
 */

public class MyButton extends BaseButton {

    private GameSurfaceView mView;
    private int mImgRes;

    private Paint mAlphaPaint;//透明度画笔
    private Bitmap mBtBitmap;//跳按钮bitmap
    private Bitmap mDownBitmap;//跳按钮bitmap

    private int mDownX, mDownY;

    public MyButton(GameSurfaceView view, int x, int y, int width, int height, int imgRes) {
        mView = view;
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        mImgRes = imgRes;
        init();
    }

    private void init() {

        mBtBitmap = BitmapFactory.decodeResource(mView.getResources(), mImgRes);
        mBtBitmap = Bitmap.createScaledBitmap(mBtBitmap, mWidth, mHeight, true);//压缩一下

        mAlphaPaint = new Paint();
        mAlphaPaint.setAlpha(160);//设置一下透明度

        mDownBitmap = Bitmap.createScaledBitmap(mBtBitmap, (int) (mWidth * 0.9f), (int) (mHeight * 0.9f), true);//压缩一下

        mDownX = (int) (mX + mWidth * 0.05f);
        mDownY = (int) (mY + mHeight * 0.05f);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!mIsDown)//如果没有按下
            canvas.drawBitmap(mBtBitmap, mX, mY, null);//绘制一下bitmap
        else
            canvas.drawBitmap(mDownBitmap, mDownX, mDownY, mAlphaPaint);//绘制一下bitmap
    }
}

