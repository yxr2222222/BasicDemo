package com.yxr.base.http;

import android.text.TextUtils;

import com.yxr.base.http.callback.OnBaseUrlReplaceCallback;
import com.yxr.base.http.util.HttpUtil;
import com.yxr.base.util.MMKVUtil;
import com.yxr.base.util.PackageUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseUrlReplaceConfig {
    private static final String MMKV_CURR_BASE_DEBUG_URL = "CURR_BASE_DEBUG_URL";
    public static final String IGNORE_REPLACE_BASE_URL = "ignoreReplaceBaseUrl";
    /**
     * 正式环境的BaseUrl
     */
    private String formalBaseUrl;
    /**
     * 测试环境的BaseUrl
     */
    private String debugBaseUrl;
    /**
     * BaseUrl回调
     */
    private OnBaseUrlReplaceCallback onBaseUrlReplaceCallback;
    private final List<String> baseUrlList = new ArrayList<>();

    private BaseUrlReplaceConfig() {

    }

    public void setFormalBaseUrl(String formalBaseUrl) {
        this.formalBaseUrl = formalBaseUrl;
    }

    public String getFormalBaseUrl() {
        return formalBaseUrl;
    }

    public String getDebugBaseUrl() {
        return debugBaseUrl;
    }

    public OnBaseUrlReplaceCallback getOnBaseUrlReplaceCallback() {
        return onBaseUrlReplaceCallback;
    }

    public List<String> getBaseUrlList() {
        return baseUrlList;
    }

    private String currDebugBaseUrl = null;

    /**
     * 获取动态域名
     */
    public String getBaseUrl() {
        if (PackageUtil.isDebug()) {
            if (currDebugBaseUrl == null) {
                currDebugBaseUrl = MMKVUtil.getString(MMKV_CURR_BASE_DEBUG_URL, "");
            }
            if (TextUtils.isEmpty(currDebugBaseUrl)) {
                return getDebugBaseUrl();
            }
            return currDebugBaseUrl;
        }

        OnBaseUrlReplaceCallback callback = getOnBaseUrlReplaceCallback();
        return callback != null && HttpUtil.isUrl(callback.getCustomFormalBaseUrl())
                ? callback.getCustomFormalBaseUrl()
                : getFormalBaseUrl();
    }

    /**
     * 更新自定义的Debug网络地址
     */
    public void updateDebugUrl(String url) {
        currDebugBaseUrl = url;
        MMKVUtil.putString(MMKV_CURR_BASE_DEBUG_URL, currDebugBaseUrl);
    }

    public static class Builder {
        private final BaseUrlReplaceConfig replaceConfig = new BaseUrlReplaceConfig();

        public Builder formalBaseUrl(String formalBaseUrl) {
            replaceConfig.formalBaseUrl = formalBaseUrl;
            addToBaseUrlList(formalBaseUrl);
            return this;
        }

        public Builder debugBaseUrl(String debugBaseUrl) {
            replaceConfig.debugBaseUrl = debugBaseUrl;
            addToBaseUrlList(debugBaseUrl);
            return this;
        }

        public Builder otherBaseUrl(String otherBaseUrl) {
            addToBaseUrlList(otherBaseUrl);
            return this;
        }

        public Builder onBaseUrlReplaceCallback(OnBaseUrlReplaceCallback onBaseUrlReplaceCallback) {
            replaceConfig.onBaseUrlReplaceCallback = onBaseUrlReplaceCallback;
            return this;
        }

        public BaseUrlReplaceConfig build() {
            return replaceConfig;
        }

        private void addToBaseUrlList(String url) {
            if (HttpUtil.isUrl(url) && !replaceConfig.baseUrlList.contains(url)) {
                replaceConfig.baseUrlList.add(url);
            }
        }

    }
}