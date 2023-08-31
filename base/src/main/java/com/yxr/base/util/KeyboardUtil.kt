package com.yxr.base.util

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


class KeyboardUtil {
    companion object {
        /**
         * 隐藏软键盘
         */
        fun hideKeyboard(activity: Activity) {
            try {
                val inputMethodManager: InputMethodManager =
                    activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        /**
         * 打开键盘
         */
        fun showKeyboard(view: EditText) {
            try {
                val inputMethodManager: InputMethodManager =
                    view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                view.requestFocus()
                inputMethodManager.showSoftInput(view, 0)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}