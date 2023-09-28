package com.yxr.base.http.model

open class NetworkException(val code: Int, val message: String?, val detail: String? = null) {
}