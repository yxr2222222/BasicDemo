package com.yxr.base.widget

import android.text.TextPaint
import android.text.style.ClickableSpan
import androidx.annotation.ColorInt

abstract class CustomClickableSpan(@ColorInt val textColor:Int) : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = textColor
        ds.isUnderlineText = false
    }
}