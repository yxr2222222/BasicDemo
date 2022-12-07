package com.yxr.base.util

import android.content.ContentUris
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.yxr.base.BaseApplication
import java.io.File
import java.net.URISyntaxException

class PathUtil {
    companion object {
        /**
         * 获取文件夹
         */
        @JvmStatic
        fun getDir(ditPath: String): File {
            val dir = File(BaseApplication.context.externalCacheDir?.path + ditPath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return dir
        }

        @WorkerThread
        @Throws(URISyntaxException::class)
        @JvmStatic
        fun getFilePath(fileUri: Uri): String? {
            var uri = fileUri
            var selection: String? = null
            var selectionArgs: Array<String>? = null
            // Uri is different in versions after KITKAT (Android 4.4), we need to
            if (DocumentsContract.isDocumentUri(
                    BaseApplication.context.applicationContext,
                    uri
                )
            ) {
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    if ("image" == type) {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    selection = "_id=?"
                    selectionArgs = arrayOf(
                        split[1]
                    )
                }
            }
            if ("content".equals(uri.scheme, ignoreCase = true)) {
                if (isGooglePhotosUri(uri)) {
                    return uri.lastPathSegment
                }
                val projection = arrayOf(
                    MediaStore.Images.Media.DATA
                )
                try {
                    val cursor = BaseApplication.context.contentResolver?.query(
                        uri,
                        projection,
                        selection,
                        selectionArgs,
                        null
                    )
                    var path: String? = null
                    if (cursor != null) {
                        val columnIndex =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        if (cursor.moveToFirst()) {
                            path = cursor.getString(columnIndex)
                            cursor.close()
                        }
                    }
                    return path
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        @JvmStatic
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        @JvmStatic
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        @JvmStatic
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        @JvmStatic
        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }
    }
}