package com.yxr.basicdemo.model

import com.google.gson.annotations.SerializedName
import com.yxr.base.http.model.IResponse

class BaseResponse<T> : IResponse<T> {
    var code: Int = -1
    var message: String? = null
    @SerializedName(value = "result", alternate = ["data"])
    var result: T? = null

    override fun isSuccess() = code == 200

    override fun code() = code

    override fun error() = message

    override fun getData() = result
}