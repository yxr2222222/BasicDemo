package com.yxr.base.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class BaseBindingViewHolder(view: View) : BaseViewHolder(view) {
    val dataBinding: ViewDataBinding? = DataBindingUtil.bind(view)
}