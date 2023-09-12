package com.yxr.base.extension

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.view.View
import androidx.annotation.ColorInt
import com.yxr.base.widget.CustomClickableSpan

fun String.getSuffix():String{
    if (this.contains(".")){
        try {
            val start = this.lastIndexOf(".")
            return this.substring(start, this.length)
        }catch (e:Throwable){
            e.printStackTrace()
        }
    }
    return ""
}

fun SpannableString.keywordClickable(
    keyword: String,
    @ColorInt highlightColor: Int = 0xffff5d00.toInt(),
    listener: View.OnClickListener? = null
): SpannableString {
    val text = toString()
    val indexList = text.indexAllText(keyword)
    indexList.forEach { index ->
        setSpan(
            object : CustomClickableSpan(highlightColor) {
                override fun onClick(v: View) {
                    listener?.onClick(v)
                }
            },
            index,
            index + keyword.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }
    return this
}

fun String.indexAllText(keyword: String?): List<Int> {
    val indexList = arrayListOf<Int>()
    if (!keyword.isNullOrBlank()) {
        var tempText: String = this
        while (tempText.contains(keyword)) {
            val index = tempText.indexOf(keyword)
            indexList.add(index)
            if (tempText.length >= index + keyword.length) {
                tempText = tempText.substring(index + keyword.length)
            }
        }
    }
    return indexList
}

fun String.strFormat(vararg args: Any?): String {
    return try {
        String.format(this, *args)
    } catch (e: Throwable) {
        e.printStackTrace()
        this
    }
}

fun Int.resFormat(context: Context, vararg args: Any?): String {
    var resStr: String? = null
    return try {
        resStr = context.getString(this)
        resStr.strFormat(*args)
    } catch (e: Throwable) {
        e.printStackTrace()
        resStr ?: ""
    }
}

fun String.phoneDesensitization(): String {
    if (this.length > 7) {
        return this.replaceRange(3, 7, "****")
    }
    return this
}