package com.yxr.base.listener;

public interface PermissionListener {
    /**
     * 权限申请成功
     */
    void onPermissionGranted();

    /**
     * 权限申请失败了
     *
     * @param isProhibit 权限是否被禁止了，如果被禁止了，需要主动处理
     */
    void onPermissionDenied(boolean isProhibit);
}
