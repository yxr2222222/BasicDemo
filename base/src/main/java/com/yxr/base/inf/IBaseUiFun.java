package com.yxr.base.inf;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

public interface IBaseUiFun {
    /**
     * 是否需要沉浸式
     */
    boolean needImmersion();

    /**
     * 状态栏文字是否是深色
     */
    boolean statusBarDarkFont();

    /**
     * 状态栏颜色
     */
    @ColorRes
    int statusBarColor();

    /**
     * 内容是否顶到状态栏，false-不顶上去，below状态栏
     */
    boolean fitsSystemWindows();

    void toast(@StringRes int stringRes);

    void toast(String message);

    void showLoadingDialog();

    void dismissLoadingDialog();
}
