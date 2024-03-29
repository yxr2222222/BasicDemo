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
    protected lateinit var viewModel: VM

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
        dataLoaded = false
        viewModel = createViewModel()

        rootView = initBinding(inflater)

        // 初始化事件监听
        initListener()
        initData()

        viewModel.init()

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
        dataLoaded = false
        loadingDialog = null
        super.onDestroyView()
        binding.unbind()
    }

    protected open fun toast(@StringRes stringRes: Int) {
        toast(getString(stringRes))
    }

    protected open fun toast(message: String?) {
        ToastUtil.show(message)
    }

    /**
     * 展示弹框类型的loading
     */
    protected open fun showLoadingDialog(loadingText: String?) {
        activity?.isFinishing?.let {
            if (!it) {
                try {
                    if (loadingDialog == null) {
                        loadingDialog = createLoadingDialog(loadingText)
                        loadingDialog?.setCanceledOnTouchOutside(false)
                    }
                    loadingDialog?.let { loadingDialog ->
                        if (loadingDialog is DefaultLoadingDialog) {
                            loadingDialog.setLoadingText(loadingText)
                        }
                    }
                    loadingDialog?.setCancelable(isLoadingDialogCancelable())
                    if (loadingDialog?.isShowing != true) loadingDialog?.show()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
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

        binding.setVariable(viewModel.viewModelId, viewModel)
        return binding.root
    }

    protected open fun initListener() {
        viewModel.toastStringOb.observe(viewLifecycleOwner) { message ->
            toast(message)
        }

        viewModel.finishOb.observe(viewLifecycleOwner) {
            if (it == true) activity?.finish()
        }

        viewModel.loadingOb.observe(viewLifecycleOwner) { showLoading ->
            if (showLoading != null) {
                if (showLoading.isShowLoading) {
                    showLoadingDialog(showLoading.loadingText)
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
    protected open fun createLoadingDialog(loadingText: String?): Dialog? {
        return if (activity == null) null else DefaultLoadingDialog(activity)
    }

    abstract fun createViewModel(): VM
}