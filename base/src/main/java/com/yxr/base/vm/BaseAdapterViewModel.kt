package com.yxr.base.vm

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnItemLongClickListener
import com.yxr.base.adapter.BaseBindingItemAdapter
import com.yxr.base.adapter.ItemBinding

/**
 *
 */
abstract class BaseAdapterViewModel<T : ItemBinding>(lifecycle: LifecycleOwner?) :
    BaseStatusViewModel(lifecycle), OnItemClickListener,
    OnItemChildClickListener, OnItemLongClickListener {
    abstract val layoutManager: RecyclerView.LayoutManager
    abstract val adapter: BaseBindingItemAdapter<T>

    val isEmptyData = MutableLiveData(true)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        // 如果item中的子控件ID集合不为空，则设置子控件点击事件
        getChildClickViewIds()?.let { childClickViewIds ->
            adapter.addChildClickViewIds(*childClickViewIds)
            adapter.setOnItemChildClickListener(this)
        }

        adapter.setOnItemClickListener(this)

        adapter.setOnItemLongClickListener(this)
    }

    final override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        this.adapter.getItemOrNull(position)?.let { item ->
            onItemClick(item, view, position)
        }
    }

    final override fun onItemChildClick(
        adapter: BaseQuickAdapter<*, *>,
        view: View,
        position: Int
    ) {
        this.adapter.getItemOrNull(position)?.let { item ->
            onItemChildClick(item, view, position)
        }
    }

    final override fun onItemLongClick(
        adapter: BaseQuickAdapter<*, *>,
        view: View,
        position: Int
    ): Boolean {
        this.adapter.getItemOrNull(position)?.let { item ->
            return onItemLongClick(item, view, position)
        }
        return true
    }

    /**
     * 获取item中需要响应点击事件的子控件ID集合
     */
    open fun getChildClickViewIds(): IntArray? {
        return null
    }

    open fun refreshData(dataList: MutableList<T>?) {
        refreshData(false, dataList)
    }

    open fun refreshData(isClear: Boolean, dataList: MutableList<T>?) {
        if (isClear) {
            adapter.setNewInstance(dataList)
        } else if (dataList != null) {
            adapter.addData(dataList)
        }
        isEmptyData.value = adapter.itemCount <= 0
    }

    open fun onItemClick(itemBinding: T, view: View, position: Int) {

    }

    open fun onItemChildClick(itemBinding: T, view: View, position: Int) {

    }

    open fun onItemLongClick(itemBinding: T, view: View, position: Int): Boolean {
        return true
    }
}