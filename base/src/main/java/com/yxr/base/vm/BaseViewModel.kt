package com.yxr.base.vm

import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yxr.base.R
import com.yxr.base.http.HttpErrorCode
import com.yxr.base.http.extension.launchRequest
import com.yxr.base.http.manager.HttpManager
import com.yxr.base.http.model.IResponse
import com.yxr.base.http.model.NetworkException
import com.yxr.base.http.util.NetworkExceptionUtil
import kotlinx.coroutines.*
import okhttp3.Dispatcher

/**
 * @author ciba
 * @description ViewModel基类
 * @date 2020/9/17
 */
abstract class BaseViewModel(lifecycle: LifecycleOwner) : AbsViewModel(lifecycle),
    View.OnClickListener {
    companion object {
        private const val CLICK_INTERVAL_TIME = 250L
    }

    abstract val viewModelId: Int

    val toastStringOb = MutableLiveData<String>()
    val loadingOb = MutableLiveData<Boolean>()
    val finishOb = MutableLiveData<Boolean>()

    private var preClickTime = 0L
    private var preClickView: Int? = null

    open fun onBackPressed() = true

    final override fun onClick(v: View) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - preClickTime >= CLICK_INTERVAL_TIME) {
            preClickTime = currentTimeMillis
            onSingleClick(v)
            preClickView = v.hashCode()
        } else {
            if (preClickView == v.hashCode()) {
                preClickView = null
                onDoubleClick(v)
            } else {
                preClickView = v.hashCode()
            }
        }
    }

    /**
     * 创建RetrofitService，通过Service获取对应api
     */
    open fun <T : Any> createApi(cls: Class<T>, dispatcher: Dispatcher? = null): T =
        HttpManager.get().createApi(cls, dispatcher)

    /**
     * 快捷请求
     * @param block 需要进行的挂起方法（一般为网络请求之类的）
     * @param onSuccess block方法处理成功回调
     * @param onError block方法处理失败回调
     * @param isNeedLoading 是否需要展示loading弹框
     * @param isShowError 是否在失败时自动展示toast简要信息
     * @param isShowErrorDetail 失败时toast的是否为详细信息
     */
    open fun <E : IResponse> launchRequest(
        block: suspend () -> E?,
        onSuccess: (E?) -> Unit,
        onError: suspend (exception: NetworkException) -> Unit = {},
        isNeedLoading: Boolean = true,
        isShowError: Boolean = false,
        isShowErrorDetail: Boolean = false,
    ) {
        if (isNeedLoading) {
            showLoading()
        }
        viewModelScope.launchRequest(block, onSuccess = { data ->
            if (isNeedLoading) {
                dismissLoading()
            }
            if (data == null) {
                onError(
                    NetworkException(
                        HttpErrorCode.CODE_NULL_DATA,
                        NetworkExceptionUtil.getMessage(R.string.default_null_body_exception),
                    )
                )
            } else if (!data.isSuccess()) {
                onError(
                    NetworkException(
                        HttpErrorCode.CODE_UNKNOWN,
                        data.error()
                            ?: NetworkExceptionUtil.getMessage(R.string.default_null_body_exception),
                    )
                )
            } else {
                onSuccess(data)
            }
        }, onError = { exception ->
            if (isNeedLoading) {
                dismissLoading()
            }
            if (isShowError) {
                if (isShowErrorDetail) {
                    showToast(exception.message + ":" + exception.detail)
                } else {
                    showToast(exception.message)
                }
            }
            onError(exception)
        })
    }

    /**
     * 协程运行在主线程
     */
    open fun launchWithMain(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.Main) { block() }
    }

    /**
     * 协程运行在子线程
     */
    open fun launchWithIO(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) { block() }
    }

    /**
     * 展示Toast
     */
    open fun showToast(@StringRes toastRes: Int) {
        toastStringOb.postValue(getContext().getString(toastRes))
    }

    /**
     * 展示Toast
     */
    open fun showToast(message: String?) {
        toastStringOb.postValue(message)
    }

    /**
     * 展示loading弹框
     */
    open fun showLoading() {
        loadingOb.postValue(true)
    }

    /**
     * 隐藏loading弹框
     */
    open fun dismissLoading() {
        loadingOb.postValue(false)
    }

    /**
     * 关闭当前Activity
     */
    open fun finish() {
        finishOb.postValue(true)
    }

    /**
     * 文本资源转文本
     */
    open fun getString(@StringRes stringRes: Int): String {
        return getContext().getString(stringRes)
    }

    /**
     * 双击事件
     */
    protected open fun onDoubleClick(v: View?) {

    }

    /**
     * 单击事件
     */
    protected open fun onSingleClick(v: View?) {

    }
}

