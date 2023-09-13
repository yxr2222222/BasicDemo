package com.yxr.basicdemo.main

import com.yxr.base.activity.BaseActivity
import com.yxr.basicdemo.R
import com.yxr.basicdemo.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding, MainVM>() {
    override val layoutId = R.layout.activity_main
    override fun createViewModel() = MainVM(this)
}