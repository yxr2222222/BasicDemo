package com.yxr.base.http.callback;

import androidx.annotation.Nullable;

public interface OnBaseUrlReplaceCallback {
    /**
     * 获取自定义的正式环境的BaseUrl
     *
     * @return 返回空的或者不是网络地址将使用@{@link com.yxr.base.http.BaseUrlReplaceConfig}的formalBaseUrl
     */
    @Nullable
    String getCustomFormalBaseUrl();

    /**
     * BaseUrl被替换
     */
    void onBaseUrlReplace(String oldHost, String newHost);

}