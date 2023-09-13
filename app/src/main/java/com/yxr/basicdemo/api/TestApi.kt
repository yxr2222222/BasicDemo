package com.yxr.basicdemo.api

import com.yxr.basicdemo.model.BaseResponse
import com.yxr.basicdemo.model.PageTestData
import retrofit2.http.GET
import retrofit2.http.Query

interface TestApi {
    /**
     * 这个是个假的请求
     */
    @GET("/app/pageTestData")
    suspend fun getPageTestData(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 0
    ): BaseResponse<List<PageTestData>>
}