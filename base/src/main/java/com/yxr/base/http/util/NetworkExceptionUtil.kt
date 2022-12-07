package com.yxr.base.http.util

import android.accounts.NetworkErrorException
import androidx.annotation.StringRes
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.yxr.base.BaseApplication
import com.yxr.base.R
import com.yxr.base.http.HttpErrorCode
import com.yxr.base.http.model.NetworkException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkExceptionUtil {
    companion object {
        @JvmStatic
        fun getNetworkException(
            e: Throwable?,
            elseCode: Int? = null,
            elseError: String? = null
        ): NetworkException =
            when (e) {
                is HttpException -> {
                    NetworkException(
                        e.code(),
                        getMessage(R.string.default_http_exception),
                        e.message
                    )
                }
                is SocketTimeoutException -> {
                    NetworkException(
                        HttpErrorCode.CODE_TIMEOUT,
                        getMessage(R.string.default_http_timeout),
                        e.message
                    )
                }
                is UnknownHostException -> {
                    NetworkException(
                        HttpErrorCode.CODE_UNKNOWN_HOST,
                        getMessage(R.string.default_unknown_host),
                        e.message
                    )
                }
                is NetworkErrorException -> {
                    NetworkException(
                        HttpErrorCode.CODE_NETWORK_EXCEPTION,
                        getMessage(R.string.default_network_exception),
                        e.message
                    )
                }
                is ConnectException -> {
                    NetworkException(
                        HttpErrorCode.CODE_CONNECT_EXCEPTION,
                        getMessage(R.string.default_connect_exception),
                        e.message
                    )
                }
                is JsonIOException -> {
                    NetworkException(
                        HttpErrorCode.CODE_DATA_PARSE_EXCEPTION,
                        getMessage(R.string.code_data_parse_exception),
                        e.message
                    )
                }
                is JsonParseException -> {
                    NetworkException(
                        HttpErrorCode.CODE_DATA_PARSE_EXCEPTION,
                        getMessage(R.string.code_data_parse_exception),
                        e.message
                    )
                }
                is IOException->{
                    NetworkException(
                        HttpErrorCode.CODE_IO_EXCEPTION,
                        getMessage(R.string.code_io_exception),
                        e.message
                    )
                }
                else -> {
                    NetworkException(
                        elseCode ?: HttpErrorCode.CODE_UNKNOWN,
                        elseError ?: getMessage(R.string.default_unknown_exception),
                        e?.message
                    )
                }
            }

        @JvmStatic
        fun getMessage(@StringRes stringRes: Int) =
            BaseApplication.context.getString(stringRes)
    }
}