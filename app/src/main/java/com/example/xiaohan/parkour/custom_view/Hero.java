package com.example.xiaohan.parkour.custom_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Hero {

    private static final String TAG = "Hero";

    public static final int RUN = 0;
    public static final int JUMP = 1;
    public static final int DASH = 2;
    public static final int DROP = 3;
    public static final int ROLL = 4;//翻滚

    private int mCurrentState = RUN;//当前状态

    private int mNowDropSpeed = 0;//当前掉落速度
    private int mDropNum = 0;//下降次数

    private int mJumpNum = 0;//跳的数目

    private int mImgRes;//当前图片源
    private GameSurfaceView mView;

    private Bitmap[][] mBitmapArr = new Bitmap[4][14];//存放英雄的bitmap数组

    private int mIndexX = 0, mIndexY = RUN;//当前图片x的索引 y索引
    private int mX = 0, mY = 0;//人物当前的 X Y

    private int mHeroWidth, mHeroHeight;//角色宽高

    public Hero(GameSurfaceView view, int imgRes) {
        mImgRes = imgRes;
        mView = view;
        init();//初始化一下
    }

    /**
     * 初始化 压缩图片
     */
    private void init() {
        Bitmap heroBitmap = BitmapFactory.decodeResource(mView.getResources(), mImgRes);//将人物读取进来

        mHeroWidth = mView.getUnitX() * 8;//英雄宽
        mHeroHeight = mView.getUnitY() * 8;//英雄高

        float targetHeight = mHeroHeight * mBitmapArr.length;//计算图片高 因为一共4个小人
        float targetWidth = mHeroWidth * mBitmapArr[0].length;//横向14桢

        heroBitmap = Bitmap.createScaledBitmap(heroBitmap, (int) targetWidth,
                (int) targetHeight, true);//压缩一下 2表示两个状态

        mY = 0;//英雄的Y坐标

        for (int i = 0; i < mBitmapArr.length; i++)
            for (int j = 0; j < mBitmapArr[0].length; j++) {
                mBitmapArr[i][j] = Bitmap.createBitmap(heroBitmap, mHeroWidth * j, mHeroHeight * i,
                        mHeroWidth, mHeroHeight);
            }

    }

    /**
     * 绘制方法
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {

        switch (mCurrentState) {
            case RUN:
                mIndexX++;
                if (mIndexX >= mBitmapArr[0].length)
                    mIndexX = 0;
                break;
            case JUMP:
                mIndexX++;
                if (mIndexX >= 9) {
                    if (mJumpNum != 3)
                        mIndexX = 8;
                    else
                        setCurrentState(DROP);//当前状态改为跑
                    mJumpNum++;
                }
                break;
            case DASH:
                break;
            case DROP:
                mIndexX++;
                if (mIndexX >= mBitmapArr[0].length)
                    mIndexX = 9;
                break;
        }


        Paint paint = new Paint();//绘制红框
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(mX, mY, mX + mHeroWidth, mY + mHeroHeight, paint);
        canvas.drawBitmap(mBitmapArr[mIndexY][mIndexX], mX, mY, null);
    }

    /**
     * 设置用户的当前状态
     *
     * @param state
     */
    public void setCurrentState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;

            switch (mCurrentState) {
                case RUN:
                    mIndexX = 0;
                    mIndexY = 0;
                    break;
                case JUMP:
                    mJumpNum = 0;//跳的数量变为0
                    mIndexX = 0;
                    mIndexY = 1;
                    break;
                case DASH:
                    break;
                case DROP:
                    mNowDropSpeed = 0;
                    mDropNum = 0;//掉下数也归零
                    mIndexX = 9;
                    mIndexY = 1;
                    break;
            }
        }
    }

    /**
     * 得到英雄当前状态
     *
     * @return
     */
    public int getCurrentState() {
        return mCurrentState;
    }

    /**
     * 设置主角X位置
     */
    public void setX(int x) {
        mX = x;
    }

    /**
     * 设置主角Y位置
     */
    public void setY(int y) {
        mY = y;
    }

    public void drop() {

        if (mNowDropSpeed == 0)
            mNowDropSpeed = mView.getUnitY();
        /*else if (mNowDropSpeed < mView.getUnitY() * 2f)
            mNowDropSpeed += mView.getUnitY() / 2;*/
        mDropNum++;

        if (mDropNum % 3 == 0) {
            mDropNum = 0;
            if (mNowDropSpeed < mView.getUnitY() * 4)//最大下落速度
                mNowDropSpeed += mView.getUnitY();//速度加加
        }

        mY += mNowDropSpeed;//下落距离
    }

    /**
     * 跳
     */
    public void jump(int distance) {
        if (mJumpNum == 0)
            mY -= distance;//上跳距离
    }

    /**
     * 得到X
     *
     * @return
     */
    public int getX() {
        return mX;
    }

    /**
     * 得到Y
     *
     * @return
     */
    public int getY() {
        return mY;
    }

    /**
     * 得到用户宽
     *
     * @return
     */
    public int getHeroWidth() {
        return mHeroWidth;
    }

    /**
     * 得到用户高
     *
     * @return
     */
    public int getHeroHeight() {
        return mHeroHeight;
    }


    public Rect getHeroRect() {
        Rect rect = new Rect(mX, mY, mX + mHeroWidth - 1, mY + mHeroHeight - 1);
        return rect;
    }


}
