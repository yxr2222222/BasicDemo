package com.yxr.base.http.model

interface IResponse<T> {
    /**
     * 是否成功
     */
    fun isSuccess(): Boolean

    fun code(): Int?

    /**
     * 错误信息
     */
    fun error(): String?

    fun getData(): T?
}