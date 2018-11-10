package com.example.xiaohan.parkour.bean;

public class MapBean {

    private int mX;//X坐标
    private int mY;//Y坐标
    private int mImgX;//图片X
    private int mImgY;//图片Y索引
    private boolean mDraw;//是否绘制

    public MapBean(int x, int y, int imgX, int imgY, boolean draw) {
        mX = x;
        mY = y;
        mImgX = imgX;
        mImgY = imgY;
        mDraw = draw;
    }

    public int getX() {
        return mX;
    }

    public void setX(int x) {
        mX = x;
    }

    public int getY() {
        return mY;
    }

    public void setY(int y) {
        mY = y;
    }

    public int getImgX() {
        return mImgX;
    }

    public void setImgX(int imgX) {
        mImgX = imgX;
    }

    public int getImgY() {
        return mImgY;
    }

    public void setImgY(int imgY) {
        mImgY = imgY;
    }

    public boolean isDraw() {
        return mDraw;
    }

    public void setDraw(boolean draw) {
        mDraw = draw;
    }
}
