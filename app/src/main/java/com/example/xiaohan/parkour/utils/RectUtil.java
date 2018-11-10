package com.example.xiaohan.parkour.utils;

import android.graphics.Rect;

/**
 * Created by xiaohan on 2018/11/9.
 */

public class RectUtil {

    /**
     * 矩阵工具类
     */

    public static boolean isIntersect(Rect r1, Rect r2) {

        //計算兩矩形可能的相交矩形的邊界
        int maxLeft = r1.left >= r2.left ? r1.left : r2.left;
        int maxTop = r1.top >= r2.top ? r1.top : r2.top;
        int minRight = r1.right <= r2.right ? r1.right : r2.right;
        int minBottom = r1.bottom <= r2.bottom ? r1.bottom : r2.bottom;

        if (maxLeft > minRight || maxTop > minBottom)
            return false;
        else
            return true;

    }

}
