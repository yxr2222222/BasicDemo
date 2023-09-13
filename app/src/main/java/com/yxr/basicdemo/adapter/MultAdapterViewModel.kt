package com.yxr.basicdemo.adapter

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.yxr.base.adapter.BaseBindingItemAdapter
import com.yxr.base.adapter.ItemBinding
import com.yxr.base.vm.BaseAdapterViewModel
import com.yxr.basicdemo.BR
import com.yxr.basicdemo.R

class MultAdapterViewModel(lifecycle: LifecycleOwner?) :
    BaseAdapterViewModel<ItemBinding>(lifecycle) {
    override val layoutManager = LinearLayoutManager(getContext())

    // 第二个参数是需要使用到的ItemBinding内包含的所有layout
    override val adapter = object : BaseBindingItemAdapter<ItemBinding>(
        lifecycle,
        R.layout.item_adapter,
        R.layout.item_adapter2
    ) {}
    override val viewModelId = BR.viewModel

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        val itemList = arrayListOf<ItemBinding>()
        for (index in 0 until 20) {
            if (index % 2 == 0) {
                itemList.add(AdapterItemBinding("Index $index"))
            } else {
                itemList.add(AdapterItemBinding2("Index $index"))
            }
        }
        refreshData(isClear = true, itemList)
    }

    override fun onItemClick(itemBinding: ItemBinding, view: View, position: Int) {
        when(itemBinding){
            is AdapterItemBinding-> showToast("AdapterItemBinding ${itemBinding.item}")
            is AdapterItemBinding2-> showToast("AdapterItemBinding2 ${itemBinding.item}")
        }
    }
}