package com.yxr.base.http.download

import com.yxr.base.updater.api.BaseUpdaterApi
import com.yxr.base.util.FileUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit


class DownloadUtil {
    companion object {
        private var retrofit: Retrofit? = null

        private fun createRetrofit(): Retrofit {
            if (retrofit == null) {
                val builder = OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)

                retrofit = Retrofit.Builder()
                    .baseUrl("http://www.baidu.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(builder.build())
                    .build()
            }

            return retrofit!!
        }

        @JvmStatic
        fun downloadFile(
            coroutineScope: CoroutineScope,
            downloadUrl: String,
            targetFile: File,
            deleteExists: Boolean = false,
            listener: OnDownloadListener? = null
        ) {
            if (downloadUrl.isBlank()) {
                listener?.onDownloadFailed(downloadUrl, "下载地址不能为空")
                return
            }
            if (!deleteExists && targetFile.exists()) {
                listener?.onDownloadSuccess(downloadUrl, targetFile)
                return
            }

            if (deleteExists && targetFile.exists()) {
                targetFile.delete()
            }

            val job = createRetrofit()
                .create(BaseUpdaterApi::class.java)
                .downloadApp(downloadUrl)

            listener?.onDownloadStart(downloadUrl, job)

            job.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val successful = response.isSuccessful
                    if (!successful) {
                        onFailure(call, java.lang.RuntimeException("Not successful!"))
                        return
                    }
                    val body = response.body()
                    if (body == null) {
                        onFailure(call, java.lang.RuntimeException("Body is empty!"))
                        return
                    }
                    coroutineScope.launch(Dispatchers.IO) {
                        val contentLength = body.contentLength()
                        val byteStream = body.byteStream()
                        var os: OutputStream? = null
                        var currLength = 0L
                        try {
                            os = FileOutputStream(targetFile)
                            var len = 0
                            val buffer = ByteArray(1024)
                            while (len != -1) {
                                len = byteStream.read(buffer)
                                if (len != -1) {
                                    os.write(buffer, 0, len)
                                    currLength += len
                                    coroutineScope.launch(Dispatchers.Main) {
                                        listener?.onDownloadProgress(
                                            downloadUrl,
                                            currLength,
                                            contentLength
                                        )
                                    }
                                }
                            }

                            coroutineScope.launch(Dispatchers.Main) {
                                listener?.onDownloadSuccess(downloadUrl, targetFile)
                            }
                        } catch (e: Throwable) {
                            e.printStackTrace()
                            onFailure(call, e)
                        } finally {
                            FileUtil.closeQuietly(byteStream, os)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                    try {
                        targetFile.delete()
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                    coroutineScope.launch(Dispatchers.Main) {
                        listener?.onDownloadFailed(downloadUrl, throwable.message)
                    }
                }
            })
        }
    }
}