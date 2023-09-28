package com.yxr.base.http.download

import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

interface OnDownloadListener {
    fun onDownloadStart(downloadUrl: String, call: Call<ResponseBody>)

    fun onDownloadProgress(downloadUrl: String, progress: Long, total: Long)

    fun onDownloadSuccess(downloadUrl: String, file: File)

    fun onDownloadFailed(downloadUrl: String, error: String?)
}