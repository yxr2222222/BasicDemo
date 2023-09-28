package com.yxr.basicdemo.api

import com.yxr.base.http.cache.CacheMode
import com.yxr.base.http.cache.CacheStrategy
import com.yxr.basicdemo.model.BaseResponse
import com.yxr.basicdemo.model.PageTestData
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TestApi {
    /**
     * 这个是个假的请求
     * 需要缓存可以如下添加对应的header
     */
    @GET("/app/pageTestData")
    @Headers(
        "${CacheStrategy.CACHE_MODE}: ${CacheMode.READ_CACHE_NETWORK_PUT}",
        "${CacheStrategy.CACHE_TIME}: ${CacheStrategy.DAY}"
    )
    suspend fun getPageTestData(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 0
    ): BaseResponse<List<PageTestData>>
}