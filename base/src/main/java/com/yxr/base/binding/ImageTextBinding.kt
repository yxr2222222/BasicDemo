package com.yxr.base.binding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.yxr.base.widget.GifImageTextView
import com.yxr.base.widget.ImageTextView
import pl.droidsonroids.gif.GifDrawable

class ImageTextBinding {
    companion object {
        @BindingAdapter(
            "itvChecked",
            requireAll = false
        )
        @JvmStatic
        fun bindingImageText(
            view: ImageTextView,
            itvChecked: Boolean? = false,
        ) {
            view.setCheck(itvChecked == true)
        }

        @BindingAdapter(
            "itvTextBold",
            requireAll = false
        )
        @JvmStatic
        fun setTextBold(
            view: ImageTextView,
            itvTextBold: Boolean? = false,
        ) {
            view.setTextBold(itvTextBold == true)
        }

        @BindingAdapter(
            "itvNormalImgRes",
            "itvNormalTextRes",
            requireAll = false
        )
        @JvmStatic
        fun bindingImageTextNormal(
            view: ImageTextView,
            @DrawableRes itvNormalImgRes: Int? = null,
            @StringRes itvNormalTextRes: Int? = null
        ) {
            itvNormalImgRes?.run {
                view.setImageUncheckRes(this)
            }
            itvNormalTextRes?.run {
                if (this > 0) {
                    try {
                        view.setNormalText(view.resources.getString(itvNormalTextRes))
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }

        @BindingAdapter(
            "itvCheckedImgRes",
            "itvCheckedTextRes",
            requireAll = false
        )
        @JvmStatic
        fun bindingImageTextChecked(
            view: ImageTextView,
            @DrawableRes itvCheckedImgRes: Int? = null,
            @StringRes itvCheckedTextRes: Int? = null
        ) {
            itvCheckedImgRes?.run {
                view.setImageCheckRes(this)
            }
            itvCheckedTextRes?.run {
                if (this > 0) {
                    try {
                        view.setText(view.resources.getString(itvCheckedTextRes))
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }

        @BindingAdapter(
            "itvNormalText",
            requireAll = false
        )
        @JvmStatic
        fun bindingTextNormal(
            view: ImageTextView,
            itvNormalText: String? = null
        ) {
            view.setNormalText(itvNormalText)
        }

        @BindingAdapter(
            "itvText",
            requireAll = false
        )
        @JvmStatic
        fun bindingText(
            view: ImageTextView,
            itvText: String? = null
        ) {
            view.setText(itvText)
        }

        @BindingAdapter(
            "isImageTextEnabled",
            requireAll = false
        )
        @JvmStatic
        fun bindingImageTextEnabled(
            view: ImageTextView,
            isImageTextEnabled: Boolean = true
        ) {
            view.setImageTextEnabled(isImageTextEnabled)
        }

        @BindingAdapter(
            "itvNormalTextColor",
            requireAll = false
        )
        @JvmStatic
        fun bindingNormalTextColor(
            view: ImageTextView,
            itvNormalTextColor: Int?
        ) {
            itvNormalTextColor?.let {
                view.setTextColor(it)
            }
        }

        @BindingAdapter(
            "itvCheckedTextColor",
            requireAll = false
        )
        @JvmStatic
        fun bindingCheckedTextColor(
            view: ImageTextView,
            itvCheckedTextColor: Int?
        ) {
            itvCheckedTextColor?.let {
                view.setTextCheckColor(it)
            }
        }

        @BindingAdapter(
            "gifDrawableRes",
            "gifLoopCount",
            requireAll = false
        )
        @JvmStatic
        fun setGifDrawRes(
            view: GifImageTextView,
            @DrawableRes gifDrawableRes: Int?,
            loopCount: Int = 1
        ) {
            if (gifDrawableRes != null && gifDrawableRes != 0 && loopCount >= 0) {
                view.setGifDrawRes(gifDrawableRes, loopCount)
            }
        }

        @BindingAdapter(
            "isNeedGif",
            requireAll = false
        )
        @JvmStatic
        fun setIsNeedGif(
            view: GifImageTextView,
            isNeedGif: Boolean?
        ) {
            isNeedGif?.let {
                view.isNeedGif = isNeedGif
            }
        }

        @BindingAdapter(
            "gifState",
            requireAll = false
        )
        @JvmStatic
        fun setGifState(
            view: GifImageTextView,
            gifState: Boolean?
        ) {
            gifState?.let {
                if (gifState) {
                    view.startGif()
                } else {
                    view.stopGif()
                }
            }
        }

        @BindingAdapter(
            "isStopShowImage",
            requireAll = false
        )
        @JvmStatic
        fun setIsStopShowImage(
            view: GifImageTextView,
            isStopShowImage: Boolean
        ) {
            view.isStopShowImage = isStopShowImage
        }

        @BindingAdapter(
            "gifDrawable",
            requireAll = false
        )
        @JvmStatic
        fun setGifDrawable(
            view: GifImageTextView,
            gifDrawable: GifDrawable?
        ) {
            gifDrawable?.let {
                view.setGifDrawRes(it)
            }
        }

    }
}