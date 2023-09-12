package com.yxr.base.model;

import androidx.annotation.Nullable;

import com.yxr.base.listener.PermissionListener;

public abstract class PermissionReq implements PermissionListener {
    /**
     * 权限申请描述，如果为空，则不会弹出权限申请描述弹框，会直接进行权限申请
     */
    @Nullable
    private final String desc;
    /**
     * 获取自动处理被禁止的权限的弹框描述，如果为空则交给用户自己处理，如果不为空且权限被禁止，则弹出弹框告诉用户要跳转设置界面手动开启
     */
    private final String[] permissions;
    @Nullable
    private final String permissionProhibitDesc;

    public PermissionReq(@Nullable String desc, String[] permissions) {
        this(desc, "当前申请的权限被永久拒绝或多次拒绝，需要您手动开启", permissions);
    }

    /**
     * @param desc                   权限申请描述，如果为空，则不会弹出权限申请描述弹框，会直接进行权限申请
     * @param permissionProhibitDesc 获取自动处理被禁止的权限的弹框描述，如果为空则交给用户自己处理，如果不为空且权限被禁止，则弹出弹框告诉用户要跳转设置界面手动开启
     * @param permissions            需要申请的权限们
     */
    public PermissionReq(@Nullable String desc, @Nullable String permissionProhibitDesc, String[] permissions) {
        this.desc = desc;
        this.permissionProhibitDesc = permissionProhibitDesc;
        this.permissions = permissions;
    }

    @Nullable
    public String getDesc() {
        return desc;
    }

    public String[] getPermissions() {
        return permissions;
    }

    @Nullable
    public String getPermissionProhibitDesc() {
        return permissionProhibitDesc;
    }
}
