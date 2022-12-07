package com.yxr.base.adapter

import androidx.annotation.LayoutRes
import androidx.room.Ignore
import com.chad.library.adapter.base.entity.MultiItemEntity

abstract class ItemBinding @Ignore constructor(
    @Ignore val viewModelId: Int,
    @Ignore @LayoutRes val layoutRes: Int
) : MultiItemEntity {
    override val itemType: Int
        get() = layoutRes
}