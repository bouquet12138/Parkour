package com.example.xiaohan.parkour.custom_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.xiaohan.parkour.R;
import com.example.xiaohan.parkour.bean.GoldBean;
import com.example.xiaohan.parkour.utils.RectUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.xiaohan.parkour.map.GoldMapHelper.*;

/**
 * Created by xiaohan on 2018/11/8.
 */

public class Gold {

    private static final String TAG = "Gold";

    private Bitmap[][] mBitmapArr = new Bitmap[3][2];//存金币的bitmap数组
    private int mGoldWidth, mGoldHeight;//一个地图Bean宽 高

    private GameSurfaceView mView;

    private List<GoldBean[][]> mGoldBeanList = new ArrayList<>();//存放一个屏 一个屏的地图bean

    private int mNowX;//当前X
    private int mPass;//当前第几关

    public Gold(GameSurfaceView view, int pass) {
        mView = view;
        mPass = pass;
        init();
        initGold();
    }

    /**
     * 初始化
     */
    private void init() {
        Bitmap bitmap = BitmapFactory.decodeResource(mView.getResources(), R.drawable.gold);//将金币图片读取进来
        mGoldWidth = mView.getUnitX() * 2;//金币宽
        mGoldHeight = mView.getUnitY() * 2;//金币高

        Log.d(TAG, "init: mGoldWidth " + mGoldWidth);
        Log.d(TAG, "init: mGoldHeight " + mGoldHeight);

        bitmap = Bitmap.createScaledBitmap(bitmap, mGoldWidth * mBitmapArr[0].length, mGoldHeight * mBitmapArr.length, true);//压缩一下

        for (int i = 0; i < mBitmapArr.length; i++)//裁剪图片
            for (int j = 0; j < mBitmapArr[0].length; j++) {
                mBitmapArr[i][j] = Bitmap.createBitmap(bitmap, mGoldWidth * j, mGoldHeight * i,
                        mGoldWidth, mGoldHeight);
            }

    }

    /**
     * 初始化金币
     */
    private void initGold() {

        int[][][] goldMapList = sTotalGoldMap.get(mPass);//得到关卡的金币

        Log.d(TAG, "initGold: goldList " + goldMapList);

        for (int i = 0; i < goldMapList.length; i++) {
            int[][] goldMaps = goldMapList[i];//得到当前屏的金币地图

            GoldBean[][] goldBeans = new GoldBean[goldMaps.length][goldMaps[0].length];//初始化一个goldBean


            for (int j = 0; j < goldMaps.length; j++) {
                for (int k = 0; k < goldMaps[0].length; k++) {
                    if (goldMaps[j][k] == 0) {
                        goldBeans[j][k] = new GoldBean(k * mGoldWidth, j * mGoldHeight, 0, false);//新建一个金币
                    } else {
                        goldBeans[j][k] = new GoldBean(k * mGoldWidth, j * mGoldHeight, goldMaps[j][k], true);//新建一个金币
                    }
                }
            }
            mGoldBeanList.add(goldBeans);//将金币地图添加进去
        }

    }

    /**
     * 设置金币地图现在的位置
     *
     * @param nowX
     */
    public void setNowX(int nowX) {
        mNowX = nowX;
    }

    /**
     * View
     * 绘制的方法
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {


        int nowX = mNowX % mView.getViewWidth();//计算当前X

        int screenNum = mNowX / mView.getViewWidth() % mGoldBeanList.size();
        GoldBean[][] goldBeans = mGoldBeanList.get(screenNum);//得到当前屏的金币

        int start = nowX / mGoldWidth;

        for (int j = start; j < goldBeans[0].length; j++) {
            for (int i = 0; i < goldBeans.length; i++) {
                GoldBean bean = goldBeans[i][j];
                if (bean.isDraw()) {//如果绘制才绘制
                    int imgType = bean.getGoldType();

                    switch (imgType) {
                        case 1:
                            canvas.drawBitmap(mBitmapArr[0][0], bean.getX() - nowX, bean.getY(), null);
                            break;
                        case 2:
                            canvas.drawBitmap(mBitmapArr[1][0], bean.getX() - nowX, bean.getY(), null);
                            break;
                        case 3:
                            canvas.drawBitmap(mBitmapArr[2][0], bean.getX() - nowX, bean.getY(), null);
                            break;

                    }
                }
            }
        }

        GoldBean[][] goldBeans1 = mGoldBeanList.get((screenNum + 1) % mGoldBeanList.size());//得到下一个屏
        for (int j = 0; j < start + 1; j++) {
            for (int i = 0; i < goldBeans1.length; i++) {
                GoldBean bean = goldBeans1[i][j];
                if (bean.isDraw()) //如果绘制才绘制
                {
                    int imgType = bean.getGoldType();
                    switch (imgType) {
                        case 1:
                            canvas.drawBitmap(mBitmapArr[0][0], bean.getX() - nowX + mView.getViewWidth(), bean.getY(), null);
                            break;
                        case 2:
                            canvas.drawBitmap(mBitmapArr[1][0], bean.getX() - nowX + mView.getViewWidth(), bean.getY(), null);
                            break;
                        case 3:
                            canvas.drawBitmap(mBitmapArr[2][0], bean.getX() - nowX + +mView.getViewWidth(), bean.getY(), null);
                            break;

                    }
                }
            }
        }

    }

    /**
     * 吃金币
     *
     * @param hero
     */
    public void eatGold(Hero hero) {

        int screenNum = mNowX / mView.getViewWidth() % mGoldBeanList.size();
        GoldBean[][] goldBeans1 = mGoldBeanList.get(screenNum);

        int nowX = mNowX % mView.getViewWidth();
        int index = (hero.getX() + nowX) / mGoldWidth;//算一下英雄碰到的最左边碰到的索引
        int endIndex = (hero.getX() + nowX + hero.getHeroWidth()) / mGoldWidth;//算一下英雄碰到的最右边碰到的索引


        if (index < 20 && endIndex < 20) {
            for (int i = 0; i < goldBeans1.length; i++) {
                for (int j = index; j < endIndex; j++) {
                    GoldBean goldBean = goldBeans1[i][j];
                    if (goldBean.isDraw()) {

                        Rect rect = new Rect(goldBean.getX() - nowX, goldBean.getY(),
                                goldBean.getX() - nowX + mGoldWidth, goldBean.getY() + mGoldHeight);

                        if (RectUtil.isIntersect(rect, hero.getHeroRect())) {
                            goldBean.setDraw(false);//停止绘制
                            mView.getGameSound().playGoldSound();//播放一下吃金币声音
                        }

                    }
                }
            }
        }

    }

}
