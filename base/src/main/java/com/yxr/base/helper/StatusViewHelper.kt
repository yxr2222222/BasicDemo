package com.yxr.base.helper

import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.yxr.base.R
import com.yxr.base.inf.IBaseStatusFun
import com.yxr.base.model.BaseStatus
import com.yxr.base.widget.DefaultAnimLoadingView
import com.yxr.base.widget.TitleBar
import com.yxr.base.widget.status.MultipleStatusView
import com.yxr.base.widget.status.UIStatus

open class StatusViewHelper(private val statusView: MultipleStatusView) : IBaseStatusFun {
    private var animLoadingView: DefaultAnimLoadingView? = null
    private var tvRetryHint: TextView? = null
    private var tvRetry: TextView? = null

    override fun titleBar(): TitleBar = statusView.titleBar

    override fun showTitleBar(
        title: CharSequence?,
        immersive: Boolean,
        isContentNotBelowTitleBar: Boolean,
        dividerHeight: Int,
        backgroundColor: Int,
        leftImageResource: Int
    ) {
        titleBar().let { titleBar ->
            titleBar.setTitle(title)
            titleBar.setImmersive(immersive)
            titleBar.setDividerHeight(dividerHeight)
            titleBar.setBackgroundColor(backgroundColor)
            titleBar.setLeftImageResource(leftImageResource)
            if (isContentNotBelowTitleBar) {
                statusView.DEFAULT_LAYOUT_PARAMS.removeRule(RelativeLayout.BELOW)
            } else {
                statusView.DEFAULT_LAYOUT_PARAMS.addRule(RelativeLayout.BELOW, titleBar.id)
            }
            titleBar.visibility = View.VISIBLE
        }
    }

    override fun showLoading(loadingText: String?) {
        changUiStatus(UIStatus.LOADING)
        if (animLoadingView == null) {
            val loadingView = statusView.findViewById<View>(R.id.loadingView)
            if (loadingView is DefaultAnimLoadingView) {
                animLoadingView = loadingView
            }
        }
        animLoadingView?.startLoading()
        animLoadingView?.setLoadingText(loadingText)
    }

    override fun showError(hintMessage: String?, retryText: String?) {
        changUiStatus(UIStatus.ERROR)
        if (tvRetryHint == null) {
            tvRetryHint = statusView.findViewById(R.id.tvRetryHint)
        }
        tvRetryHint?.text =
            if (TextUtils.isEmpty(hintMessage)) statusView.context.getString(R.string.load_error_refresh_please) else hintMessage
        if (retryText != null) {
            if (tvRetry == null) {
                tvRetry = statusView.findViewById(R.id.error_retry_view)
            }
            tvRetry?.text = retryText
        }
    }

    override fun changUiStatus(uiStatus: UIStatus) {
        statusView.changUiStatus(uiStatus)
        if (UIStatus.LOADING != uiStatus) {
            animLoadingView?.pauseLoading()
        }
    }

    open fun statusChanged(status: BaseStatus?) {
        if (status != null) {
            val uiStatus = status.status
            if (UIStatus.LOADING == uiStatus) {
                showLoading(status.message)
            } else if (UIStatus.CONTENT == uiStatus) {
                changUiStatus(UIStatus.CONTENT)
            } else if (UIStatus.EMPTY == uiStatus) {
                changUiStatus(UIStatus.EMPTY)
            } else if (UIStatus.ERROR == uiStatus) {
                changUiStatus(UIStatus.ERROR)
            }
        }
    }

    open fun release() {
        animLoadingView?.cancelLoading()
    }
}