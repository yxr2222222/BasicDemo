package com.yxr.basicdemo.refershload

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.yxr.base.adapter.BaseBindingItemAdapter
import com.yxr.base.http.model.IResponse
import com.yxr.base.model.LoadingType
import com.yxr.base.vm.BasePageAdapterViewModel
import com.yxr.basicdemo.BR
import com.yxr.basicdemo.R
import com.yxr.basicdemo.api.TestApi
import com.yxr.basicdemo.model.BaseResponse
import com.yxr.basicdemo.model.PageTestData

class RefreshLoadPageAdapterVM(lifecycle: LifecycleOwner?) :
    BasePageAdapterViewModel<List<PageTestData>, RefreshLoadPageAdapterItemBinding>(lifecycle) {
    override val layoutManager = LinearLayoutManager(getContext())
    override val adapter = object : BaseBindingItemAdapter<RefreshLoadPageAdapterItemBinding>(
        lifecycle,
        R.layout.item_refresh_load_page
    ) {}
    override val viewModelId = BR.viewModel

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        refresh(isAutoRefresh = true)
    }

    override suspend fun loadDataBlock(loadingType: LoadingType): IResponse<List<PageTestData>>? {
        // 这是个模拟请求，实际上应该是自己的网络请求或构建数据的逻辑
        return getPageData()
//        return createApi(TestApi::class.java).getPageTestData(page, pageSize)
    }

    override fun onLoadSuccess(data: List<PageTestData>?): MutableList<RefreshLoadPageAdapterItemBinding>? {
        val itemList = arrayListOf<RefreshLoadPageAdapterItemBinding>()
        data?.forEach {
            itemList.add(RefreshLoadPageAdapterItemBinding(it))
        }
        return itemList
    }

    /**
     * 模拟网络请求
     */
    private fun getPageData(): BaseResponse<List<PageTestData>> {
        val dataList = arrayListOf<PageTestData>()
        for (index in 0 until pageSize) {
            dataList.add(PageTestData("Index : ${page * pageSize + index}"))
        }
        return BaseResponse.createSuccessResp(dataList)
    }
}