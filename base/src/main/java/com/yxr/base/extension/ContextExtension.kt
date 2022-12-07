package com.yxr.base.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable

fun Context.startSimpleActivity(
    cls: Class<out Activity>,
    bundleMap: HashMap<String, Any?>? = null
) {
    val intent = Intent(this, cls)
    bundleMap?.let { bundles ->
        for (entry in bundles.entries) {
            val key = entry.key
            val value = entry.value
            when (value) {
                is String -> {
                    intent.putExtra(key, value)
                }
                is Int -> {
                    intent.putExtra(key, value)
                }
                is Long -> {
                    intent.putExtra(key, value)
                }
                is Float -> {
                    intent.putExtra(key, value)
                }
                is Boolean -> {
                    intent.putExtra(key, value)
                }
                is java.io.Serializable -> {
                    intent.putExtra(key, value)
                }
                is Parcelable -> {
                    intent.putExtra(key, value)
                }
            }
        }
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}