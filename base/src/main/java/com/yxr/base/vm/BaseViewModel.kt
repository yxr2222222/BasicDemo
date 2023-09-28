package com.yxr.base.vm

import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yxr.base.R
import com.yxr.base.http.HttpErrorCode
import com.yxr.base.http.download.DownloadUtil
import com.yxr.base.http.download.OnDownloadListener
import com.yxr.base.http.extension.launchRequest
import com.yxr.base.http.manager.HttpManager
import com.yxr.base.http.model.CstServiceException
import com.yxr.base.http.model.IResponse
import com.yxr.base.http.model.NetworkException
import com.yxr.base.http.util.NetworkExceptionUtil
import com.yxr.base.model.LoadingOb
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

/**
 * @author ciba
 * @description ViewModel基类
 * @date 2020/9/17
 */
abstract class BaseViewModel(lifecycle: LifecycleOwner?) : AbsViewModel(lifecycle),
    View.OnClickListener {
    companion object {
        private const val CLICK_INTERVAL_TIME = 250L
    }

    abstract val viewModelId: Int

    val toastStringOb = MutableLiveData<String>()
    val loadingOb = MutableLiveData<LoadingOb>()
    val finishOb = MutableLiveData<Boolean>()
    val dismissFragmentOb = MutableLiveData<Boolean>()

    private val downloadJobMap = hashMapOf<String, Call<ResponseBody>>()
    private var preClickTime = 0L
    private var preClickView: Int? = null

    open fun onBackPressed() = true

    override fun onDestroy(owner: LifecycleOwner) {
        downloadJobMap.forEach { entry ->
            cancelDownloadJob(
                downloadUrl = entry.key,
                removeFromMap = false,
                call = entry.value
            )
        }
        downloadJobMap.clear()
        super.onDestroy(owner)
    }

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
    open fun <T : Any> createApi(
        cls: Class<T>,
        dispatcher: Dispatcher? = null
    ): T = HttpManager.get().createApi(cls, dispatcher)

    /**
     * 快捷请求
     * @param block 需要进行的挂起方法（一般为网络请求之类的）
     * @param onSuccess block方法处理成功回调
     * @param onError block方法处理失败回调
     * @param isNeedLoading 是否需要展示loading弹框
     * @param isShowError 是否在失败时自动展示toast简要信息
     * @param isShowErrorDetail 失败时toast的是否为详细信息
     */
    open fun <T> launchRequest(
        block: suspend () -> IResponse<T>?,
        onSuccess: (T?) -> Unit,
        onError: suspend (exception: NetworkException) -> Unit = {},
        isNeedLoading: Boolean = true,
        loadingText: String? = null,
        isShowError: Boolean = false,
        isShowErrorDetail: Boolean = false,
    ) {
        if (isNeedLoading) {
            showLoading(loadingText)
        }
        var isErrorCallback = false
        viewModelScope.launchRequest(block, onSuccess = { data ->
            if (isNeedLoading) {
                dismissLoading()
            }
            if (data == null) {
                val error = NetworkExceptionUtil.getMessage(R.string.default_null_body_exception)
                if (isShowError || isShowErrorDetail) {
                    showToast(error)
                }
                if (!isErrorCallback) {
                    isErrorCallback = true
                    onError(CstServiceException(HttpErrorCode.CODE_NULL_DATA, error))
                }
            } else if (!data.isSuccess()) {
                val error = data.error()
                    ?: NetworkExceptionUtil.getMessage(R.string.default_null_body_exception)
                if (isShowError || isShowErrorDetail) {
                    showToast(error)
                }
                if (!isErrorCallback) {
                    isErrorCallback = true
                    onError(CstServiceException(data.code() ?: HttpErrorCode.CODE_UNKNOWN, error))
                }

                val httpConfig = HttpManager.get().httpConfig
                httpConfig.globalErrorCodeList.forEach { code ->
                    if (data.code() == code) {
                        httpConfig.configCallback?.onGlobalError(code)
                    }
                }
            } else {
                onSuccess(data.getData())
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
            if (!isErrorCallback) {
                isErrorCallback = true
                onError(exception)
            }
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
    open fun showLoading(loadingText: String? = null) {
        loadingOb.postValue(LoadingOb(isShowLoading = true, loadingText))
    }

    /**
     * 隐藏loading弹框
     */
    open fun dismissLoading() {
        loadingOb.postValue(LoadingOb(isShowLoading = false))
    }

    open fun dismissFragment() {
        dismissFragmentOb.postValue(true)
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
     * 下载文件
     * @param downloadUrl 需要下载的地址
     * @param targetFile 需要存储到的文件
     * @param deleteExists 如果文件存在了，是否需要删除重新下载
     * @param listener 下载监听
     */
    open fun download(
        downloadUrl: String,
        targetFile: File,
        deleteExists: Boolean = false,
        listener: OnDownloadListener? = null
    ) {
        val call = downloadJobMap[downloadUrl]
        if (call != null) return
        DownloadUtil.downloadFile(
            viewModelScope,
            downloadUrl,
            targetFile,
            deleteExists,
            object : OnDownloadListener {
                override fun onDownloadStart(downloadUrl: String, call: Call<ResponseBody>) {
                    downloadJobMap[downloadUrl] = call
                    listener?.onDownloadStart(downloadUrl, call)
                }

                override fun onDownloadProgress(downloadUrl: String, progress: Long, total: Long) {
                    listener?.onDownloadProgress(downloadUrl, progress, total)
                }

                override fun onDownloadSuccess(downloadUrl: String, file: File) {
                    cancelDownloadJob(downloadUrl)
                    listener?.onDownloadSuccess(downloadUrl, file)
                }

                override fun onDownloadFailed(downloadUrl: String, error: String?) {
                    cancelDownloadJob(downloadUrl)
                    listener?.onDownloadFailed(downloadUrl, error)
                }
            })
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

    protected open fun cancelDownloadJob(
        downloadUrl: String,
        removeFromMap: Boolean = true,
        call: Call<ResponseBody>? = null
    ) {
        val downloadJob = call ?: downloadJobMap[downloadUrl]
        downloadJob?.let { downloadAppJob ->
            if (!downloadAppJob.isCanceled && downloadAppJob.isExecuted) {
                try {
                    downloadAppJob.cancel()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
        if (removeFromMap) {
            downloadJobMap.remove(downloadUrl)
        }
    }
}

