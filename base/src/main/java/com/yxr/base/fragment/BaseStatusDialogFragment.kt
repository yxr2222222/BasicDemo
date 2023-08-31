package com.yxr.base.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
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
 * @description 多UI状态Fragment基类
 * @date 2020/10/16
 */
abstract class BaseStatusDialogFragment<T : ViewDataBinding, VM : BaseStatusViewModel> :
    BaseDialogFragment<T, VM>(), IBaseStatusFun {
    private lateinit var statusViewHelper: StatusViewHelper

    @SuppressLint("InflateParams")
    override fun initBinding(inflater: LayoutInflater): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, null, false)
        binding.lifecycleOwner = this

        binding.setVariable(viewModel.viewModelId, viewModel)

        // 创建多状态控件
        val statusView = LayoutInflater.from(activity)
            .inflate(R.layout.layout_base_status, null, false) as MultipleStatusView
        // 设置重试按钮点击事件
        statusView.setOnRetryClickListener(object : NoFastClickListener() {
            override fun onSimpleClick(v: View?) {
                reloadData()
            }
        })
        // 添加内容控件
        statusView.addView(binding.root, 0, statusView.DEFAULT_LAYOUT_PARAMS)
        // 设置默认的加载中控件
        statusView.setLoadingLayoutResId(R.layout.layout_default_anim_loading)

        statusViewHelper = StatusViewHelper(statusView)
        return statusView
    }

    override fun initListener() {
        super.initListener()
        viewModel.status.observe(viewLifecycleOwner) { status ->
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

    override fun showError(hintMessage: String?, retryText: String?) {
        statusViewHelper.showError(hintMessage, retryText)
    }

    override fun changUiStatus(uiStatus: UIStatus) {
        statusViewHelper.changUiStatus(uiStatus)
    }

    override fun onDestroyView() {
        statusViewHelper.release()
        super.onDestroyView()
    }

    /**
     * 重新加载数据
     */
    open fun reloadData() {
        viewModel.reloadData()
    }
}