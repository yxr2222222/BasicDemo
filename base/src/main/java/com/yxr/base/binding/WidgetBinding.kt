package com.yxr.base.binding

import android.view.View
import androidx.databinding.BindingAdapter

class WidgetBinding {
    companion object {
        @BindingAdapter(
            "isSelected",
            requireAll = false
        )
        @JvmStatic
        fun bindingSelected(
            view: View,
            isSelected: Boolean?,
        ) {
            view.isSelected = isSelected == true
        }
    }
}