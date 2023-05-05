package com.yxr.base.inf

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.yxr.base.R
import com.yxr.base.widget.TitleBar
import com.yxr.base.widget.status.UIStatus

interface IBaseStatusFun {
    /**
     * 获取TitleBar
     */
    fun titleBar(): TitleBar

    /**
     * 展示标题
     * @param immersive 是否适配沉浸式，true-高度会加上状态栏高度
     * @param isContentNotBelowTitleBar 内容布局是否不below标题栏，true-TitleBar叠加在content上
     * @param dividerHeight 分割线高度
     * @param backgroundColor 标题栏背景颜色
     * @param leftImageResource 标题栏左边的图片资源
     * @param title 标题
     */
    fun showTitleBar(
        title: CharSequence? = null,
        immersive: Boolean = true,
        isContentNotBelowTitleBar: Boolean = false,
        dividerHeight: Int = 1,
        @ColorInt backgroundColor: Int = 0xffffffff.toInt(),
        @DrawableRes leftImageResource: Int = R.drawable.titlebar_return_icon_black,
    )

    fun showLoading(loadingText: String?)

    fun showError(hintMessage: String?, retryText: String?)

    fun changUiStatus(uiStatus: UIStatus)
}