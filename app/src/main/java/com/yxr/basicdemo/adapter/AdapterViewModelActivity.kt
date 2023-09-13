package com.yxr.basicdemo.adapter

import com.yxr.base.activity.BaseStatusActivity
import com.yxr.basicdemo.R
import com.yxr.basicdemo.databinding.ActivityAdapterViewModelBinding

class AdapterViewModelActivity :
    BaseStatusActivity<ActivityAdapterViewModelBinding, AdapterViewModel>() {
    override val layoutId = R.layout.activity_adapter_view_model

    override fun createViewModel() = AdapterViewModel(this)

    override fun initData() {
        super.initData()

        showTitleBar(title = "AdapterViewModel")
    }
}