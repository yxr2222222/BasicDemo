package com.yxr.base.listener

import android.content.Context
import android.view.View
import android.widget.Toast
import com.yxr.base.util.ToastUtil

abstract class NoFastClickListener(private val isNeedToast: Boolean = false) :
    View.OnClickListener {
    companion object {
        private const val DELAY_TIME = 250L
    }

    private var preClickTime = 0L
    private var preClickView: View? = null
    override fun onClick(v: View?) {
        val currentTimeMillis = System.currentTimeMillis()
        if (preClickView != v || currentTimeMillis - preClickTime >= DELAY_TIME) {
            preClickTime = currentTimeMillis
            onSimpleClick(v)
            preClickView = v
        } else {
            if (preClickView == v) {
                preClickView = null
                onDoubleClick(v)
            } else {
                preClickView = v
            }
            if (isNeedToast) {
                toast(v?.context)
            }
        }
    }

    protected open fun toast(context: Context?) {
        context?.let {
            ToastUtil.show("Click slowly")
        }
    }

    protected open fun onDoubleClick(v: View?) {

    }

    abstract fun onSimpleClick(v: View?)

}