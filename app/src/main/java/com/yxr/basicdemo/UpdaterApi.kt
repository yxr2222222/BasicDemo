package com.yxr.basicdemo

import com.yxr.basicdemo.model.BaseResponse
import com.yxr.basicdemo.model.CstUpdateChecker
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UpdaterApi {
    @GET("/appUpdate/getAppUpdate")
    fun checkUpdate(
        @Query("machine") machine: String?,
        @Query("version") version: String?,
        @Query("packageName") packageName: String?,
        @Query("type") type: Int = 0,
    ): Call<BaseResponse<CstUpdateChecker>>
}