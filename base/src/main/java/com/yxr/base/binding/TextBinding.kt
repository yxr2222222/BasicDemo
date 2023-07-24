package com.yxr.base.binding

import android.text.TextWatcher
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import pl.droidsonroids.gif.GifImageView

class TextBinding {
    companion object {
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
    }
}