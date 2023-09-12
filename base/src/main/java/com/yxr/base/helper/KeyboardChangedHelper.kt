package com.yxr.base.helper

import android.graphics.Rect
import android.util.Log
import android.view.ViewTreeObserver
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.abs

abstract class KeyboardChangedHelper(val activity: FragmentActivity) :
    ViewTreeObserver.OnGlobalLayoutListener {
    private var screenHeight = 0
    private var rect = Rect()
    private var currKeyboardHeight = -1
    private val lifecycleOb = object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            activity.window.decorView.viewTreeObserver?.removeOnGlobalLayoutListener(this@KeyboardChangedHelper)
            activity.lifecycle.removeObserver(this)
        }
    }

    fun init() {
        activity.lifecycle.removeObserver(lifecycleOb)
        activity.lifecycle.addObserver(lifecycleOb)
        activity.window.decorView.viewTreeObserver?.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        if (screenHeight <= 0) {
            screenHeight = rect.bottom
        }
        // 输入法的高度
        val keyboardHeight = screenHeight - rect.bottom
        var isActive = false
        if (abs(keyboardHeight) > screenHeight / 5) {
            // 超过屏幕五分之一则表示弹出了输入法
            isActive = true
        }
        if (keyboardHeight != currKeyboardHeight) {
            currKeyboardHeight = keyboardHeight
            onKeyboardChanged(isActive, keyboardHeight)
            Log.d("KeyboardChangedHelper", "keyboardHeight: $keyboardHeight, isActive: $isActive")
        }
    }

    abstract fun onKeyboardChanged(active: Boolean, keyboardHeight: Int)
}