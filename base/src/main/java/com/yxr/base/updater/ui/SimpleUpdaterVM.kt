package com.yxr.base.updater.ui

import androidx.lifecycle.LifecycleOwner
import com.yxr.base.BR

open class SimpleUpdaterVM(lifecycle: LifecycleOwner?) : BaseUpdaterVM(lifecycle) {
    override val viewModelId = BR.viewModel
}