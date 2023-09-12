package com.yxr.base.binding

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import pl.droidsonroids.gif.GifImageView

class GifBinding {
    companion object {
        @SuppressLint("ResourceType")
        @BindingAdapter(value = ["bindingGifResId"], requireAll = false)
        @JvmStatic
        fun bindingGifResId(gifImageView: GifImageView, @DrawableRes gifResId: Int?) {
            if (gifResId != null && gifResId >= 0) {
                gifImageView.setImageResource(gifResId)
            }
        }
    }
}