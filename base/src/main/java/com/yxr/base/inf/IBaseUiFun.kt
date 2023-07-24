package com.yxr.base.inf

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface IBaseUiFun {

    /**
     * 是否需要沉浸式
     */
    fun needImmersion(): Boolean

    /**
     * 状态栏文字是否是深色
     */
    fun statusBarDarkFont(): Boolean

    /**
     * 状态栏颜色
     */
    @ColorRes
    fun statusBarColor(): Int

    /**
     * 内容是否顶到状态栏，false-不顶上去，below状态栏
     */
    fun fitsSystemWindows(): Boolean

    fun toast(@StringRes stringRes: Int)

    fun toast(message: String?)

    fun showLoadingDialog(loadingText: String?)

    fun dismissLoadingDialog()
}