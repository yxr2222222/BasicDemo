package com.yxr.base.adapter

import androidx.lifecycle.LifecycleOwner
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter

open class BaseBindingItemAdapter<T : ItemBinding>(
    val lifecycleOwner: LifecycleOwner,
    vararg layoutResIds: Int
) : BaseMultiItemQuickAdapter<T, BaseBindingViewHolder>() {

    init {
        for (layoutResId in layoutResIds) {
            addItemType(layoutResId, layoutResId)
        }
    }

    override fun convert(holder: BaseBindingViewHolder, item: T) {
        val dataBinding = holder.dataBinding
        if (dataBinding != null) {
            dataBinding.lifecycleOwner = lifecycleOwner
            dataBinding.setVariable(item.viewModelId, item)
            dataBinding.executePendingBindings()
        }
    }
}