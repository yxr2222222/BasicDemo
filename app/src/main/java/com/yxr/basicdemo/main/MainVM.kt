package com.yxr.basicdemo.main

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.yxr.base.http.manager.HttpManager
import com.yxr.base.updater.manager.UpdateManager
import com.yxr.base.util.MachineUtil
import com.yxr.base.util.PackageUtil
import com.yxr.base.vm.BaseViewModel
import com.yxr.basicdemo.BR
import com.yxr.basicdemo.R
import com.yxr.basicdemo.api.UpdaterApi
import com.yxr.basicdemo.adapter.AdapterViewModelActivity
import com.yxr.basicdemo.adapter.MultAdapterViewModelActivity
import com.yxr.basicdemo.refershload.RefreshLoadPageAdapterActivity
import com.yxr.basicdemo.status.StatusDemoActivity

class MainVM(lifecycle: LifecycleOwner?) : BaseViewModel(lifecycle) {
    override val viewModelId = BR.viewModel

    override fun onSingleClick(v: View?) {
        when (v?.id) {
            R.id.btnCheckUpdate -> checkUpdate()
            R.id.btnBaseUrlEdit -> HttpManager.get().startBaseUrlEditActivity(getContext())
            R.id.btnAdapterViewModel -> startSimpleActivity(AdapterViewModelActivity::class.java)
            R.id.btnMultAdapterViewModel -> startSimpleActivity(MultAdapterViewModelActivity::class.java)
            R.id.btnPageAdapterViewModel -> startSimpleActivity(RefreshLoadPageAdapterActivity::class.java)
            R.id.btnStatus -> startSimpleActivity(StatusDemoActivity::class.java)
        }
    }

    /**
     * 检查更新，如果需要自定义做检查结果，可以传入监听器
     */
    private fun checkUpdate() {
        UpdateManager.instance.checkUpdate(
            createApi(UpdaterApi::class.java).checkUpdate(
                machine = MachineUtil.getDeviceId(),
                version = PackageUtil.getVersionName(),
                packageName = PackageUtil.getPackageName()
            ), listener = null
        )
    }
}