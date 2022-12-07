package com.yxr.base.binding

import androidx.databinding.BindingAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class ViewPagerBinding {
    companion object {
        @BindingAdapter(value = ["viewPagerFragmentAdapter"], requireAll = false)
        @JvmStatic
        fun bindingViewPagerFragmentAdapter(viewPager: ViewPager2, adapter: FragmentStateAdapter?) {
            if (adapter != null) {
                viewPager.adapter = adapter
            }
        }
    }
}