package com.yxr.base.binding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yxr.base.R

class GlideBinding {
    companion object {
        @BindingAdapter(
            value = ["glideImage", "glidePlaceholder", "glideOptions"],
            requireAll = false
        )
        @JvmStatic
        fun bindingGlide(
            imageView: ImageView,
            glideImage: Any?,
            @DrawableRes glidePlaceholder: Int?,
            glideOptions: RequestOptions?
        ) {
            val requestOptions = glideOptions ?: RequestOptions.placeholderOf(
                glidePlaceholder ?: R.color.white_eeeeee
            )
            Glide.with(imageView)
                .load(glideImage)
                .apply(requestOptions)
                .into(imageView)
        }
    }
}