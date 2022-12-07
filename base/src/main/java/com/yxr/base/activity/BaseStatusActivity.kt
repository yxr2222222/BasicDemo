package com.yxr.base.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import com.yxr.base.R
import com.yxr.base.helper.StatusViewHelper
import com.yxr.base.inf.IBaseStatusFun
import com.yxr.base.listener.NoFastClickListener
import com.yxr.base.vm.BaseStatusViewModel
import com.yxr.base.widget.TitleBar
import com.yxr.base.widget.status.MultipleStatusView
import com.yxr.base.widget.status.UIStatus

/**
 * @author ciba
 * @description 多UI状态Activity基类
 * @date 2020/09/17
 */
abstract class BaseStatusActivity<T : ViewDataBinding, VM : BaseStatusViewModel> :
    BaseActivity<T, VM>(), IBaseStatusFun {
    private lateinit var statusViewHelper: StatusViewHelper

    @SuppressLint("InflateParams")
    override fun setContentView(view: View) {
        // 创建多状态控件
        val statusView = LayoutInflater.from(this)
            .inflate(R.layout.layout_base_status, null, false) as MultipleStatusView
        // 设置重试按钮点击事件
        statusView.setOnRetryClickListener(object : NoFastClickListener() {
            override fun onSimpleClick(v: View?) {
                reloadData()
            }
        })
        // 添加内容控件
        statusView.addView(view, 0, statusView.DEFAULT_LAYOUT_PARAMS)
        // 设置默认的加载中控件
        statusView.setLoadingLayoutResId(R.layout.layout_default_anim_loading)

        statusViewHelper = StatusViewHelper(statusView)

        super.setContentView(statusView)
    }

    override fun onDestroy() {
        statusViewHelper.release()
        super.onDestroy()
    }

    override fun fitsSystemWindows() = false

    override fun initListener() {
        super.initListener()
        viewModel.status.observe(this) { status ->
            statusViewHelper.statusChanged(status)
        }
    }

    override fun titleBar(): TitleBar {
        return statusViewHelper.titleBar()
    }

    override fun showTitleBar(
        title: CharSequence?,
        immersive: Boolean,
        isContentNotBelowTitleBar: Boolean,
        dividerHeight: Int,
        backgroundColor: Int,
        leftImageResource: Int
    ) {
        statusViewHelper.showTitleBar(
            title,
            immersive,
            isContentNotBelowTitleBar,
            dividerHeight,
            backgroundColor,
            leftImageResource
        )
    }

    override fun showLoading(loadingText: String?) {
        statusViewHelper.showLoading(loadingText)
    }

    override fun showNetworkError(hintMessage: String?, retryText: String?) {
        statusViewHelper.showNetworkError(hintMessage, retryText)
    }

    override fun changUiStatus(uiStatus: UIStatus) {
        statusViewHelper.changUiStatus(uiStatus)
    }

    /**
     * 重新加载数据;
     */
    protected open fun reloadData() {
        viewModel.reloadData()
    }

    fun getStatusViewHelper() = statusViewHelper
}