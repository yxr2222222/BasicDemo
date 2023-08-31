package com.yxr.base.vm

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.yxr.base.BaseApplication
import com.yxr.base.R
import com.yxr.base.extension.startSimpleActivity
import com.yxr.base.helper.PermissionPageHelper
import com.yxr.base.model.PermissionReq
import com.yxr.base.widget.dialog.CancelConfirmDialog

open class AbsViewModel(var lifecycle: LifecycleOwner?) : ViewModel(), DefaultLifecycleObserver {
    private val permissionReqOb = MutableLiveData<PermissionReq>()
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null
    private var permissionDialog: CancelConfirmDialog? = null
    private val permissionPageHelper = PermissionPageHelper(getContext())

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
        dismissPermissionDialog()
        super.onDestroy(owner)
        lifecycle?.lifecycle?.removeObserver(this)
        lifecycle = null
        permissionLauncher = null
    }

    open fun init() {
        lifecycle?.lifecycle?.removeObserver(this)
        lifecycle?.lifecycle?.addObserver(this)
        initPermissionResultCaller()
    }

    open fun requestPermission(permissionReq: PermissionReq) {
        permissionReqOb.postValue(permissionReq)
    }

    fun getContext(): Context = when (val data = getLifecycleOwner()) {
        is Fragment -> data.activity ?: BaseApplication.context
        is Activity -> data
        else -> BaseApplication.context
    }

    fun startSimpleActivity(
        cls: Class<out Activity>,
        bundleMap: HashMap<String, Any?>? = null
    ) {
        getContext().startSimpleActivity(cls, bundleMap)
    }

    fun getFragmentManager(): FragmentManager? {
        return when (val data = getLifecycleOwner()) {
            is FragmentActivity -> data.supportFragmentManager
            is Fragment -> data.childFragmentManager
            else -> null
        }
    }

    fun getLifecycleOwner(): LifecycleOwner? = lifecycle

    fun needRequestPermissions(permissions: Array<String>?): ArrayList<String> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // 如果是6.0之前
            return arrayListOf()
        } else {
            // 获取需要申请的权限列表
            val needRequestPermissions = arrayListOf<String>()
            permissions?.forEach { permission ->
                val result = ContextCompat.checkSelfPermission(
                    getContext(),
                    permission
                )
                if (PackageManager.PERMISSION_GRANTED != result) {
                    needRequestPermissions.add(permission)
                }
            }
            return needRequestPermissions
        }
    }

    private fun initPermissionResultCaller() {
        val lifecycle = getLifecycleOwner()
        if (lifecycle is ActivityResultCaller) {
            // 注册权限获取回调监听
            permissionLauncher =
                lifecycle.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap: Map<String?, Boolean?>? ->
                    if (resultMap != null) {
                        // 是否同意了权限
                        var isGranted = true
                        // 是否禁止了权限，比如说拒绝多次或者拒绝之后永不提醒
                        var isProhibit = false

                        val context = getContext()
                        val isNeedCheck = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

                        for (next in resultMap.entries) {
                            val key = next.key
                            if (next.value == null || next.value == false) {
                                isGranted = false
                                if (isNeedCheck && context is Activity && key != null) {
                                    if (!shouldShowRequestPermissionRationale(context, key)) {
                                        // 用户拒绝权限并且系统不再弹出请求权限的弹窗
                                        isProhibit = true
                                    }
                                }
                                break
                            }
                        }
                        permissionReqOb.value?.let { permissionReq ->
                            if (isGranted) {
                                permissionReq.onPermissionGranted()
                            } else if (isProhibit && !permissionReq.permissionProhibitDesc.isNullOrBlank()) {
                                showPermissionDialog(
                                    permissionReq.permissionProhibitDesc,
                                    object : CancelConfirmDialog.CancelConfirmListener {
                                        override fun onCancel() {
                                            permissionReq.onPermissionDenied(isProhibit)
                                        }

                                        override fun onConfirm() {
                                            permissionReq.onPermissionDenied(isProhibit)
                                            permissionPageHelper.jumpPermissionPage()
                                        }
                                    })
                            } else {
                                permissionReq.onPermissionDenied(isProhibit)
                            }
                        }
                    }
                }

            // 监听权限申请
            permissionReqOb.observe(lifecycle) { permissionReq: PermissionReq? ->
                if (permissionReq != null) {
                    val needRequestPermissions = needRequestPermissions(permissionReq.permissions)
                    if (needRequestPermissions.isEmpty()) {
                        // 如果都已经授权则直接回调已授权回调
                        permissionReq.onPermissionGranted()
                    } else {
                        if (permissionReq.desc.isNullOrBlank()) {
                            permissionLauncher?.launch(needRequestPermissions.toTypedArray())
                        } else {
                            // 展示申请权限弹框
                            showPermissionDialog(permissionReq.desc, object :
                                CancelConfirmDialog.SimpleCancelConfirmListener() {
                                override fun onConfirm() {
                                    permissionLauncher?.launch(needRequestPermissions.toTypedArray())
                                }

                                override fun onCancel() {
                                    permissionReq.onPermissionDenied(false)
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    /**
     * 展示权限申请的说明弹框
     */
    private fun showPermissionDialog(
        desc: String?,
        listener: CancelConfirmDialog.CancelConfirmListener
    ) {
        val context = getContext()
        if (context is Activity) {
            dismissPermissionDialog()
            permissionDialog = CancelConfirmDialog(context)
                .setCancelText(context.getString(R.string.disagree))
                .setConfirmText(context.getString(R.string.agree))
                .setContent(desc)
                .setCancelConfirmListener(listener)
            permissionDialog?.setCancelable(false)
            permissionDialog?.setCanceledOnTouchOutside(false)
            permissionDialog?.show()
        }
    }

    private fun dismissPermissionDialog() {
        permissionDialog?.dismiss()
        permissionDialog = null
    }
}