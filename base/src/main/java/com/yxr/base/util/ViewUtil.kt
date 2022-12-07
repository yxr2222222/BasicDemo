package com.yxr.base.util

import android.view.MotionEvent
import android.view.View

class ViewUtil {
    companion object {
        @JvmStatic
        fun simulateClick(view: View?, x:Float, y:Float) {
            val currentTimeMillis = System.currentTimeMillis()

            val downMotionEvent = MotionEvent.obtain(
                currentTimeMillis,
                currentTimeMillis,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                0
            )

            val upMotionEvent = MotionEvent.obtain(
                currentTimeMillis,
                currentTimeMillis,
                MotionEvent.ACTION_UP,
                x,
                y,
                0
            )

            view?.dispatchTouchEvent(downMotionEvent)
            view?.dispatchTouchEvent(upMotionEvent)

            downMotionEvent.recycle()
            upMotionEvent.recycle()
        }
    }
}