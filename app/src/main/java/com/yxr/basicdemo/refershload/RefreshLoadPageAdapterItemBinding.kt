package com.yxr.basicdemo.refershload

import com.yxr.base.adapter.ItemBinding
import com.yxr.basicdemo.BR
import com.yxr.basicdemo.R
import com.yxr.basicdemo.model.PageTestData

class RefreshLoadPageAdapterItemBinding(val item: PageTestData) :
    ItemBinding(BR.viewModel, R.layout.item_refresh_load_page) {
}