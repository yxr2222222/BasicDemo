package com.yxr.basicdemo.status

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.yxr.basicdemo.BR
import com.yxr.base.vm.BaseStatusViewModel
import com.yxr.basicdemo.model.BaseResponse

class StatusDemoVM(lifecycle: LifecycleOwner?) : BaseStatusViewModel(lifecycle) {
    override val viewModelId = BR.viewModel
    val responseText = MutableLiveData<String>()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        getTestData(isReload = false)
    }

    override fun reloadData() {
        getTestData(isReload = true)
    }

    private fun getTestData(isReload: Boolean) {
        // 展示加载状态UI
        showLoadingStatus("内容加载中...")

        // 请求内容, isNeedLoading标记未false，这个是用来展示弹框loading的
        launchRequest({ innerGetTestData(isReload) }, onSuccess = {
            responseText.value = it
            // 展示内容控件
            showContentStatus()
        }, onError = {
            // 展示加载错误重试控件
            showError(it.message)
        }, isNeedLoading = false)
    }

    /**
     * 模拟请求数据
     */
    private fun innerGetTestData(reload: Boolean): BaseResponse<String> {
        Thread.sleep(2000)
        return if (reload) {
            BaseResponse.createSuccessResp(data = "这是一个测试文案")
        } else {
            BaseResponse.createFailedResp(message = "内容获取失败，请点击按钮重试")
        }
    }
}