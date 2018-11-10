package com.example.xiaohan.parkour.bean;

/**
 * Created by xiaohan on 2018/11/8.
 */

public class GoldBean {

    private int mX,mY;//X坐标 Y坐标

    private int mGoldType;//金币类型

    private boolean mDraw;//是否绘制

    public GoldBean(int x, int y, int goldType, boolean draw) {
        mX = x;
        mY = y;
        mGoldType = goldType;
        mDraw = draw;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getGoldType() {
        return mGoldType;
    }

    public boolean isDraw() {
        return mDraw;
    }

    public void setGoldType(int goldType) {
        mGoldType = goldType;
    }

    public void setDraw(boolean draw) {
        mDraw = draw;
    }
}
