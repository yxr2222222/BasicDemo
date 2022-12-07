package com.yxr.base.http.model

interface IResponse {
    /**
     * 是否成功
     */
    fun isSuccess(): Boolean

    /**
     * 错误信息
     */
    fun error(): String?
}