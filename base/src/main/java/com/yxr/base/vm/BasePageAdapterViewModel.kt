package com.yxr.base.vm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yxr.base.adapter.ItemBinding
import com.yxr.base.http.model.IResponse
import com.yxr.base.http.model.NetworkException
import com.yxr.base.model.LoadingType

abstract class BasePageAdapterViewModel<E : IResponse, T : ItemBinding>(lifecycle: LifecycleOwner) :
    BaseAdapterViewModel<T>(lifecycle), OnRefreshLoadMoreListener {

    /**
     * 加载状态，主要用于RefreshLayout状态更新，配合RecyclerBinding使用风味更佳
     */
    val loadType = MutableLiveData(LoadingType.IDLE)

    /**
     * 是否还有更多数据，主要用于RefreshLayout状态更新，配合RecyclerBinding使用风味更佳
     */
    val hasMore = MutableLiveData(false)

    /**
     * 获取当前页码
     */
    var page = 0
        private set

    /**
     * 获取每页加载数据量
     */
    open var pageSize = 10

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadMore(true)
    }

    override fun refreshData(dataList: MutableList<T>?) {
        val value = loadType.value
        refreshData(
            isClear = LoadingType.REFRESH == value || LoadingType.AUTO_REFRESH == value,
            dataList
        )
    }

    override fun refreshData(isClear: Boolean, dataList: MutableList<T>?) {
        super.refreshData(isClear, dataList)
        hasMore.postValue(dataList != null && dataList.size > 0)
        page++
        loadFinish()
    }

    /**
     * 加载新数据
     *
     * @param isAutoRefresh 是否触发刷新的UI
     */
    open fun refresh(isAutoRefresh: Boolean = false) {
        if (isNotLoading()) {
            page = 0
            loadType.postValue(if (isAutoRefresh) LoadingType.AUTO_REFRESH else LoadingType.REFRESH)
            loadData()
        }
    }

    /**
     * 加载更多
     */
    open fun loadMore() {
        if (isNotLoading()) {
            loadType.postValue(LoadingType.LOAD_MORE)
            loadData()
        }
    }

    /**
     * 加载更多
     *
     * @param isAutoLoadMore 是否触发加载更多的UI
     */
    open fun loadMore(isAutoLoadMore: Boolean) {
        if (isNotLoading()) {
            loadType.postValue(if (isAutoLoadMore) LoadingType.AUTO_LOAD_MORE else LoadingType.LOAD_MORE)
            loadData()
        }
    }

    /**
     * 是否没有在加载数据
     */
    open fun isNotLoading(): Boolean {
        val value = loadType.value
        return value == null || LoadingType.IDLE == value
    }

    /**
     * 加载数据
     */
    open fun loadData() {
        launchRequest(block = {
            loadDataBlock()
        }, onSuccess = { data ->
            val itemList = onLoadSuccess(data)
            refreshData(itemList)
        }, onError = { exception ->
            onLoadError(exception)
        }, isNeedLoading = false)
    }

    /**
     * 加载失败处理
     */
    open fun onLoadError(exception: NetworkException) {
        loadFinish()
    }

    /**
     * 加载结束处理状态
     */
    open fun loadFinish() {
        loadType.postValue(LoadingType.IDLE)
    }

    /**
     * 加载数据的代码块，由子类实现
     */
    abstract suspend fun loadDataBlock(): E?

    /**
     * 加载成功，由子类实现，处理好返回结果后返回，会自动填充到adapter中
     * @param data loadDataBlock()获取的数据
     */
    abstract fun onLoadSuccess(data: E?): MutableList<T>?
}