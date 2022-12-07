package com.yxr.base.model;

import com.yxr.base.listener.PermissionListener;

public abstract class PermissionReq implements PermissionListener {
    private final String desc;
    private final String[] permissions;

    public PermissionReq( String desc, String...permissions) {
        this.desc = desc;
        this.permissions = permissions;
    }

    public String getDesc() {
        return desc;
    }

    public String[] getPermissions() {
        return permissions;
    }
}
