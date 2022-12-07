package com.yxr.base.http.extension

import android.content.Context
import android.net.ConnectivityManager
import com.yxr.base.http.model.NetworkException
import com.yxr.base.http.util.NetworkExceptionUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun Context.isNetworkConnected(): Boolean {
    try {
        val mConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable
        }
    } catch (e:Throwable){
        e.printStackTrace()
    }
    return false
}

fun <T> CoroutineScope.launchRequest(
    block: suspend () -> T?,
    onSuccess: suspend (T?) -> Unit,
    onError: suspend (exception: NetworkException) -> Unit = {}
) {
    launch(CoroutineExceptionHandler { _, throwable ->
        launch(Dispatchers.Main) {
            val exception = NetworkExceptionUtil.getNetworkException(throwable)
            throwable.printStackTrace()
            onError(exception)
        }
    }) {
        launch(Dispatchers.IO) {
            var data: T? = null
            try {
                data = block()
            } finally {
                launch(Dispatchers.Main) {
                    onSuccess(data)
                }
            }
        }
    }
}
