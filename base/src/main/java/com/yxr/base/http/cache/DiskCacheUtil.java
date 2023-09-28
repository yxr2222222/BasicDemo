package com.yxr.base.http.cache;

import java.io.File;

import okhttp3.Cache;
import okhttp3.internal.cache.DiskLruCache;

public class DiskCacheUtil {
    public static DiskLruCache getDiskLruCache(File cacheDir, long maxSize) {
        return new Cache(cacheDir, maxSize).getCache$okhttp();
    }
}
