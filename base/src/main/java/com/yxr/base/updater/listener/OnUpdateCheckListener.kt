package com.yxr.base.updater.listener

import com.yxr.base.updater.UpdateChecker

interface OnUpdateCheckListener {
    fun onSuccess(updateChecker: UpdateChecker)
    fun onFailed(error: String?)
}