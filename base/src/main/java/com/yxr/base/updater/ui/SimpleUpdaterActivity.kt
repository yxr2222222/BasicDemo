package com.yxr.base.updater.ui

import com.yxr.base.R
import com.yxr.base.databinding.ActivitySimpleUpdaterBinding

open class SimpleUpdaterActivity : BaseUpdaterActivity<ActivitySimpleUpdaterBinding, SimpleUpdaterVM>() {
    override val layoutId = R.layout.activity_simple_updater
    override fun createViewModel() = SimpleUpdaterVM(this)

    override fun fitsSystemWindows() = false

    override fun statusBarColor() = R.color.transparent
}