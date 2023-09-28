package com.yxr.basicdemo.status

import com.yxr.base.activity.BaseStatusActivity
import com.yxr.basicdemo.R
import com.yxr.basicdemo.databinding.ActivityStatusDemoBinding

class StatusDemoActivity : BaseStatusActivity<ActivityStatusDemoBinding, StatusDemoVM>() {
    override val layoutId = R.layout.activity_status_demo

    override fun createViewModel() = StatusDemoVM(this)

    override fun initData() {
        super.initData()

        showTitleBar(title = "多状态页面(Activity/Fragment/DialogFragment)")
    }

}