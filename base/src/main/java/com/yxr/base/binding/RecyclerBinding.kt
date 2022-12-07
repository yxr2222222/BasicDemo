package com.yxr.base.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yxr.base.adapter.BaseBindingItemAdapter
import com.yxr.base.adapter.ItemBinding
import com.yxr.base.model.LoadingType

class RecyclerBinding {
    companion object {
        @BindingAdapter(value = ["recyclerAdapter"], requireAll = false)
        @JvmStatic
        fun <T : ItemBinding> bindingRecyclerAdapter(
            recyclerView: RecyclerView,
            adapter: BaseBindingItemAdapter<T>?
        ) {
            if (adapter != null) {
                recyclerView.adapter = adapter
            }
        }

        @BindingAdapter(value = ["recyclerLayoutManager"], requireAll = false)
        @JvmStatic
        fun bindingLayoutManager(
            recyclerView: RecyclerView,
            layoutManager: RecyclerView.LayoutManager?
        ) {
            if (layoutManager != null) {
                recyclerView.layoutManager = layoutManager
            }
        }

        @BindingAdapter(
            value = ["loadType", "hasMore", "refreshLoadMoreListener"],
            requireAll = false
        )
        @JvmStatic
        fun bindingRefreshingLoading(
            refreshLayout: SmartRefreshLayout,
            loadType: LoadingType?,
            hasMore: Boolean?,
            refreshLoadMoreListener: OnRefreshLoadMoreListener?
        ) {
            if (loadType == null || LoadingType.IDLE == loadType) {
                refreshLayout.finishRefresh()
                refreshLayout.finishLoadMore()
            } else if (LoadingType.AUTO_REFRESH == loadType) {
                refreshLayout.autoRefresh()
            } else if (LoadingType.AUTO_LOAD_MORE == loadType) {
                refreshLayout.autoLoadMore()
            }
            if (hasMore != null) {
                refreshLayout.setNoMoreData(!hasMore)
            }
            if (refreshLoadMoreListener != null) {
                refreshLayout.setOnRefreshLoadMoreListener(refreshLoadMoreListener)
            }
        }
    }
}