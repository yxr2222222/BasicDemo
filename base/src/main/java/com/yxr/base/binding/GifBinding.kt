package com.yxr.base.binding

import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import pl.droidsonroids.gif.GifImageView

class GifBinding {
    companion object {
        @BindingAdapter(value = ["bindingGifResId"], requireAll = false)
        @JvmStatic
        fun bindingGifResId(gifImageView: GifImageView, @DrawableRes gifResId: Int?) {
            if (gifResId != null && gifResId > 0) {
                gifImageView.setImageResource(gifResId)
            }
        }
    }
}