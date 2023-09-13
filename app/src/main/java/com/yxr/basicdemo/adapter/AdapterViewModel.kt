package com.yxr.basicdemo.adapter

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.yxr.base.adapter.BaseBindingItemAdapter
import com.yxr.base.vm.BaseAdapterViewModel
import com.yxr.basicdemo.BR
import com.yxr.basicdemo.R

class AdapterViewModel(lifecycle: LifecycleOwner?) :
    BaseAdapterViewModel<AdapterItemBinding>(lifecycle) {
    override val layoutManager = LinearLayoutManager(getContext())

    // 第二个参数是需要使用到的ItemBinding内包含的所有layout
    override val adapter =
        object : BaseBindingItemAdapter<AdapterItemBinding>(lifecycle, R.layout.item_adapter) {}
    override val viewModelId = BR.viewModel

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        val itemList = arrayListOf<AdapterItemBinding>()
        for (index in 0 until 20) {
            itemList.add(AdapterItemBinding("Index $index"))
        }
        refreshData(isClear = true, itemList)
    }

    override fun onItemClick(itemBinding: AdapterItemBinding, view: View, position: Int) {
        showToast(itemBinding.item)
    }
}