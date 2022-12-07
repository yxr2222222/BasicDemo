package com.yxr.base.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ciba
 * @description 文件IO操作工具类
 * @date 2020/9/17
 */
public class FileUtil {

    /**
     * 快速关闭Closeable
     *
     * @param closeables
     */
    public static void closeQuietly(Closeable... closeables) {
        if (closeables != null && closeables.length > 0) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 文件是否存在
     */
    public static boolean fileIsExists(@Nullable String path) {
        return path != null && new File(path).exists();
    }

    /**
     * 保存内容到文件
     *
     * @param path    文件完整路径
     * @param content 存储内容
     */
    public static boolean saveContentToFile(String path, String content) {
        return saveContentToFile(path, content, false);
    }

    /**
     * 保存内容到文件
     *
     * @param path    文件完整路径
     * @param content 存储内容
     */
    public static boolean saveContentToFile(String path, byte[] content) {
        return saveContentToFile(path, content, false);
    }

    public static boolean saveContentToFile(String path, String content, boolean isAppend) {
        if (content == null) {
            return false;
        }
        return saveContentToFile(path, content.getBytes(), isAppend);
    }

    /**
     * 保存内容到文件
     *
     * @param path     文件完整路径
     * @param content  存储内容
     * @param isAppend 是否拼接
     * @return 是否成功
     */
    public static boolean saveContentToFile(String path, byte[] content, boolean isAppend) {
        boolean isSuccess = true;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path, isAppend);
            fos.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            closeQuietly(fos);
        }
        return isSuccess;
    }

    public static boolean saveBitmapToFile(String path, Bitmap bitmap, int quality) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        boolean isSuccess = true;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            closeQuietly(fos);
        }
        return isSuccess;
    }

    public static void deleteDirectoryFiles(File root) {
        deleteDirectoryFiles(root, "");
    }

    /**
     * 删除某个文件下面的所有名称包含conditionPrefix的文件
     *
     * @param root            文件夹
     * @param conditionPrefix 前缀
     */
    public static void deleteDirectoryFiles(File root, String conditionPrefix) {
        try {
            if (root.isDirectory()) {
                File[] files = root.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            deleteDirectoryFiles(file, conditionPrefix);
                        } else if (file.getName().startsWith(conditionPrefix)) {
                            file.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 外部存储器是否可用
     *
     * @return 外部存储器是否可用
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 从Asset中获取数据
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return 内容
     */
    public static String readFromAsset(Context context, String fileName) {
        String result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            AssetManager assetManager = context.getAssets();
            inputStream = assetManager.open(fileName);
            bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(baos, inputStream, bufferedInputStream);
        }
        return result;
    }

    /**
     * 从文件中读取内容
     *
     * @param filePath 文件完整地址
     * @return 文件内容
     */
    public static String readFile(@NonNull String filePath) {
        StringBuilder sb = new StringBuilder("");
        File file = new File(filePath);
        //打开文件输入流
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            while (len > 0) {
                sb.append(new String(buffer, 0, len));
                len = inputStream.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(inputStream);
        }
        return sb.toString();
    }

    public static String copyAssetsSingleFile(Context context, String outDir, String fileName) {
        return copyAssetsSingleFile(context, outDir, fileName, true);
    }

    /**
     * 复制单个文件
     *
     * @param outDir      String 输出文件路径 如：data/user/0/com.test/files
     * @param fileName    String 要复制的文件名 如：abc.txt
     * @param replaceFile 如果存在文件是否覆盖替换
     */
    public static String copyAssetsSingleFile(Context context, String outDir, String fileName, boolean replaceFile) {
        File file = new File(outDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        File outFile = new File(file, fileName);
        if (outFile.exists() && !replaceFile) {
            return outFile.getPath();
        }
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            // Transfer bytes from inputStream to fileOutputStream
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = inputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return outFile.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static String getFilePath(@Nullable Context context,
                                     @Nullable Uri uri,
                                     String tempFileName,
                                     boolean existsDelete) {
        if (context == null || uri == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.Q
        ) {
            File photoTempFile = new File(PathUtil.getDir("temp"), tempFileName);
            if (photoTempFile.exists()) {
                if (existsDelete) {
                    photoTempFile.delete();
                } else {
                    return photoTempFile.getPath();
                }
            }

            InputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = context.getContentResolver().openInputStream(uri);
                if (bis != null) {
                    bos = new BufferedOutputStream(new FileOutputStream(photoTempFile.getPath()));
                    byte[] buffer = new byte[1024];
                    int bytes = bis.read(buffer);
                    while (bytes >= 0) {
                        bos.write(buffer, 0, bytes);
                        bos.flush();
                        bytes = bis.read(buffer);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
                closeQuietly(bis, bos);
            }
            return photoTempFile.getPath();
        }
        try {
            return PathUtil.getFilePath(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
