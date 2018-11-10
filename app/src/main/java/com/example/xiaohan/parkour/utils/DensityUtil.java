package com.example.xiaohan.parkour.utils;


import com.example.xiaohan.parkour.base.MyApplication;

public class DensityUtil {

    private DensityUtil() {

    }

    public static int dipToPx(float dpValue) {
        float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int pxToDp(float pxValue) {
        float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
