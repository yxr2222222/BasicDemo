package com.yxr.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.yxr.base.http.HttpConfig;
import com.yxr.base.http.manager.HttpManager;
import com.yxr.base.manager.SPManager;


/**
 * @author ciba
 * @description Application基类
 * @date 2020/9/17
 */
public abstract class BaseApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        SPManager.getInstance().init(this);
        HttpManager.get().init(getHttpConfig());
    }

    /**
     * 获取网络配置信息
     *
     * @return 网络配置信息
     */
    @NonNull
    protected abstract HttpConfig getHttpConfig();
}
