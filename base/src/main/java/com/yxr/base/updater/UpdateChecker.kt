package com.yxr.base.updater

import java.io.Serializable

abstract class UpdateChecker : Serializable {
    /**
     * 是否需要更新
     */
    abstract fun isNeedUpdate(): Boolean

    /**
     * 更新提醒的标题，默认UI中该值为空的话将隐藏该UI
     */
    abstract fun title(): String?

    /**
     * 更新提醒的内容说明，默认UI中该值为空的话将隐藏该UI
     */
    abstract fun message(): String?

    /**
     * 更新提醒的背景图片，默认UI中该值为空的话将使用默认背景图
     */
    abstract fun image(): String?

    /**
     * 新版本号，默认UI中该值为空的话将隐藏该UI
     */
    abstract fun version(): String?

    /**
     * 暂不更新按钮文案，默认UI中该值为空的话将使用默认文案
     */
    abstract fun notNowBtnTxt(): String?

    /**
     * 立即更新按钮文案，默认UI中该值为空的话将使用默认文案
     */
    abstract fun updateNowBtnTxt(): String?

    /**
     * 更新类型，配合schemeUrl使用
     * 例如：DOWNLOAD类型的schemeUrl应该是APK下载地址、APP_STORE类型的应是跳转应用市场的SCHEME、WEB类型的应该是网页地址
     */
    abstract fun updateType(): UpdateType

    /**
     * 操作地址，配合updateType使用
     */
    abstract fun schemeUrl(): String

    /**
     * 是否是强制更新，如果是则不显示暂不更新按钮，且不可退出当前界面
     */
    abstract fun isForceUpdate(): Boolean
}