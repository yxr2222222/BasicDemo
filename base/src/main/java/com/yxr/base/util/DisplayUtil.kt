package com.yxr.base.util

import com.yxr.base.BaseApplication

class DisplayUtil {
    companion object{
        @JvmStatic
        fun getScreenWidth(): Int {
            return BaseApplication.context.resources.displayMetrics.widthPixels
        }

        @JvmStatic
        fun getScreenHeight(): Int {
            return BaseApplication.context.resources.displayMetrics.heightPixels
        }

        /**
         * convert px to its equivalent dp
         * 将px转换为与之相等的dp
         */
        @JvmStatic
        fun px2dp(pxValue: Float): Int {
            val scale = BaseApplication.context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        /**
         * convert dp to its equivalent px
         * 将dp转换为与之相等的px
         */
        @JvmStatic
        fun dp2px(dipValue: Float): Int {
            val scale = BaseApplication.context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }

        /**
         * convert px to its equivalent sp
         * 将px转换为sp
         */
        @JvmStatic
        fun px2sp(pxValue: Float): Int {
            val fontScale = BaseApplication.context.resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }

        /**
         * convert sp to its equivalent px
         * 将sp转换为px
         */
        @JvmStatic
        fun sp2px(spValue: Float): Int {
            val fontScale = BaseApplication.context.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }
    }
}