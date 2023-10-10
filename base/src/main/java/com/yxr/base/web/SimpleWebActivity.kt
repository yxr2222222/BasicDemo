package com.yxr.base.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import com.yxr.base.R
import com.yxr.base.activity.BaseStatusActivity
import com.yxr.base.databinding.ActivitySimpleWebBinding
import com.yxr.base.util.PackageUtil
import com.yxr.base.widget.TitleBar

open class SimpleWebActivity : BaseStatusActivity<ActivitySimpleWebBinding, SimpleWebVM>() {

    companion object {
        private const val EXTRA_WEB_URL = "EXTRA_WEB_URL"
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_IS_NEED_TITLE_BAR = "EXTRA_IS_NEED_TITLE_BAR"

        fun start(
            context: Context,
            webUrl: String,
            title: String? = null,
            isNeedTitleBar: Boolean = true
        ) {
            val intent = Intent(context, SimpleWebActivity::class.java)
            intent.putExtra(EXTRA_WEB_URL, webUrl)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_IS_NEED_TITLE_BAR, isNeedTitleBar)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override val layoutId = R.layout.activity_simple_web
    override fun createViewModel() = SimpleWebVM(this)

    override fun getAssets(): AssetManager {
        return resources.assets
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
            return
        }
        super.onBackPressed()
    }

    override fun initListener() {
        super.initListener()
        viewModel.reloadOb.observe(this) {
            if (it == true) {
                binding.webView.reload()
            }
        }
    }

    override fun initData() {
        super.initData()

        if (!intent.getBooleanExtra(EXTRA_IS_NEED_TITLE_BAR, true)) {
            titleBar().visibility = View.GONE
        }

        // 初始化WebView
        initWebView()

        // 跳转监听
        viewModel.schemeJump.observe(this) { scheme ->
            PackageUtil.schemeJump(scheme)
        }

        // 初始化标题
        showTitleBar(intent.getStringExtra(EXTRA_TITLE))
        titleBar().addAction(object :
            TitleBar.TextAction(getString(R.string.close), 0xff292929.toInt()) {
            override fun performAction(view: View?) {
                finish()
            }
        })

        binding.webView.loadUrl(getWebUrl() ?: "")
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseWebView()
    }

    protected open fun getWebUrl(): String? = intent.getStringExtra(EXTRA_WEB_URL)

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        try {
            val settings: WebSettings = binding.webView.settings
            settings.javaScriptEnabled = true
            // 是否支持viewport属性，默认值 false
            // 页面通过`<meta name="viewport" ... />`自适应手机屏幕
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            // 是否自动加载图片
            settings.loadsImagesAutomatically = true
            // 开启 DOM storage API 功能
            settings.domStorageEnabled = true
            settings.defaultTextEncodingName = "UTF-8"
            settings.displayZoomControls = false
            settings.setSupportMultipleWindows(true)
            // android 5.0以上默认不支持Mixed Content
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            // 是否在离开屏幕时光栅化(会增加内存消耗)，默认值 false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                settings.offscreenPreRaster = false
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun releaseWebView() {
        try {
            val parent = binding.webView.parent as ViewGroup
            parent.removeAllViews()
            binding.webView.visibility = View.GONE
            binding.webView.stopLoading()
            binding.webView.webChromeClient = null
            binding.webView.clearView()
            binding.webView.removeAllViews()
            binding.webView.destroy()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}