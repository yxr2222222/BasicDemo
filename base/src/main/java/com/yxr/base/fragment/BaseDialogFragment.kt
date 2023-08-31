package com.yxr.base.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
    protected lateinit var viewModel: VM

    protected lateinit var binding: T
    protected open lateinit var rootView: View
    var onDismissListener: OnDismissListener? = null

    private var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = createViewModel()

        rootView = initBinding(inflater)

        // 初始化事件监听
        initListener()
        initData()

        viewModel.init()
        return rootView
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
                    loadingDialog?.show()
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
        viewModel.dismissFragmentOb.observe(viewLifecycleOwner) {
            if (it == true) {
                dismiss()
            }
        }

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
     * 自定义Loading的Dialog
     *
     * @return Loading的Dialog
     */
    protected open fun createLoadingDialog(loadingText: String?): Dialog? {
        return if (activity == null) null else DefaultLoadingDialog(activity)
    }

    override fun dismiss() {
        onInnerDismiss()
        super.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        onInnerDismiss()
        super.onDismiss(dialog)
    }

    private fun onInnerDismiss() {
        onDismissListener?.onDismiss()
        onDismissListener = null
    }

    abstract fun createViewModel(): VM

    interface OnDismissListener {
        fun onDismiss()
    }
}