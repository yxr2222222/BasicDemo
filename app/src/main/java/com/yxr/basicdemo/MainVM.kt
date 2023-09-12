package com.yxr.basicdemo

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yxr.base.adapter.BaseBindingItemAdapter
import com.yxr.base.http.manager.HttpManager
import com.yxr.base.updater.manager.UpdateManager
import com.yxr.base.util.MachineUtil
import com.yxr.base.util.PackageUtil
import com.yxr.base.vm.BaseAdapterViewModel

class MainVM(lifecycle: LifecycleOwner?) : BaseAdapterViewModel<MainItemBinding>(lifecycle) {
    override val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(getContext())
    override val adapter =
        object : BaseBindingItemAdapter<MainItemBinding>(lifecycle, R.layout.item_main) {

        }
    override val viewModelId: Int = BR.viewModel

    override fun onSingleClick(v: View?) {
        when(v?.id){
            R.id.btnCheckUpdate-> checkUpdate()
            R.id.btnBaseUrlEdit->HttpManager.get().startBaseUrlEditActivity(getContext())
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
            )
        )
    }
}