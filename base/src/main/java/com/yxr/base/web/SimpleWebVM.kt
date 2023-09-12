package com.yxr.base.web

import android.graphics.Bitmap
import android.webkit.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.yxr.base.BR
import com.yxr.base.R
import com.yxr.base.vm.BaseStatusViewModel
import java.util.regex.Pattern

class SimpleWebVM(lifecycle: LifecycleOwner?) : BaseStatusViewModel(lifecycle) {
    override val viewModelId = BR.viewModel
    val webViewClient = MutableLiveData<WebViewClient>()
    val webChromeClient = MutableLiveData<WebChromeClient>()
    val schemeJump = MutableLiveData<String>()

    init {
        webViewClient.value = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                launchWithMain {
                    showLoadingStatus(message = getString(R.string.loading))
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                launchWithMain {
                    showContentStatus()
                }
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                url: String?,
            ): WebResourceResponse? {
                if (schemeMatch(url)) {
                    schemeJump.postValue(url)
                }
                return super.shouldInterceptRequest(view, url)
            }
        }
    }

    private fun schemeMatch(url: String?): Boolean {
        if (url == null) {
            return false;
        }
        try {
            val p = Pattern.compile("^(?!http)[0-9a-zA-Z]{2,}:\\/\\/[\\d\\D]*")
            //通过模式对象创建一个匹配对象
            val m1 = p.matcher(url)
            //尝试将整个区域与模式匹配。当且仅当整个区域序列匹配此匹配器的模式时才返回 true。
            return m1.matches()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}