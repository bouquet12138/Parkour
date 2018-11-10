package com.example.xiaohan.parkour.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.example.xiaohan.parkour.R;
import com.example.xiaohan.parkour.base_view.BaseButton;
import com.example.xiaohan.parkour.utils.GameSound;


public class GameSurfaceView extends BaseGameSurfaceView {

    private boolean mGameOver = true;//是否游戏结束
    private boolean mJumpDown = false;

    private static final String TAG = "GameSurfaceView";

    private int mCurrentMove = 0;//当前移动距离
    private int mMaxMove;//能够移动的最大距离
    private int mSpeedX;//移动的速度

    private BackGround mBackGround1;//背景
    private BackGround mBackGround2;//前景
    private BackGround mBackGround3;//前景
    private Hero mHero;//主人公
    private Map mMap;//地图
    private Gold mGold;//金币

    private MyButton mJumpBt;//跳按钮
    private MyButton mSlideBt;//滑动按钮

    public static final int X_NUM = 80;//屏幕X轴有多少网格
    public static final int Y_NUM = 40;//屏幕Y轴有多少网格


    private int mUnitX;//移动的单位X
    private int mUnitY;//移动的单位Y

    public GameSurfaceView(Context context, GameSound gameSound) {
        super(context, gameSound);
    }

    /**
     * 尺寸改变时
     *
     * @param width
     * @param height
     */
    @Override
    protected void onCreate(int width, int height) {
        Log.d(TAG, "onSurfaceChanged: width " + width + " height " + height);
        mMaxMove = 2 * width;//最多移3个屏
        mUnitX = width / X_NUM;//程序最小的单位X
        mUnitY = height / Y_NUM;//程序最小的单位Y

        Log.d(TAG, "onCreate: mUnitY " + mUnitY);

        mSpeedX = mUnitX;//能够移动的最大速度
        initView();
        initListener();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mBackGround1 = new BackGround(GameSurfaceView.this, R.drawable.background_a_a, 0, (int) (getHeight() * 0.65f));//背景图1
        mBackGround2 = new BackGround(GameSurfaceView.this, R.drawable.background_a_b, (int) (getHeight() * 0.38f), (int) (getHeight() * 0.9f));//背景图2
        mBackGround3 = new BackGround(GameSurfaceView.this, R.drawable.background_a_c, (int) (getHeight() * 0.4f), getHeight());//背景图3
        mSlideBt = new MyButton(GameSurfaceView.this, mUnitX * 2, getHeight() - mUnitY * 12, mUnitX * 10, mUnitY * 10, R.drawable.slide);//滑动按钮
        mJumpBt = new MyButton(GameSurfaceView.this, getWidth() - mUnitX * 12, getHeight() - mUnitY * 12, mUnitX * 10, mUnitY * 10, R.drawable.jump);//跳按钮
        mMap = new Map(GameSurfaceView.this, R.drawable.map_a, 0);//第0关
        mHero = new Hero(GameSurfaceView.this, R.drawable.hero);//主人公
        mGold = new Gold(GameSurfaceView.this, 0);//第0关
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mJumpBt.setOnButtonTouchListener(new BaseButton.OnButtonTouchListener() {
            @Override
            public void onDown() {
                mJumpDown = true;//跳跃按钮按下了
            }

            @Override
            public void onUp() {
            }
        });
    }

    /**
     * 处理触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {

        mJumpBt.onTouchEvent(event);//处理一下
        //mSlideBt.onTouchEvent(event);//处理一下
        return true;

    }

    /**
     * 计算
     */
    @Override
    protected void calculate() {

        mCurrentMove += mSpeedX;//加等于速度
        if (mCurrentMove <= mUnitX * 30) {
            mHero.setX(mCurrentMove);//移动用户
            mBackGround1.setNowX(0);
            mBackGround2.setNowX(0);
            mBackGround3.setNowX(0);
            mGold.setNowX(0);
            mMap.setNowX(0);
        } else {
            int move = mCurrentMove - mUnitX * 30;
            mBackGround1.setNowX(move / 10);
            mBackGround2.setNowX(move / 3);
            mBackGround3.setNowX(move / 2);
            mMap.setNowX(move);//设置一下地图移动
            mGold.setNowX(move);//设置金币的X
        }

        int canCatch = mMap.canCatch(mHero);
        if (mJumpDown) {
            if (canCatch != -1)
                mHero.setCurrentState(Hero.JUMP);
            else {
                Log.e(TAG, "calculate: " + mHero.getHeroRect());
            }
        }

        if (mHero.getCurrentState() == Hero.JUMP)
            mHero.jump(mUnitY);//向上升高
        else {

            if (canCatch < 0) {//如果接不住
                mHero.drop();//用户下落
                mHero.setCurrentState(Hero.DROP);//下落吧
                canCatch = mMap.canCatch(mHero);//继续捕获一下 防止下落过界
                if (canCatch >= 0) {
                    mHero.setY(mHero.getY() - canCatch);//设置英雄的位置
                    if (!mJumpDown)
                        mHero.setCurrentState(Hero.RUN);//跑去吧
                    else {
                        mHero.setCurrentState(Hero.JUMP);//变为跳
                        Log.e(TAG, "calculate: 这里进来的");
                    }
                }
            } else {
                if (canCatch > 0)
                    mHero.setY(mHero.getY() - canCatch);//设置英雄的位置
                mHero.setCurrentState(Hero.RUN);//跑去吧
            }
        }

        if (mMap.leftCollision(mHero))//如果左侧碰到了
            mHero.setX(mHero.getX() - mSpeedX);//望回退
        else {
            if (mHero.getCurrentState() == Hero.RUN && mHero.getX() < mUnitX * 30 && mCurrentMove > mUnitX * 30) {
                mHero.setX(mHero.getX() + mSpeedX);//英雄向右移动
                if (mMap.leftCollision(mHero))//如果左侧碰到了
                    mHero.setX(mHero.getX() - mSpeedX);//再退回去
            }
        }


        mGold.eatGold(mHero);//吃下金币

        mJumpDown = false;

    }

    /**
     * 绘制方法
     *
     * @param canvas
     */
    @Override
    protected void drawView(Canvas canvas) {

        mBackGround1.draw(canvas);//绘制一下
        mBackGround2.draw(canvas);//绘制一下
        mBackGround3.draw(canvas);//绘制一下
        mMap.draw(canvas);//绘制地下的地图
        mGold.draw(canvas);//绘制金币
        mHero.draw(canvas);//绘制一下人物

        mJumpBt.draw(canvas);//绘制
        mSlideBt.draw(canvas);//绘制
    }

    public int getUnitX() {
        return mUnitX;
    }

    public int getUnitY() {
        return mUnitY;
    }

    /**
     * 得到屏幕宽推荐用这个
     *
     * @return
     */
    public int getViewWidth() {
        return mUnitX * X_NUM;//屏幕宽
    }

    /**
     * 得到屏幕高推荐用这个
     *
     * @return
     */
    public int getViewHeight() {
        return mUnitX * X_NUM;//屏幕宽
    }


}
