package com.yxr.basicdemo.adapter

import com.yxr.base.BR
import com.yxr.base.adapter.ItemBinding
import com.yxr.basicdemo.R

/**
 * 第一个参数是第二个layout中的viewModel的id
 * 第二个参数是item布局
 */
class AdapterItemBinding2(val item: String) : ItemBinding(BR.itemAdapter2, R.layout.item_adapter2) {
}