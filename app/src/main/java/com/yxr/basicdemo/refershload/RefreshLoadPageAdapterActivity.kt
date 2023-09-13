package com.yxr.basicdemo.refershload

import com.yxr.base.activity.BaseStatusActivity
import com.yxr.basicdemo.R
import com.yxr.basicdemo.databinding.ActivityRefreshLoadPageBinding

class RefreshLoadPageAdapterActivity :
    BaseStatusActivity<ActivityRefreshLoadPageBinding, RefreshLoadPageAdapterVM>() {
    override val layoutId = R.layout.activity_refresh_load_page

    override fun createViewModel() = RefreshLoadPageAdapterVM(this)

    override fun initData() {
        super.initData()

        showTitleBar(title = "RefreshLoadPageAdapter")
    }
}