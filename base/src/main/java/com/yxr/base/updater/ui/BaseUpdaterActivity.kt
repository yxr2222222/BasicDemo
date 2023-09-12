package com.yxr.base.updater.ui

import androidx.databinding.ViewDataBinding
import com.yxr.base.activity.BaseActivity
import com.yxr.base.updater.UpdateChecker

abstract class BaseUpdaterActivity<T : ViewDataBinding, VM : BaseUpdaterVM> :
    BaseActivity<T, VM>() {
    companion object {
        const val EXTRA_UPDATE_CHECKER = "EXTRA_UPDATE_CHECKER"
    }

    override fun initData() {
        super.initData()
        viewModel.updateChecker.value =
            intent.getSerializableExtra(EXTRA_UPDATE_CHECKER) as UpdateChecker?
    }
}