package com.yxr.base.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

/**
 * @author ciba
 * @date 2020/11/18
 * @description SP持久化管理类
 */

public class SPManager {
    private static final String SP_NAME = "cn.yxr.base";
    @SuppressLint("StaticFieldLeak")
    private static SPManager instance;
    private Context context;

    private SPManager() {

    }

    public static SPManager getInstance() {
        if (instance == null) {
            synchronized (SPManager.class) {
                if (instance == null) {
                    instance = new SPManager();
                }
            }
        }
        return instance;
    }

    public void init(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    /****************************String******************************/
    public void putString(String key, String value) {
        putString(null, key, value);
    }

    public void putString(String fileName, String key, String value) {
        putObject(fileName, key, value);
    }

    public String getString(String key) {
        return getString(null, key);
    }

    public String getString(String fileName, String key) {
        return getString(fileName, key, "");
    }

    public String getString(String fileName, String key, String defaultValue) {
        return (String) getObj(fileName, key, defaultValue);
    }

    /****************************Long******************************/
    public void putLong(String key, long value) {
        putLong(null, key, value);
    }

    public void putLong(String fileName, String key, long value) {
        putObject(fileName, key, value);
    }

    public long getLong(String key) {
        return getLong(null, key);
    }

    public long getLong(String fileName, String key) {
        return getLong(fileName, key, 0L);
    }

    public long getLong(String fileName, String key, long defaultValue) {
        return (long) getObj(fileName, key, defaultValue);
    }

    /****************************Float******************************/
    public void putFloat(String key, float value) {
        putFloat(null, key, value);
    }

    public void putFloat(String fileName, String key, float value) {
        putObject(fileName, key, value);
    }

    public float getFloat(String key) {
        return getFloat(null, key);
    }

    public float getFloat(String fileName, String key) {
        return getFloat(fileName, key, 0.0f);
    }

    public float getFloat(String fileName, String key, float defaultValue) {
        return (float) getObj(fileName, key, defaultValue);
    }

    /****************************Boolean******************************/
    public void putBoolean(String key, boolean value) {
        putBoolean(null, key, value);
    }

    public void putBoolean(String fileName, String key, boolean value) {
        putObject(fileName, key, value);
    }

    public boolean getBoolean(String key) {
        return getBoolean(null, key);
    }

    public boolean getBoolean(String fileName, String key) {
        return getBoolean(fileName, key, false);
    }

    public boolean getBoolean(String fileName, String key, boolean defaultValue) {
        return (boolean) getObj(fileName, key, defaultValue);
    }

    /**
     * 根据key和value存储值
     *
     * @param fileName 操作的SP文件名称
     * @param key      key
     * @param value    根据不同类型的值使用不同的api存储
     */
    private void putObject(String fileName, String key, Object value) {
        try {
            SharedPreferences.Editor edit = getSP(fileName).edit();
            if (value instanceof String) {
                edit.putString(key, (String) value);
            } else if (value instanceof Float) {
                edit.putFloat(key, (float) value);
            } else if (value instanceof Long) {
                edit.putLong(key, (long) value);
            } else if (value instanceof Boolean) {
                edit.putBoolean(key, (boolean) value);
            }
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据defaultValue的类型获取值并转换成对应的类型
     *
     * @param fileName     文件名，用于获取SP对象
     * @param key          key
     * @param defaultValue 默认值
     * @return 获取到的值，没有获取到返回默认值
     */
    public Object getObj(String fileName, String key, Object defaultValue) {
        Object object = null;
        try {
            SharedPreferences sp = getSP(fileName);
            if (defaultValue instanceof String) {
                object = sp.getString(key, (String) defaultValue);
            } else if (defaultValue instanceof Float) {
                object = sp.getFloat(key, (float) defaultValue);
            } else if (defaultValue instanceof Long) {
                object = sp.getLong(key, (long) defaultValue);
            } else if (defaultValue instanceof Boolean) {
                object = sp.getBoolean(key, (boolean) defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public <T> T getData(@NonNull String key, @NonNull Class<T> cls, @Nullable T defaultData) {
        try {
            String json = getString(null, key);
            if (TextUtils.isEmpty(json)) {
                return defaultData;
            }
            T data = new Gson().fromJson(json, cls);
            return data == null ? defaultData : data;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return defaultData;
    }

    public <T> void putData(@NonNull String key, @NonNull T data) {
        try {
            String json = new Gson().toJson(data);
            putString(key, json);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SharedPreferences
     *
     * @param fileName 存储的文件名
     * @return SharedPreferences
     */
    private SharedPreferences getSP(String fileName) {
        if (fileName == null) {
            return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
}
