package com.yxr.base.vm

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.yxr.base.R
import com.yxr.base.adapter.BaseBindingItemAdapter
import com.yxr.base.adapter.ItemBinding

/**
 *
 */
abstract class BaseAdapterViewModel<T : ItemBinding>(lifecycle: LifecycleOwner) :
    BaseStatusViewModel(lifecycle), OnItemClickListener,
    OnItemChildClickListener {
    abstract val layoutManager: RecyclerView.LayoutManager
    abstract val adapter: BaseBindingItemAdapter<T>

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        // 如果item中的子控件ID集合不为空，则设置子控件点击事件
        getChildClickViewIds()?.let { childClickViewIds ->
            adapter.addChildClickViewIds(*childClickViewIds)
            adapter.setOnItemChildClickListener(this)
        }

        adapter.setOnItemClickListener(this)
    }

    /**
     * 获取item中需要响应点击事件的子控件ID集合
     */
    open fun getChildClickViewIds(): IntArray? {
        return null
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        this.adapter.getItemOrNull(position)?.let { item ->
            onItemClick(item, view, position)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        this.adapter.getItemOrNull(position)?.let { item ->
            onItemChildClick(item, view, position)
        }
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
    }

    open fun onItemClick(itemBinding: T, view: View, position: Int) {

    }

    open fun onItemChildClick(itemBinding: T, view: View, position: Int) {

    }
}