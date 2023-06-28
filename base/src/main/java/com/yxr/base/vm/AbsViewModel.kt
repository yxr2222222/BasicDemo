package com.yxr.base.vm

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.yxr.base.BaseApplication
import com.yxr.base.R
import com.yxr.base.model.PermissionReq
import com.yxr.base.widget.dialog.CancelConfirmDialog

open class AbsViewModel(val lifecycle: LifecycleOwner) : ViewModel(), DefaultLifecycleObserver {
    private val permissionReqOb = MutableLiveData<PermissionReq>()
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(owner: LifecycleOwner) {

    }

    override fun onStart(owner: LifecycleOwner) {

    }

    override fun onResume(owner: LifecycleOwner) {

    }

    override fun onPause(owner: LifecycleOwner) {

    }

    override fun onStop(owner: LifecycleOwner) {

    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        lifecycle.lifecycle.removeObserver(this)
        permissionLauncher = null
    }

    open fun init() {
        lifecycle.lifecycle.removeObserver(this)
        lifecycle.lifecycle.addObserver(this)
        initPermissionResultCaller()
    }

    open fun requestPermission(permissionReq: PermissionReq) {
        permissionReqOb.postValue(permissionReq)
    }

    open fun getContext(): Context {
        val context: Context? = when (lifecycle) {
            is Fragment -> {
                lifecycle.activity ?: BaseApplication.context
            }
            is Activity -> {
                lifecycle
            }
            else -> {
                BaseApplication.context
            }
        }
        return context ?: BaseApplication.context
    }

    fun getFragmentManager(): FragmentManager? {
        return when (val data = getLifecycleOwner()) {
            is FragmentActivity -> data.supportFragmentManager
            is Fragment -> data.childFragmentManager
            else -> null
        }
    }

    fun getLifecycleOwner() = lifecycle

    private fun initPermissionResultCaller() {
        if (lifecycle is ActivityResultCaller) {
            // 注册权限获取回调监听
            permissionLauncher =
                lifecycle.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap: Map<String?, Boolean?>? ->
                    if (resultMap != null) {
                        var isGranted = true
                        for (next in resultMap.entries) {
                            if (next.value == null || next.value == false) {
                                isGranted = false
                                break
                            }
                        }
                        permissionReqOb.value?.let { permissionReq ->
                            if (isGranted) {
                                permissionReq.onPermissionGranted()
                            } else {
                                permissionReq.onPermissionDenied()
                            }
                        }
                    }
                }

            // 监听权限申请
            permissionReqOb.observe(lifecycle) { permissionReq: PermissionReq? ->
                if (permissionReq != null) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        // 如果是6.0之前
                        permissionReq.onPermissionGranted()
                    } else {
                        // 获取需要申请的权限列表
                        val needRequestPermissions = arrayListOf<String>()
                        permissionReq.permissions?.forEach { permission ->
                            val result = ContextCompat.checkSelfPermission(
                                getContext(),
                                permission
                            )
                            if (PackageManager.PERMISSION_GRANTED != result) {
                                needRequestPermissions.add(permission)
                            }
                        }
                        if (needRequestPermissions.isEmpty()) {
                            // 如果都已经授权则直接回调已授权回调
                            permissionReq.onPermissionGranted()
                        } else {
                            // 展示申请权限弹框
                            showPermissionDialog(permissionReq, needRequestPermissions)
                        }
                    }
                }
            }
        }
    }

    /**
     * 展示权限申请的说明弹框
     *
     * @param permissionReq         权限申请的
     * @param needRequestPermission 需要申请的权限列表
     */
    private fun showPermissionDialog(
        permissionReq: PermissionReq,
        needRequestPermission: List<String>
    ) {
        val context = getContext()
        if (context is Activity) {
            val cancelConfirmDialog = CancelConfirmDialog(context)
                .setCancelText(context.getString(R.string.disagree))
                .setConfirmText(context.getString(R.string.agree))
                .setContent(permissionReq.desc)
                .setCancelConfirmListener(object :
                    CancelConfirmDialog.SimpleCancelConfirmListener() {
                    override fun onConfirm() {
                        permissionLauncher?.launch(needRequestPermission.toTypedArray())
                    }

                    override fun onCancel() {
                        permissionReq.onPermissionDenied()
                    }
                })
            cancelConfirmDialog.setCancelable(false)
            cancelConfirmDialog.setCanceledOnTouchOutside(false)
            cancelConfirmDialog.show()
        }
    }
}