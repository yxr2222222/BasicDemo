package com.yxr.base.updater.manager

import com.yxr.base.BaseApplication
import com.yxr.base.extension.startSimpleActivity
import com.yxr.base.http.manager.HttpManager
import com.yxr.base.http.model.IResponse
import com.yxr.base.updater.api.BaseUpdaterApi
import com.yxr.base.updater.listener.OnUpdateCheckListener
import com.yxr.base.updater.UpdateChecker
import com.yxr.base.updater.ui.BaseUpdaterActivity
import com.yxr.base.updater.ui.SimpleUpdaterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateManager private constructor() {
    companion object {
        private const val TAG = "UpdateManager"

        @JvmField
        val instance = UpdateManager()
    }

    /**
     * 检查是否有更新
     * @param call 自己APP的检查更新接口请求
     * @param listener APP更新检查回调，如果不传回调默认内部处理有更新逻辑
     */
    fun <UC : UpdateChecker, T : IResponse<UC>> checkUpdate(
        call: Call<T>,
        listener: OnUpdateCheckListener? = null
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                val updateChecker = response.body()?.getData()
                if (updateChecker == null) {
                    listener?.onFailed(response.message() ?: "获取应用更新信息失败")
                } else {
                    listener?.onSuccess(updateChecker) ?: run {
                        BaseApplication.context.startSimpleActivity(
                            SimpleUpdaterActivity::class.java,
                            hashMapOf(BaseUpdaterActivity.EXTRA_UPDATE_CHECKER to updateChecker)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                listener?.onFailed(t.message)
            }
        })
    }
}