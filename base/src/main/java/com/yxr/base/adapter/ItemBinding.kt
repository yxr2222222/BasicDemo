package com.yxr.base.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.entity.MultiItemEntity

abstract class ItemBinding constructor(
    val viewModelId: Int,
    @LayoutRes val layoutRes: Int
) : MultiItemEntity {
    override val itemType: Int
        get() = layoutRes
}