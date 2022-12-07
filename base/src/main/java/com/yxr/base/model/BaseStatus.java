package com.yxr.base.model;

import com.yxr.base.widget.status.UIStatus;

public class BaseStatus {
    private UIStatus status;
    private String message;
    private String retryText;

    public BaseStatus(UIStatus status) {
        this.status = status;
    }

    public BaseStatus(UIStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseStatus(UIStatus status, String message, String retryText) {
        this.status = status;
        this.message = message;
        this.retryText = retryText;
    }

    public UIStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getRetryText() {
        return retryText;
    }
}
