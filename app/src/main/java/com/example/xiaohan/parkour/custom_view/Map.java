package com.example.xiaohan.parkour.custom_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;


import com.example.xiaohan.parkour.bean.MapBean;
import com.example.xiaohan.parkour.utils.RectUtil;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import static com.example.xiaohan.parkour.map.MapHelper.*;

public class Map {

    private static final String TAG = "Map";

    private int mNowX;//地图现在的X
    private GameSurfaceView mView;//基础的游戏View
    private int mImgRes;//图片源

    private Bitmap[][] mBitmapArr = new Bitmap[2][6];//存地图的bitmap数组
    private int mMapWidth, mMapHeight;//一个地图Bean宽 高

    private int mPass;//当前是第几关
    private List<MapBean[][]> mMapBeanList = new ArrayList<>();//存放一个屏 一个屏的地图bean

    public Map(GameSurfaceView view, int imgRes, int pass) {
        mView = view;
        this.mImgRes = imgRes;//地图图片源
        this.mPass = pass;//记录一下第几关
        init();
        initMap();
    }

    /**
     * 初始化
     */
    private void init() {
        Bitmap bitmap = BitmapFactory.decodeResource(mView.getResources(), mImgRes);//将障碍物图片读取进来

        mMapWidth = mView.getUnitX() * 4;//一个小地图宽
        mMapHeight = mView.getUnitY() * 4;//一个小地图高

        Log.d(TAG, "init: mMapHeight " + mMapHeight);

        float targetWidth = mMapWidth * mBitmapArr[0].length;
        float targetHeight = mMapHeight * mBitmapArr.length;

        bitmap = Bitmap.createScaledBitmap(bitmap, (int) targetWidth, (int) targetHeight, true);//压缩一下

        for (int i = 0; i < mBitmapArr.length; i++)//裁剪图片
            for (int j = 0; j < mBitmapArr[0].length; j++) {
                mBitmapArr[i][j] = Bitmap.createBitmap(bitmap, mMapWidth * j, mMapHeight * i,
                        mMapWidth, mMapHeight);
            }

    }

    /**
     * 初始化地图
     */
    private void initMap() {
        int[][][] mapList = sTotalMap.get(mPass);

        for (int i = 0; i < mapList.length; i++) {
            int[][] maps = mapList[i];

            MapBean[][] mapBeans = new MapBean[maps.length][maps[0].length];//初始化一个mapBean

            Random random = new Random();

            //region 遍历生成图片区域
            for (int k = 0; k < maps[0].length; k++) {
                boolean hasTop = false;
                for (int j = 0; j < maps.length; j++) {

                    if (maps[j][k] == 0) {
                        mapBeans[j][k] = new MapBean(k * mMapWidth, (mView.getHeight() - (maps.length - j) * mMapHeight), 0, 0, false);
                        continue;//跳过此次循环
                    }

                    boolean isLeft = ((k == 0 && i == 0)
                            || (k == 0 && i != 0 && mapList[i - 1][j][maps[0].length - 1] == 0)
                            || (k != 0 && maps[j][k - 1] == 0));
                    boolean isRight = false;
                    if (!isLeft)
                        isRight = ((k == maps[0].length - 1 && i == mapList.length - 1)
                                || (k == maps[0].length - 1 && i != mapList.length - 1 && mapList[i + 1][j][0] == 0)
                                || (k != maps[0].length - 1 && maps[j][k + 1] == 0));

                    int imageX = 0, imageY = 0;

                    if (isLeft) {//如果是最左边
                        if (hasTop)
                            imageY = 1;
                    } else if (isRight) {//如果是最右边
                        imageX = 4;
                        if (hasTop)
                            imageY = 1;
                    } else {
                        if (!hasTop) {
                            int r = random.nextInt(5);
                            if (r < 3) imageX = 1;
                            else if (r < 4) imageX = 2;
                            else imageX = 3;
                        } else {

                            boolean leftTop = ((k != 0 && maps[j - 1][k - 1] == 0) ||
                                    (k == 0 && i != 0 && mapList[i - 1][j - 1][maps[0].length - 1] == 0)
                            );
                            boolean rightTop = false;
                            if (!leftTop)
                                rightTop = ((k != maps[0].length - 1 && maps[j - 1][k + 1] == 0)
                                        || (k == maps[0].length - 1 && i != mapList.length - 1 && mapList[i + 1][j - 1][0] == 0)
                                );

                            if (leftTop)//左上角为空
                                imageX = 5;
                            else if (rightTop) {//右上角为空
                                imageX = 5;
                                imageY = 1;
                            } else {
                                imageY = 1;
                                int r = random.nextInt(5);
                                if (r < 3) imageX = 3;
                                else if (r < 4) imageX = 2;
                                else imageX = 1;
                            }
                        }
                    }

                    mapBeans[j][k] = new MapBean(k * mMapWidth, (mView.getHeight() - (maps.length - j) * mMapHeight), imageX, imageY, true);
                    hasTop = true;//有顶
                }
            }
            //endregion
            mMapBeanList.add(mapBeans);//将地图添加进去
        }

    }

    /**
     * 设置地图现在的位置
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

        int screenNum = mNowX / mView.getViewWidth() % mMapBeanList.size();
        MapBean[][] mapBeans = mMapBeanList.get(screenNum);//得到当前屏

        int start = nowX / mMapWidth;

        for (int j = start; j < mapBeans[0].length; j++) {
            for (int i = 0; i < mapBeans.length; i++) {
                MapBean bean = mapBeans[i][j];
                if (bean.isDraw()) //如果绘制才绘制
                    canvas.drawBitmap(mBitmapArr[bean.getImgY()][bean.getImgX()], bean.getX() - nowX, bean.getY(), null);
            }
        }

        MapBean[][] mapBeans1 = mMapBeanList.get((screenNum + 1) % mMapBeanList.size());//得到下一个屏
        for (int j = 0; j < start + 1; j++) {
            for (int i = 0; i < mapBeans1.length; i++) {
                MapBean bean = mapBeans1[i][j];
                if (bean.isDraw()) //如果绘制才绘制
                    canvas.drawBitmap(mBitmapArr[bean.getImgY()][bean.getImgX()], bean.getX() - nowX + mView.getViewWidth(), bean.getY(), null);
            }
        }

    }

    /**
     * 是否能接住 返回-1表示接不住
     *
     * @param hero
     * @return
     */
    public int canCatch(Hero hero) {

        int screenNum = mNowX / mView.getViewWidth() % mMapBeanList.size();
        int nowX = mNowX % mView.getViewWidth();
        int index1 = (hero.getX() + nowX) / mMapWidth;//用户现在站在哪个位置

        int canCatch1 = canCatch(hero, index1, screenNum, nowX);


        if (canCatch1 != -1)
            return canCatch1;

        int index2 = (hero.getX() + nowX + hero.getHeroWidth() - mMapWidth / 4 * 3) / mMapWidth;//用户站的结束位置

        int canCatch2 = canCatch(hero, index2, screenNum, nowX);

        return canCatch2;

    }

    private int canCatch(Hero hero, int index, int screenNum, int nowX) {

        MapBean[][] mapBeans;
        int mapLeft;

        if (index >= 20) {
            index %= 20;//取余
            mapBeans = mMapBeanList.get((screenNum + 1) % mMapBeanList.size());
            mapLeft = mView.getViewWidth() - nowX + mapBeans[0][index].getX();
        } else {
            mapBeans = mMapBeanList.get(screenNum);
            mapLeft = -nowX + mapBeans[0][index].getX();
        }


        for (int i = 0; i < mapBeans.length; i++) {
            if (mapBeans[i][index].isDraw()) //第一个可见的
            {
                int mapTop = mapBeans[i][index].getY();//地图顶部
                Rect mapRect = new Rect(mapLeft, mapTop, mapLeft + mMapWidth, mapTop + mMapHeight);

                if (RectUtil.isIntersect(hero.getHeroRect(), mapRect)) {
                    return hero.getY() - (mapTop + mMapHeight / 2 - hero.getHeroHeight());
                }
                break;
            }
        }
        return -1;
    }

    /**
     * 左侧碰撞 方法
     *
     * @param hero
     * @return 返回真 表示能碰到 假表示碰不到
     */
    public boolean leftCollision(Hero hero) {

        int screenNum = mNowX / mView.getViewWidth() % mMapBeanList.size();

        int nowX = mNowX % mView.getViewWidth();
        int index = (hero.getX() + nowX + hero.getHeroWidth()) / mMapWidth;//算一下英雄碰到的最左边碰到的索引


        MapBean[][] mapBeans;
        int mapLeft = mMapWidth / 2;//左侧有一些空白的地方

        if (index >= 20) {
            index %= 20;//取余
            mapBeans = mMapBeanList.get((screenNum + 1) % mMapBeanList.size());
            mapLeft += mView.getViewWidth() - nowX + mapBeans[0][index].getX();
        } else {
            mapBeans = mMapBeanList.get(screenNum);
            mapLeft += mapBeans[0][index].getX() - nowX;
        }

        for (int i = 0; i < mapBeans.length; i++) {
            if (mapBeans[i][index].isDraw() && hero.getX() + hero.getHeroWidth() == mapLeft) {//如果绘制 X是相同的

                int heroStart = hero.getY() - mMapHeight / 2;
                int heroEnd = heroStart + hero.getHeroHeight();

                int mapStart = mapBeans[i][index].getY();
                int mapEnd = mapStart + mMapHeight;

                if ((heroStart >= mapStart && heroStart < mapEnd)
                        || (heroEnd <= mapEnd && heroEnd > mapStart)
                        || (heroStart < mapStart && heroEnd > mapEnd)
                        || (heroStart > mapStart && heroEnd < mapEnd))
                    return true;//返回真左侧碰到了
            }
        }
        return false;//默认是没碰到
    }

    /**
     * 得到一小个地图的宽
     *
     * @return
     */
    public int getMapWidth() {
        return mMapWidth;
    }

    /**
     * 得到一小个地图的高
     *
     * @return
     */
    public int getMapHeight() {
        return mMapHeight;
    }
}
