package com.yxr.base.updater.ui

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yxr.base.R
import com.yxr.base.extension.getSuffix
import com.yxr.base.extension.resFormat
import com.yxr.base.http.manager.HttpManager
import com.yxr.base.updater.UpdateChecker
import com.yxr.base.updater.UpdateType
import com.yxr.base.updater.api.BaseUpdaterApi
import com.yxr.base.util.FileUtil
import com.yxr.base.util.MD5Util
import com.yxr.base.util.PackageUtil
import com.yxr.base.util.PathUtil
import com.yxr.base.vm.BaseViewModel
import com.yxr.base.web.SimpleWebActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.RuntimeException

abstract class BaseUpdaterVM(lifecycle: LifecycleOwner?) : BaseViewModel(lifecycle) {
    private val appDir = PathUtil.getDir("updater")
    private var downloadAppJob: Call<ResponseBody>? = null
    val updateChecker = MutableLiveData<UpdateChecker>()
    val updateBtnText = MutableLiveData<String>()
    val notNowBtnText = MutableLiveData<String>()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        updateChecker.observe(owner) { uc ->
            if (uc != null) {
                updateBtnText.value = if (uc.updateNowBtnTxt().isNullOrBlank()) {
                    getString(R.string.update_now)
                } else {
                    uc.updateNowBtnTxt()
                }

                notNowBtnText.value = if (uc.notNowBtnTxt().isNullOrBlank()) {
                    getString(R.string.update_not_now)
                } else {
                    uc.notNowBtnTxt()
                }

                if (UpdateType.DOWNLOAD == uc.updateType()) {
                    val fileName = MD5Util.md5(uc.schemeUrl()) + uc.schemeUrl().getSuffix()
                    val appFile = File(appDir, fileName)
                    if (appFile.exists()) {
                        updateBtnText.value = getString(R.string.install_now)
                    }
                }
            }
        }
        updateBtnText.value = getString(R.string.update_now)
        notNowBtnText.value = getString(R.string.update_not_now)
    }

    override fun onBackPressed(): Boolean {
        return updateChecker.value?.isForceUpdate() == true
    }

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.tvUpdate -> update()
            R.id.tvNotNow -> finish()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        releaseDownloadJob()
        super.onDestroy(owner)
    }

    /**
     * 立即更新
     */
    open fun update() {
        updateChecker.value?.let { uc ->
            if (UpdateType.DOWNLOAD == uc.updateType()) {
                checkDownloadNewVersionApp(uc.schemeUrl())
            } else if (UpdateType.APP_STORE == uc.updateType()) {
                PackageUtil.schemeJump(uc.schemeUrl())
            } else if (UpdateType.WEB == uc.updateType()) {
                SimpleWebActivity.start(
                    context = getContext(),
                    webUrl = uc.schemeUrl(),
                    title = uc.title()
                )
            } else {
                // TODO: 暂时不用处理
            }
        } ?: finish()
    }

    /**
     * 检查是否需要下载新版本APP
     */
    private fun checkDownloadNewVersionApp(schemeUrl: String) {
        if (schemeUrl.startsWith("http")) {
            val fileName = MD5Util.md5(schemeUrl) + schemeUrl.getSuffix()
            val appFile = File(appDir, fileName)
            if (appFile.exists()) {
                PackageUtil.installApp(appFile.path)
            } else {
                downloadNewVersionApp(schemeUrl, appFile)
            }
        } else {
            finish()
        }
    }

    /**
     * 下载新版本APP
     */
    private fun downloadNewVersionApp(schemeUrl: String, appFile: File) {
        if (downloadAppJob != null) return

        onDownloadStart()

        downloadAppJob = createApi(BaseUpdaterApi::class.java).downloadApp(schemeUrl)
        downloadAppJob?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()
                if (body == null) {
                    onFailure(call, RuntimeException("Body is empty!"))
                    return
                }
                viewModelScope.launch(Dispatchers.IO) {
                    val byteStream = body.byteStream()
                    val contentLength = body.contentLength()
                    var os: OutputStream? = null
                    var currLength = 0L
                    try {
                        os = FileOutputStream(appFile)
                        var len = 0
                        val buffer = ByteArray(1024)
                        while (len != -1) {
                            len = byteStream.read(buffer)
                            if (len != -1) {
                                os.write(buffer, 0, len)
                                currLength += len
                                launchWithMain {
                                    onDownloadProgress(currLength * 100 / contentLength)
                                }
                            }
                        }

                        launchWithMain { onDownloadSuccess(appFile) }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        onFailure(call, e)
                    } finally {
                        FileUtil.closeQuietly(byteStream, os)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                try {
                    appFile.delete()
                } catch (e:Throwable){
                    e.printStackTrace()
                }
                launchWithMain { onDownloadFailed(t.message) }
            }
        })
    }

    /**
     * 开始下载
     */
    protected open fun onDownloadStart() {

    }

    /**
     * 下载中
     * @param progress 下载进度，百分制
     */
    protected open fun onDownloadProgress(progress: Long) {
        updateBtnText.value = R.string.downloading_percent_format.resFormat(
            getContext(),
            "$progress%"
        )
    }

    /**
     * 下载成功
     * @param appFile 下载成功的APP文件
     */
    protected open fun onDownloadSuccess(appFile: File) {
        updateBtnText.value = getString(R.string.install_now)
        PackageUtil.installApp(appFile.path)
        releaseDownloadJob()
    }

    /**
     * 下载失败
     * @param error 错误信息
     */
    protected open fun onDownloadFailed(error: String?) {
        updateBtnText.value = getString(R.string.update_now)
        releaseDownloadJob()
    }

    /**
     * 释放下载任务
     */
    private fun releaseDownloadJob() {
        downloadAppJob?.let { downloadAppJob ->
            if (!downloadAppJob.isCanceled && downloadAppJob.isExecuted) {
                try {
                    downloadAppJob.cancel()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
        downloadAppJob = null
    }

}