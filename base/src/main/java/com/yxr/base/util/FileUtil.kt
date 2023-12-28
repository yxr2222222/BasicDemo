package com.yxr.base.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.yxr.base.util.PathUtil.Companion.getDir
import com.yxr.base.util.PathUtil.Companion.getFilePath
import java.io.*

class FileUtil {
    companion object {
        /**
         * 快速关闭Closeable
         *
         * @param closeables
         */
        @JvmStatic
        fun closeQuietly(vararg closeables: Closeable?) {
            if (closeables.isNotEmpty()) {
                for (closeable in closeables) {
                    if (closeable != null) {
                        try {
                            closeable.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        /**
         * 文件是否存在
         */
        @JvmStatic
        fun fileIsExists(path: String?): Boolean {
            return path != null && File(path).exists()
        }

        /**
         * 保存内容到文件
         *
         * @param path    文件完整路径
         * @param content 存储内容
         */
        @JvmStatic
        fun saveContentToFile(path: String, content: String?): Boolean {
            return saveContentToFile(path, content, false)
        }

        /**
         * 保存内容到文件
         *
         * @param path    文件完整路径
         * @param content 存储内容
         */
        @JvmStatic
        fun saveContentToFile(path: String, content: ByteArray?): Boolean {
            return saveContentToFile(path, content, false)
        }

        @JvmStatic
        fun saveContentToFile(path: String, content: String?, isAppend: Boolean): Boolean {
            return if (content == null) {
                false
            } else saveContentToFile(path, content.toByteArray(), isAppend)
        }

        /**
         * 保存内容到文件
         *
         * @param path     文件完整路径
         * @param content  存储内容
         * @param isAppend 是否拼接
         * @return 是否成功
         */
        @JvmStatic
        fun saveContentToFile(path: String, content: ByteArray?, isAppend: Boolean): Boolean {
            var isSuccess = true
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(path, isAppend)
                fos.write(content)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                isSuccess = false
                e.printStackTrace()
            } finally {
                closeQuietly(fos)
            }
            return isSuccess
        }

        @JvmStatic
        fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            val resolver = context.contentResolver
            val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val insertUri: Uri?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10 (Q) and above
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                values.put(MediaStore.Images.Media.IS_PENDING, 1)
                insertUri = resolver.insert(externalUri, values)
                if (insertUri != null) {
                    try {
                        val outputStream = resolver.openOutputStream(insertUri)
                        if (outputStream != null) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            outputStream.close()
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    } finally {
                        values.clear()
                        values.put(MediaStore.Images.Media.IS_PENDING, 0)
                        resolver.update(insertUri, values, null, null)
                    }
                }
            } else {
                // For versions before Android 10
                insertUri = resolver.insert(externalUri, values)
                if (insertUri != null) {
                    try {
                        val outputStream = resolver.openOutputStream(insertUri)
                        if (outputStream != null) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            outputStream.close()
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }
        @JvmStatic
        fun saveBitmapToFile(path: String?, bitmap: Bitmap?, quality: Int): Boolean {
            if (bitmap == null || bitmap.isRecycled) {
                return false
            }
            var isSuccess = true
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(path)
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos)
                fos.flush()
                fos.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                isSuccess = false
                e.printStackTrace()
            } finally {
                closeQuietly(fos)
            }
            return isSuccess
        }

        @JvmStatic
        fun deleteDirectoryFiles(root: File) {
            deleteDirectoryFiles(root, "")
        }

        /**
         * 删除某个文件下面的所有名称包含conditionPrefix的文件
         *
         * @param root            文件夹
         * @param conditionPrefix 前缀
         */
        @JvmStatic
        fun deleteDirectoryFiles(root: File, conditionPrefix: String?) {
            try {
                if (root.isDirectory) {
                    val files = root.listFiles()
                    if (files != null) {
                        for (file in files) {
                            if (file.isDirectory) {
                                deleteDirectoryFiles(file, conditionPrefix)
                            } else if (file.name.startsWith(conditionPrefix!!)) {
                                file.delete()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * 外部存储器是否可用
         *
         * @return 外部存储器是否可用
         */
        @JvmStatic
        fun isExternalStorageWritable(): Boolean {
            return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        }

        /**
         * 从Asset中获取数据
         *
         * @param context  上下文
         * @param fileName 文件名
         * @return 内容
         */
        @JvmStatic
        fun readFromAsset(context: Context, fileName: String?): String? {
            var result: String? = null
            val baos = ByteArrayOutputStream()
            var inputStream: InputStream? = null
            var bufferedInputStream: BufferedInputStream? = null
            try {
                val assetManager = context.assets
                inputStream = assetManager.open(fileName!!)
                bufferedInputStream = BufferedInputStream(inputStream)
                val buffer = ByteArray(1024)
                var len: Int
                while (bufferedInputStream.read(buffer).also { len = it } != -1) {
                    baos.write(buffer, 0, len)
                }
                result = baos.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                closeQuietly(baos, inputStream, bufferedInputStream)
            }
            return result
        }

        /**
         * 从文件中读取内容
         *
         * @param filePath 文件完整地址
         * @return 文件内容
         */
        @JvmStatic
        fun readFile(filePath: String): String {
            val sb = StringBuilder("")
            val file = File(filePath)
            //打开文件输入流
            var inputStream: FileInputStream? = null
            try {
                inputStream = FileInputStream(file)
                val buffer = ByteArray(1024)
                var len = inputStream.read(buffer)
                while (len > 0) {
                    sb.append(String(buffer, 0, len))
                    len = inputStream.read(buffer)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                closeQuietly(inputStream)
            }
            return sb.toString()
        }

        @JvmStatic
        fun copyAssetsSingleFile(context: Context, outDir: String, fileName: String): String? {
            return copyAssetsSingleFile(context, outDir, fileName, true)
        }

        /**
         * 复制单个文件
         *
         * @param outDir      String 输出文件路径 如：data/user/0/com.test/files
         * @param fileName    String 要复制的文件名 如：abc.txt
         * @param replaceFile 如果存在文件是否覆盖替换
         */
        @JvmStatic
        fun copyAssetsSingleFile(
            context: Context,
            outDir: String,
            fileName: String,
            replaceFile: Boolean
        ): String? {
            val file = File(outDir)
            if (!file.exists()) {
                file.mkdirs()
            }
            val outFile = File(file, fileName)
            return if (outFile.exists() && !replaceFile) {
                outFile.path
            } else try {
                val inputStream = context.assets.open(fileName)
                val fileOutputStream = FileOutputStream(outFile)
                // Transfer bytes from inputStream to fileOutputStream
                val buffer = ByteArray(1024)
                var byteRead: Int
                while (-1 != inputStream.read(buffer).also { byteRead = it }) {
                    fileOutputStream.write(buffer, 0, byteRead)
                }
                inputStream.close()
                fileOutputStream.flush()
                fileOutputStream.close()
                outFile.path
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        @JvmStatic
        fun getFilePath(
            context: Context?,
            uri: Uri?,
            tempFileName: String,
            existsDelete: Boolean
        ): String? {
            if (context == null || uri == null) {
                return null
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && context.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.Q
            ) {
                val photoTempFile = File(getDir("temp"), tempFileName)
                if (photoTempFile.exists()) {
                    if (existsDelete) {
                        photoTempFile.delete()
                    } else {
                        return photoTempFile.path
                    }
                }
                var bis: InputStream? = null
                var bos: BufferedOutputStream? = null
                try {
                    bis = context.contentResolver.openInputStream(uri)
                    if (bis != null) {
                        bos = BufferedOutputStream(FileOutputStream(photoTempFile.path))
                        val buffer = ByteArray(1024)
                        var bytes = bis.read(buffer)
                        while (bytes >= 0) {
                            bos.write(buffer, 0, bytes)
                            bos.flush()
                            bytes = bis.read(buffer)
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    closeQuietly(bis, bos)
                }
                return photoTempFile.path
            }
            try {
                return getFilePath(uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}