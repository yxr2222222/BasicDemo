package com.yxr.base.updater.ui

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.yxr.base.R
import com.yxr.base.extension.getSuffix
import com.yxr.base.extension.resFormat
import com.yxr.base.http.download.OnDownloadListener
import com.yxr.base.updater.UpdateChecker
import com.yxr.base.updater.UpdateType
import com.yxr.base.util.MD5Util
import com.yxr.base.util.PackageUtil
import com.yxr.base.util.PathUtil
import com.yxr.base.vm.BaseViewModel
import com.yxr.base.web.SimpleWebActivity
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File
import kotlin.math.max

abstract class BaseUpdaterVM(lifecycle: LifecycleOwner?) : BaseViewModel(lifecycle) {
    private val appDir = PathUtil.getDir("updater")
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
        download(schemeUrl, appFile, deleteExists = false, listener = object : OnDownloadListener {
            override fun onDownloadStart(downloadUrl: String, call: Call<ResponseBody>) {
                onDownloadStart()
            }

            override fun onDownloadProgress(downloadUrl: String, progress: Long, total: Long) {
                onDownloadProgress(progress * 100 / max(1, total))
            }

            override fun onDownloadSuccess(downloadUrl: String, file: File) {
                onDownloadSuccess(file)
            }

            override fun onDownloadFailed(downloadUrl: String, error: String?) {
                onDownloadFailed(error)
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
    }

    /**
     * 下载失败
     * @param error 错误信息
     */
    protected open fun onDownloadFailed(error: String?) {
        updateBtnText.value = getString(R.string.update_now)
        showToast(getString(R.string.download_failed) + ": $error")
    }
}