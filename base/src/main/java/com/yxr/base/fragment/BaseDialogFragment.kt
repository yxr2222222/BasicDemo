package com.yxr.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.yxr.base.util.ToastUtil
import com.yxr.base.vm.BaseViewModel
import com.yxr.base.widget.dialog.DefaultLoadingDialog

abstract class BaseDialogFragment<T : ViewDataBinding, VM : BaseViewModel> : DialogFragment() {
    protected abstract val layoutId: Int
    protected abstract val viewModel: VM

    protected lateinit var binding: T
    protected open lateinit var rootView: View
    var onDismissListener: OnDismissListener? = null

    /**
     * 数据是否加载过了，用于数据懒加载
     */
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

    override fun onStart() {
        super.onStart()
        setDialogStyle()
    }

    override fun onDestroyView() {
        dismissLoadingDialog()
        loadingDialog = null
        super.onDestroyView()
        binding.unbind()
    }

    protected open fun setDialogStyle() {
        dialog?.run {
            window?.setGravity(Gravity.CENTER)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
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
     * 自定义Loading的Dialog
     *
     * @return Loading的Dialog
     */
    protected open fun createLoadingDialog(): Dialog? {
        return if (activity == null) null else DefaultLoadingDialog(activity)
    }

    override fun dismiss() {
        super.dismiss()
        onDismissListener?.onDismiss()
    }

    interface OnDismissListener {
        fun onDismiss()
    }
}