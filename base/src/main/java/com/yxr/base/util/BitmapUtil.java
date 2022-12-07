package com.yxr.base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

public class BitmapUtil {
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap view2Bitmap(@NonNull View v) {
        try {
            Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            c.drawColor(Color.WHITE);
            v.draw(c);
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap createBitmap(@NonNull Context context, @DrawableRes int drawableRes, int width, int height) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), drawableRes);
            bitmap = scaleBitmap(bitmap, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin != null) {
            try {
                int height = origin.getHeight();
                int width = origin.getWidth();
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 使用后乘
                Bitmap newBitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
                if (!origin.isRecycled()) {
                    origin.recycle();
                }
                return newBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, float ratio, int degrees) {
        if (origin != null) {
            try {
                int width = origin.getWidth();
                int height = origin.getHeight();
                Matrix matrix = new Matrix();
                matrix.postRotate(degrees);
                matrix.preScale(ratio, ratio);
                Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
                if (newBM.equals(origin)) {
                    return newBM;
                }
                origin.recycle();
                return newBM;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                bitmap.recycle();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
