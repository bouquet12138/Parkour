package com.example.xiaohan.parkour.utils;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

public class BitmapCompressUtil {

    /**
     * 将bitmap压缩到指定大小
     *
     * @param bitmap
     * @param targetWidth
     * @return
     */
    public static Bitmap compressToTargetW(@Nullable Bitmap bitmap, float targetWidth) {

        float targetHeight = bitmap.getHeight() * (targetWidth / bitmap.getWidth());//计算底部高
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) targetWidth, (int) targetHeight, true);
        return bitmap;
    }

    /**
     * 将bitmap压缩到指定大小
     *
     * @param bitmap
     * @param targetHeight
     * @return
     */
    public static Bitmap compressToTargetH(@Nullable Bitmap bitmap, float targetHeight) {

        float targetWidth = bitmap.getWidth() * (targetHeight / bitmap.getHeight());//计算底部高
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) targetWidth, (int) targetHeight, true);
        return bitmap;
    }

}
