package com.yxr.base.binding

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.makeramen.roundedimageview.RoundedImageView
import com.yxr.base.widget.TitleBar

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
            "backgroundColor",
            requireAll = false
        )
        @JvmStatic
        fun bindingBackgroundColor(
            view: View,
            @ColorInt backgroundColor: Int?,
        ) {
            backgroundColor?.let {
                view.setBackgroundColor(it)
            }
        }

        @BindingAdapter(
            "bindingTextSize",
            requireAll = false
        )
        @JvmStatic
        fun bindingTextSize(
            view: TextView,
            bindingTextSize: Float?,
        ) {
            bindingTextSize?.let {
                view.textSize = bindingTextSize
            }
        }

        @BindingAdapter(
            "bindingBorderColor",
            requireAll = false
        )
        @JvmStatic
        fun bindingBorderColor(
            view: RoundedImageView,
            @ColorInt bindingBorderColor: Int?,
        ) {
            bindingBorderColor?.let {
                view.borderColor = it
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
            marginLeft: Int?,
            marginTop: Int?,
            marginRight: Int?,
            marginBottom: Int?,
        ) {
            (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let { params ->
                if (marginLeft != null) {
                    params.leftMargin = marginLeft
                }
                if (marginTop != null) {
                    params.topMargin = marginTop
                }
                if (marginRight != null) {
                    params.rightMargin = marginRight
                }
                if (marginBottom != null) {
                    params.bottomMargin = marginBottom
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
            bindingPaddingLeft: Int?,
            bindingPaddingTop: Int?,
            bindingPaddingRight: Int?,
            bindingPaddingBottom: Int?,
        ) {
            view.setPadding(
                bindingPaddingLeft ?: view.paddingLeft,
                bindingPaddingTop ?: view.paddingTop,
                bindingPaddingRight ?: view.paddingRight,
                bindingPaddingBottom ?: view.paddingBottom,
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

        @BindingAdapter(
            "titleBarTitle",
            requireAll = false
        )
        @JvmStatic
        fun bindingTitleBarTitle(
            view: TitleBar,
            titleBarTitle: CharSequence?
        ) {
            view.setTitle(titleBarTitle)
        }

        @BindingAdapter(
            "bindingProgress",
            "bindingMax",
            requireAll = false
        )
        @JvmStatic
        fun bindingProgressMax(
            view: ProgressBar,
            bindingProgress: Int?,
            bindingMax: Int?,
        ) {
            bindingMax?.let { view.max = bindingMax }
            bindingProgress?.let { view.progress = bindingProgress }
        }
    }
}