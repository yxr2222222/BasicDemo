package com.yxr.basicdemo

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yxr.base.adapter.BaseBindingItemAdapter
import com.yxr.base.vm.BaseAdapterViewModel

class MainVM(lifecycle: LifecycleOwner) : BaseAdapterViewModel<MainItemBinding>(lifecycle) {
    override val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(getContext())
    override val adapter =
        object : BaseBindingItemAdapter<MainItemBinding>(lifecycle, R.layout.item_main) {

        }
    override val viewModelId: Int = BR.viewModel
}