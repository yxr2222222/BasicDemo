package com.yxr.base.binding

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextWatcher
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import pl.droidsonroids.gif.GifImageView

class TextBinding {
    companion object {
        @BindingAdapter(value = ["lineSpacing"], requireAll = false)
        @JvmStatic
        fun bindingLineSpacing(textView: TextView, lineSpacing: Int?) {
            lineSpacing?.let {
                textView.setLineSpacing(lineSpacing.toFloat(), 1f)
            }
        }

        @BindingAdapter(value = ["textChangedListener"], requireAll = false)
        @JvmStatic
        fun bindingTextChangedListener(textView: TextView, textChangedListener: TextWatcher?) {
            textChangedListener?.let {
                textView.addTextChangedListener(it)
            }
        }

        @BindingAdapter(value = ["textRes"], requireAll = false)
        @JvmStatic
        fun bindingTextResId(textView: TextView, textRes: Int?) {
            if (textRes != null && textRes > 0) {
                try {
                    textView.setText(textRes)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }

        @BindingAdapter(
            value = ["textLeftDrawableRes", "textTopDrawableRes", "textRightDrawableRes", "textBottomDrawableRes", "textDrawableWidth", "textDrawableHeight"],
            requireAll = false
        )
        @JvmStatic
        fun bindingTextDrawable(
            textView: TextView,
            textLeftDrawableRes: Int? = null,
            textTopDrawableRes: Int? = null,
            textRightDrawableRes: Int? = null,
            textBottomDrawableRes: Int? = null,
            textDrawableWidth: Int? = null,
            textDrawableHeight: Int? = null
        ) {
            val context = textView.context
            val leftDrawable = getTextDrawable(
                context,
                textLeftDrawableRes,
                textDrawableWidth,
                textDrawableHeight
            )
            val topDrawable = getTextDrawable(
                context,
                textTopDrawableRes,
                textDrawableWidth,
                textDrawableHeight
            )
            val rightDrawable = getTextDrawable(
                context,
                textRightDrawableRes,
                textDrawableWidth,
                textDrawableHeight
            )
            val bottomDrawable = getTextDrawable(
                context,
                textBottomDrawableRes,
                textDrawableWidth,
                textDrawableHeight
            )
            textView.setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, bottomDrawable)
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun getTextDrawable(
            context: Context,
            drawableRes: Int?,
            width: Int?,
            height: Int?
        ): Drawable? {
            return if (width == null || height == null || drawableRes == null || drawableRes <= 0) {
                null
            } else {
                try {
                    val drawable = context.resources.getDrawable(drawableRes)
                    drawable.setBounds(0, 0, width, height)
                    drawable
                } catch (e: Throwable) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }
}