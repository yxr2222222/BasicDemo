package com.yxr.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.yxr.base.util.ToastUtil
import com.yxr.base.vm.BaseViewModel
import com.yxr.base.widget.dialog.DefaultLoadingDialog

/**
 * BaseFragment
 *
 * @author ciba
 * @date 2020/09/17
 */
abstract class BaseFragment<T : ViewDataBinding, VM : BaseViewModel> :
    Fragment() {
    protected abstract val layoutId: Int
    protected abstract val viewModel: VM

    protected lateinit var binding: T
    protected open lateinit var rootView: View

    /**
     * 数据是否加载过了，用于数据懒加载
     */
    private var dataLoaded = false
    private var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = initBinding(inflater)

        // 初始化事件监听
        initListener()
        initData()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        if (!dataLoaded) {
            dataLoaded = true
            lazyInitData()
        }
    }

    override fun onDestroyView() {
        dismissLoadingDialog()
        loadingDialog = null
        super.onDestroyView()
        binding.unbind()
    }

    protected open fun toast(@StringRes stringRes: Int) {
        toast(getString(stringRes))
    }

    protected open fun toast(message: String?) {
        activity?.let {
            ToastUtil.show(it, message)
        }
    }

    /**
     * 展示弹框类型的loading
     */
    protected open fun showLoadingDialog() {
        activity?.isFinishing?.let {
            if (!it) {
                if (loadingDialog == null) {
                    loadingDialog = createLoadingDialog()
                    loadingDialog?.setCanceledOnTouchOutside(false)
                }
                loadingDialog?.setCancelable(isLoadingDialogCancelable())
                loadingDialog?.show()
            }
        }
    }

    protected open fun isLoadingDialogCancelable() = false

    /**
     * 隐藏弹框loading
     */
    protected open fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    protected open fun initBinding(inflater: LayoutInflater): View {
        binding = DataBindingUtil.inflate(inflater, layoutId, null, false)
        binding.lifecycleOwner = this

        viewModel.init()
        binding.setVariable(viewModel.viewModelId, viewModel)
        return binding.root
    }

    protected open fun initListener() {
        viewModel.toastStringOb.observe(viewLifecycleOwner) { message ->
            toast(message)
        }

        viewModel.finishOb.observe(viewLifecycleOwner) {
            activity?.finish()
        }

        viewModel.loadingOb.observe(viewLifecycleOwner) { showLoading ->
            if (showLoading != null) {
                if (showLoading) {
                    showLoadingDialog()
                } else {
                    dismissLoadingDialog()
                }
            }
        }
    }

    /**
     * 初始化数据
     */
    protected open fun initData() {

    }

    /**
     * 懒加载数据
     */
    protected open fun lazyInitData() {

    }

    /**
     * 自定义Loading的Dialog
     *
     * @return Loading的Dialog
     */
    protected open fun createLoadingDialog(): Dialog? {
        return if (activity == null) null else DefaultLoadingDialog(activity)
    }
}