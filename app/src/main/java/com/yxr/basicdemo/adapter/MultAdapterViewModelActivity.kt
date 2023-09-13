package com.yxr.basicdemo.adapter

import com.yxr.base.activity.BaseStatusActivity
import com.yxr.basicdemo.R
import com.yxr.basicdemo.databinding.ActivityMultAdapterViewModelBinding

class MultAdapterViewModelActivity :
    BaseStatusActivity<ActivityMultAdapterViewModelBinding, MultAdapterViewModel>() {
    override val layoutId = R.layout.activity_mult_adapter_view_model

    override fun createViewModel() = MultAdapterViewModel(this)

    override fun initData() {
        super.initData()

        showTitleBar(title = "MultAdapterViewModel")
    }
}