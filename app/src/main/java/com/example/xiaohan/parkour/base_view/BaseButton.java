package com.example.xiaohan.parkour.base_view;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by xiaohan on 2018/11/7.
 */

public abstract class BaseButton {

    protected int mX, mY;
    protected int mWidth, mHeight;
    protected int mPaddingLeft, mPaddingRight, mPaddingTop, mPaddingBottom;//加个padding

    protected boolean mIsDown = false;//是否按下
    private int pointerId;//手指id

    private static final String TAG = "BaseButton";//基础按钮

    /**
     * 设置padding
     *
     * @param padding
     */
    protected void setPadding(int padding) {
        mPaddingLeft = mPaddingRight = mPaddingBottom = mPaddingTop = padding;
    }

    /**
     * @param downX
     * @param downY
     * @return
     */
    protected boolean isInBtRange(float downX, float downY) {

        if (downX >= mX - mPaddingLeft && downX <= mX + mWidth + mPaddingRight) {
            if (downY >= mY - mPaddingTop && downY <= mY + mHeight + mPaddingBottom)
                return true;
        }

        return false;
    }

    public abstract void draw(Canvas canvas);

    private int downNum = 0;

    /**
     * 处理按下事件的
     */
    public void onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (!mIsDown) {//如果还未按下

                    downNum++;

                    int pointerCount = event.getPointerCount();//屏幕上指针个数
                    for (int i = pointerCount - 1; i >= 0; i--)

                        if (isInBtRange(event.getX(i), event.getY(i))) {
                            pointerId = i;
                            mIsDown = true;
                            if (mOnButtonTouchListener != null)
                                mOnButtonTouchListener.onDown();//按下了
                            break;
                        }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDown) {
                    if (!isInBtRange(event.getX(pointerId), event.getY(pointerId))) {
                        mIsDown = false;//已经没按下了
                        if (mOnButtonTouchListener != null)
                            mOnButtonTouchListener.onUp();//抬起了
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                if (mIsDown) {
                    if (pointerId == event.getActionIndex()) {
                        if (mOnButtonTouchListener != null)
                            mOnButtonTouchListener.onUp();//抬起了
                        mIsDown = false;
                    } else if (pointerId > event.getActionIndex())
                        pointerId--;
                }
                break;
        }

    }


    public interface OnButtonTouchListener {
        void onDown();//按下

        void onUp();//抬起
    }

    private OnButtonTouchListener mOnButtonTouchListener;

    /**
     * 设置按钮触摸监听
     *
     * @param onButtonTouchListener
     */
    public void setOnButtonTouchListener(OnButtonTouchListener onButtonTouchListener) {
        mOnButtonTouchListener = onButtonTouchListener;
    }
}
