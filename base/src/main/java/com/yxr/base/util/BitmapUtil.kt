package com.yxr.base.util

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

class BitmapUtil {
    companion object {
        @JvmStatic
        fun bytes2Bitmap(bytes: ByteArray): Bitmap? {
            var bitmap: Bitmap? = null
            try {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap
        }

        @JvmStatic
        fun view2Bitmap(
            v: View?,
            @ColorInt drawColor: Int = Color.WHITE,
            config: Bitmap.Config = Bitmap.Config.ARGB_8888
        ): Bitmap? {
            if (v == null) return null
            try {
                val bmp = Bitmap.createBitmap(v.width, v.height, config)
                val c = Canvas(bmp)
                c.drawColor(drawColor)
                v.draw(c)
                return bmp
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return null
        }

        @JvmStatic
        fun createBitmap(
            context: Context,
            @DrawableRes drawableRes: Int,
            width: Int,
            height: Int
        ): Bitmap? {
            var bitmap: Bitmap? = null
            try {
                bitmap = BitmapFactory.decodeResource(context.resources, drawableRes)
                bitmap = scaleBitmap(bitmap, width, height)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap
        }

        /**
         * 根据给定的宽和高进行拉伸
         *
         * @param origin    原图
         * @param newWidth  新图的宽
         * @param newHeight 新图的高
         * @param recycleOrigin 是否销毁原图
         * @return new Bitmap
         */
        @JvmStatic
        fun scaleBitmap(
            origin: Bitmap?,
            newWidth: Int,
            newHeight: Int,
            recycleOrigin: Boolean = true
        ): Bitmap? {
            if (origin != null) {
                try {
                    val height = origin.height
                    val width = origin.width
                    val scaleWidth = newWidth.toFloat() / width
                    val scaleHeight = newHeight.toFloat() / height
                    val matrix = Matrix()
                    matrix.postScale(scaleWidth, scaleHeight)
                    // 使用后乘
                    val newBitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, true)
                    if (!origin.isRecycled && recycleOrigin) {
                        origin.recycle()
                    }
                    return newBitmap
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return null
        }

        /**
         * 按比例缩放图片
         *
         * @param origin 原图
         * @param ratio  比例
         * @param recycleOrigin 是否销毁原图
         * @return 新的bitmap
         */
        @JvmStatic
        fun scaleBitmap(
            origin: Bitmap?,
            ratio: Float,
            degrees: Int,
            recycleOrigin: Boolean = true
        ): Bitmap? {
            if (origin != null) {
                try {
                    val width = origin.width
                    val height = origin.height
                    val matrix = Matrix()
                    matrix.postRotate(degrees.toFloat())
                    matrix.preScale(ratio, ratio)
                    val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
                    if (newBM == origin) {
                        return newBM
                    }
                    if (!origin.isRecycled && recycleOrigin) {
                        origin.recycle()
                    }
                    return newBM
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return null
        }

        @JvmStatic
        fun recycleBitmap(bitmap: Bitmap?) {
            if (bitmap != null && !bitmap.isRecycled) {
                try {
                    bitmap.recycle()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }
}