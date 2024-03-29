package com.yxr.base.activity

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.yxr.base.R
import com.yxr.base.inf.IBaseUiFun
import com.yxr.base.util.KeyboardUtil
import com.yxr.base.util.ToastUtil
import com.yxr.base.vm.BaseViewModel
import com.yxr.base.widget.dialog.DefaultLoadingDialog

/**
 * Activity基类
 *
 * @author ciba
 * @date 2020/09/17
 */
abstract class BaseActivity<T : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    IBaseUiFun {
    protected abstract val layoutId: Int
    protected lateinit var viewModel: VM

    protected lateinit var binding: T
    protected lateinit var contentWidget: View

    private var loadingDialog: Dialog? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initImmersion()

        binding = DataBindingUtil.inflate(layoutInflater, layoutId, null, false)
        binding.lifecycleOwner = this
        contentWidget = binding.root

        viewModel = createViewModel()
        binding.setVariable(viewModel.viewModelId, viewModel)

        setContentView(contentWidget)

        // 初始化事件监听
        initListener()
        // 初始化数据
        initData()

        viewModel.init()
    }

    override fun onDestroy() {
        dismissLoadingDialog()
        loadingDialog = null
        KeyboardUtil.hideKeyboard(this)
        super.onDestroy()
        binding.unbind()
    }

    override fun onBackPressed() {
        if (viewModel.onBackPressed()) {
            super.onBackPressed()
        }
    }

    /**
     * 是否需要沉浸式
     */
    override fun needImmersion() = false

    /**
     * 状态栏文字是否是深色
     */
    override fun statusBarDarkFont() = true

    /**
     * 状态栏颜色
     */
    override fun statusBarColor() = R.color.white_ffffff

    /**
     * 内容是否顶到状态栏，true-不顶上去，below状态栏
     */
    override fun fitsSystemWindows() = true

    /**
     * toast提示
     */
    override fun toast(@StringRes stringRes: Int) {
        toast(getString(stringRes))
    }

    override fun toast(message: String?) {
        ToastUtil.show(message)
    }

    /**
     * 展示弹框类型的loading
     */
    override fun showLoadingDialog(loadingText: String?) {
        if (!isFinishing) {
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

    /**
     * 隐藏弹框loading
     */
    override fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    protected open fun isLoadingDialogCancelable() = false

    /**
     * 初始化沉浸式相关
     */
    protected open fun initImmersion() {
        if (needImmersion()) {
            ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .statusBarDarkFont(statusBarDarkFont())
                .fitsSystemWindows(false)
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarColor(statusBarColor())
                .statusBarDarkFont(statusBarDarkFont())
                .fitsSystemWindows(fitsSystemWindows())
                .init()
        }
    }

    /**
     * 初始化监听
     */
    protected open fun initListener() {
        viewModel.toastStringOb.observe(this) { message ->
            toast(message)
        }

        viewModel.finishOb.observe(this) {
            if (it == true) finish()
        }

        viewModel.loadingOb.observe(this) { showLoading ->
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
    protected open fun createLoadingDialog(loadingText: String?): Dialog {
        return DefaultLoadingDialog(this)
    }

    abstract fun createViewModel(): VM
}