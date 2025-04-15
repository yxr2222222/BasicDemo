package com.yxr.base.helper

import com.yxr.base.R
import com.yxr.base.http.HttpErrorCode
import com.yxr.base.http.extension.launchRequest
import com.yxr.base.http.manager.HttpManager
import com.yxr.base.http.model.IResponse
import com.yxr.base.http.model.NetworkException
import com.yxr.base.http.util.NetworkExceptionUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import okhttp3.Dispatcher
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

class HttpHelper {
    private val mainScope = MainScope()

    /**
     * 创建RetrofitService，通过Service获取对应api
     */
    fun <T : Any> createApi(
        cls: Class<T>,
        dispatcher: Dispatcher? = null,
        factory: Converter.Factory? = null,
    ): T = HttpManager.get().createApi(cls, dispatcher, factory)

    /**
     * 快捷请求
     * @param block 需要进行的挂起方法（一般为网络请求之类的）
     * @param onSuccess block方法处理成功回调
     * @param onError block方法处理失败回调
     */
    fun <T> launchRequest(
        block: suspend () -> IResponse<T>?,
        onSuccess: (T?) -> Unit,
        onError: suspend (exception: NetworkException) -> Unit = {}
    ) {
        if (!mainScope.isActive) return

        mainScope.launchRequest(block, onSuccess = { data ->
            if (data == null) {
                if (mainScope.isActive) {
                    onError(
                        NetworkException(
                            HttpErrorCode.CODE_NULL_DATA,
                            NetworkExceptionUtil.getMessage(R.string.default_null_body_exception),
                        )
                    )
                }
            } else if (!data.isSuccess()) {
                if (mainScope.isActive) {
                    onError(
                        NetworkException(
                            HttpErrorCode.CODE_UNKNOWN,
                            data.error()
                                ?: NetworkExceptionUtil.getMessage(R.string.default_null_body_exception),
                        )
                    )
                }
            } else {
                if (mainScope.isActive) {
                    onSuccess(data.getData())
                }
            }
        }, onError = { exception ->
            if (mainScope.isActive) {
                onError(exception)
            }
        })
    }

    fun release() {
        try {
            if (mainScope.isActive) {
                mainScope.cancel()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}