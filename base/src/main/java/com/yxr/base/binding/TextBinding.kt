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
        fun bindingGifResId(textView: TextView, textChangedListener: TextWatcher?) {
            textChangedListener?.let {
                textView.addTextChangedListener(it)
            }
        }
    }
}