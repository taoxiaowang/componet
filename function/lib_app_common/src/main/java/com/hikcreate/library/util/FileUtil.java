package com.hikcreate.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.studio.os.BitmapUtils;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * 文件工具类
 *
 * @author gongwei
 * @date 2019.1.25
 */
public class FileUtil {

    public static String APP_ROOT_FILE_PATH;//sd卡根路劲，在Application中初始化
    public static final String APP_ROOT_FILE = "/hikcreate/";//app文件根路径
    public static String IMAGE_CACHE = APP_ROOT_FILE_PATH + APP_ROOT_FILE + "/cache/image/";//图片缓存路径

    /**
     * 在Application初始化APP_ROOT_FILE_PATH之后重设一下这个值，否则可能会得到null/cache/image/...这样的错误路径
     *
     * @param appRootFilePath
     */
    public static void initCachePath(String appRootFilePath) {
        APP_ROOT_FILE_PATH = appRootFilePath;
        IMAGE_CACHE = APP_ROOT_FILE_PATH + APP_ROOT_FILE + "/cache/image/";
        createDirFile(IMAGE_CACHE + ".nomedia");
    }

    /**
     * 创建目录
     *
     * @param path 目录路径
     */
    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    public static String getImgCachePath() {
        return IMAGE_CACHE;
    }

    public static String getImageCacheFilePath() {
        return getImageCacheFilePath("pic.jpg");
    }

    public static String getImageCacheFilePath(String fileName) {
        createDirFile(FileUtil.getImgCachePath());
        return FileUtil.getImgCachePath() + File.separator + fileName;
    }

    /**
     * 创建临时图片文件
     *
     * @param context
     * @return
     */
    public static File createImageTmpFile(Context context) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(pic, fileName + ".jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".jpg");
            return tmpFile;
        }
    }

    /**
     * 获取http缓存地址
     *
     * @param context
     * @return
     */
    public static File createAHttpDefaultCacheDir(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "ahttp-cache");
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

    /**
     * 获取相册存储路径
     *
     * @return
     */
    public static String getDICMBasePath() {
        String basePath = null;
        File fileDCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (fileDCIM != null) {
            basePath = fileDCIM + APP_ROOT_FILE;
        } else {
            basePath = Environment.getExternalStorageDirectory() + APP_ROOT_FILE;
        }
        return basePath;
    }

    /**
     * 计算所有缓存所占空间
     *
     * @param context
     * @return
     */
    public static long calcAllCache(Context context) {
        List<File> listFile = getAllCacheFile(context);
        long allCacheSize = 0;
        for (File file : listFile) {
            allCacheSize += calcFileTotalSize(file);
        }
        return allCacheSize;
    }

    /**
     * 递归计算File空间
     *
     * @param file
     * @return
     */
    public static long calcFileTotalSize(File file) {
        if (file != null && file.exists()) {
            long totalSize = 0;
            if (file.isFile() && !file.getName().startsWith(".")) {//隐藏文件不计入统计了，华为...
                totalSize += file.length();
            } else if (file.isDirectory()) {
                try {
                    for (File child : file.listFiles()) {
                        totalSize += calcFileTotalSize(child);
                    }
                } catch (Exception e) {
                    //如果未授权有可能报错
                }
            }
            return totalSize;
        } else {
            return 0;
        }
    }

    /**
     * 计算所有缓存所占空间
     *
     * @param context
     * @return
     */
    public static void deleteAllCache(Context context) {
        List<File> listFile = getAllCacheFile(context);
        for (File file : listFile) {
            deleteFile(file);
        }
    }

    /**
     * 递归删除文件(加6)
     *
     * @param file
     * @return
     */
    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                try {
                    for (File child : file.listFiles()) {
                        deleteFile(child);
                    }
                } catch (Exception e) {
                    //如果未授权有可能报错
                }
            }
        }
    }

    /**
     * 获取所有缓存目录
     *
     * @param context
     * @return
     */
    public static List<File> getAllCacheFile(Context context) {
        List<File> list = new ArrayList<>();
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())) {
            File internalCacheDir = context.getCacheDir();
            list.add(internalCacheDir);
            File cacheLog = new File(context.getExternalCacheDir().getAbsolutePath() + "/log");
            list.add(cacheLog);
        } else {
            File cacheHttp = new File(context.getCacheDir().getAbsolutePath() + "/ahttp-cache");
            list.add(cacheHttp);
        }
        File cacheExternalImage = new File(IMAGE_CACHE);
        list.add(cacheExternalImage);

        File cacheDICMIamge = new File(getDICMBasePath());
        list.add(cacheDICMIamge);

        File gideCache = Glide.getPhotoCacheDir(context);
        list.add(gideCache);
        return list;
    }

    /**
     * 保存图片到SD卡
     *
     * @param bitmap 图片的bitmap对象
     * @return
     */
    public static String saveImageToSDCard(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        createDirFile(FileUtil.getImgCachePath());
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String newFilePath = FileUtil.getImgCachePath() + fileName;
        File file = createNewFile(newFilePath);
        if (file == null) {
            return null;
        }
        try {
            fileOutputStream = new FileOutputStream(newFilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
        } catch (FileNotFoundException e1) {
            return null;
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return newFilePath;
    }

    /**
     * 根据路径获取图片Bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap getImage(String path) {
        return scaledBitmap(path, 720, 720);
    }

    public static Bitmap scaledBitmap(String filePath, int dstWidth, int dstHeight) {
        return BitmapUtils.scaledBitmap(filePath, dstWidth, dstHeight, Bitmap.Config.RGB_565);
    }
}
