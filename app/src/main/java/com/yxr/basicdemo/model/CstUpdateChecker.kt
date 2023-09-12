package com.yxr.basicdemo.model

import com.yxr.base.updater.UpdateChecker
import com.yxr.base.updater.UpdateType
import java.io.Serializable

class CstUpdateChecker : UpdateChecker(), Serializable {
    private val title: String? = null
    private val message: String? = null
    private val image: String? = null
    private val version: String? = null
    private val updateType: UpdateType = UpdateType.DOWNLOAD
    private val schemeUrl: String = ""
    private val isForceUpdate: Int? = null

    override fun isNeedUpdate() = true

    override fun title(): String? = title

    override fun message(): String? = message

    override fun image(): String? = image

    override fun version(): String? = version

    override fun notNowBtnTxt(): String? = null

    override fun updateNowBtnTxt(): String? = null

    override fun updateType(): UpdateType = updateType

    override fun schemeUrl(): String = schemeUrl

    override fun isForceUpdate() = isForceUpdate == 1
}