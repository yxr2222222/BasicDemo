package com.yxr.base.vm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.yxr.base.model.BaseStatus
import com.yxr.base.widget.status.UIStatus

abstract class BaseStatusViewModel(lifecycle: LifecycleOwner?) : BaseViewModel(lifecycle) {
    var status = MutableLiveData<BaseStatus?>()

    open fun reloadData() {

    }

    open fun showLoadingStatus(message: String? = null) {
        showStatus(BaseStatus(UIStatus.LOADING, message))
    }

    open fun showContentStatus() {
        showStatus(BaseStatus(UIStatus.CONTENT))
    }

    open fun showError(message: String? = null, retryText: String? = null) {
        showStatus(BaseStatus(UIStatus.ERROR, message, retryText))
    }

    open fun showStatus(status: BaseStatus?) {
        this.status.value = status
    }
}