package com.yxr.base.http.model

class CstServiceException(code: Int, message: String?, detail: String? = null) :
    NetworkException(code, message, detail) {
}