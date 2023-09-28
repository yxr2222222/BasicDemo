package com.yxr.basicdemo.model

import com.google.gson.annotations.SerializedName
import com.yxr.base.http.model.IResponse

class BaseResponse<T> : IResponse<T> {
    companion object {
        const val SUCCESS = 200

        fun <T> createFailedResp(message: String?): BaseResponse<T> {
            val resp = BaseResponse<T>()
            resp.code = -1
            resp.message = message
            return resp
        }

        fun <T> createSuccessResp(data: T?): BaseResponse<T> {
            val resp = BaseResponse<T>()
            resp.code = SUCCESS
            resp.result = data
            return resp
        }
    }

    var code: Int = -1
    var message: String? = null

    @SerializedName(value = "result", alternate = ["data"])
    var result: T? = null

    override fun isSuccess() = code == SUCCESS

    override fun code() = code

    override fun error() = message

    override fun getData() = result
}