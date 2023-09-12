package com.yxr.base.binding

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

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

        /**
         * 绑定tab监听
         */
        @BindingAdapter("bindingTabSelectedListener")
        @JvmStatic
        fun bindingTabSelectedListener(
            tabLayout: TabLayout,
            onTabSelectedListener: TabLayout.OnTabSelectedListener,
        ) {
            tabLayout.removeOnTabSelectedListener(onTabSelectedListener)
            tabLayout.addOnTabSelectedListener(onTabSelectedListener)
        }

        /**
         * 设置textView
         */
        @BindingAdapter(
            "isNeedDeleteLine",
            requireAll = false
        )
        @JvmStatic
        fun bindingTextView(
            textView: TextView,
            isNeedDeleteLine: Boolean?,
        ) {
            if (isNeedDeleteLine == true) {
                // 添加删除线
                textView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

        @BindingAdapter(
            "backgroundRes",
            requireAll = false
        )
        @JvmStatic
        fun bindingBackgroundRes(
            view: View,
            @DrawableRes backgroundRes: Int?,
        ) {
            backgroundRes?.let {
                view.setBackgroundResource(it)
            }
        }

        @BindingAdapter(
            "iconRes",
            requireAll = false
        )
        @JvmStatic
        fun bindingIconRes(
            view: ImageView,
            @DrawableRes iconRes: Int?,
        ) {
            iconRes?.let {
                view.setImageResource(it)
            }
        }

        @BindingAdapter(
            "viewPager2FragmentStateAdapter",
            requireAll = false
        )
        @JvmStatic
        fun bindingViewPager2Adapter(
            view: ViewPager2,
            adapter: FragmentStateAdapter?
        ) {
            try {
                view.adapter = adapter
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        /**
         * 绑定height
         */
        @JvmStatic
        @BindingAdapter(
            "bindingHeight",
            requireAll = false
        )
        fun bindingHeight(
            view: View,
            bindingHeight: Int?
        ) {
            bindingHeight?.let { height ->
                view.layoutParams?.let { layoutParams ->
                    layoutParams.height = height
                    view.layoutParams = layoutParams
                }
            }
        }

        /**
         * 绑定margin
         */
        @JvmStatic
        @BindingAdapter(
            "bindingMarginLeft",
            "bindingMarginTop",
            "bindingMarginRight",
            "bindingMarginBottom",
            requireAll = false
        )
        fun bindMargin(
            view: View,
            marginLeft: Float?,
            marginTop: Float?,
            marginRight: Float?,
            marginBottom: Float?,
        ) {
            (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let { params ->
                if (marginLeft != null) {
                    params.leftMargin = marginLeft.toInt()
                }
                if (marginTop != null) {
                    params.topMargin = marginTop.toInt()
                }
                if (marginRight != null) {
                    params.rightMargin = marginRight.toInt()
                }
                if (marginBottom != null) {
                    params.bottomMargin = marginBottom.toInt()
                }
                view.layoutParams = params
            }
        }

        /**
         * 绑定padding
         */

        @BindingAdapter(
            "bindingPaddingLeft",
            "bindingPaddingTop",
            "bindingPaddingRight",
            "bindingPaddingBottom",
            requireAll = false
        )
        @JvmStatic
        fun bindViewPadding(
            view: View,
            bindingPaddingLeft: Float?,
            bindingPaddingTop: Float?,
            bindingPaddingRight: Float?,
            bindingPaddingBottom: Float?,
        ) {
            view.setPadding(
                bindingPaddingLeft?.toInt() ?: view.paddingLeft,
                bindingPaddingTop?.toInt() ?: view.paddingTop,
                bindingPaddingRight?.toInt() ?: view.paddingRight,
                bindingPaddingBottom?.toInt() ?: view.paddingBottom,
            )
        }

        @BindingAdapter(
            "bindingMarginStart",
            "bindingMarginEnd",
            requireAll = false
        )
        @JvmStatic
        fun bindingMargin(
            view: View,
            startMargin: Int?,
            endMargin: Int?,
        ) {
            val layoutParams = view.layoutParams
            if (layoutParams is ViewGroup.MarginLayoutParams) {
                if (startMargin != null) {
                    layoutParams.marginStart = startMargin
                }
                if (endMargin != null) {
                    layoutParams.marginEnd = endMargin
                }
                view.layoutParams = layoutParams
            }
        }
    }
}