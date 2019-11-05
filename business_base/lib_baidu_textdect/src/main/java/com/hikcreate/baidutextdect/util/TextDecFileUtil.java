package com.hikcreate.baidutextdect.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fujiayi on 2017/5/19.
 */

public class TextDecFileUtil {

    private static final String sAudioDir = "baiduTTS";
    public static String IMAGE_CACHE ;//图片缓存路径
    public static final String APP_ROOT_FILE = "/hikcreate/";//app文件根路径

    // 创建一个临时目录，用于复制临时文件，如assets目录下的离线资源文件
    public static String createTmpDir(Context context) {
        String tmpDir = Environment.getExternalStorageDirectory().toString() + "/" + sAudioDir;
        if (!TextDecFileUtil.makeDir(tmpDir)) {
            tmpDir = context.getExternalFilesDir(sAudioDir).getAbsolutePath();
            if (!TextDecFileUtil.makeDir(sAudioDir)) {
                throw new RuntimeException("create model resources dir failed :" + tmpDir);
            }
        }
        return tmpDir;
    }

    public static boolean fileCanRead(String filename) {
        File f = new File(filename);
        return f.canRead();
    }

    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    public static void copyFromAssets(AssetManager assets, String source, String dest, boolean isCover)
            throws IOException {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = assets.open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                    }
                }
            }
        }
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

    public static String getImageCacheFilePath() {
        return getImageCacheFilePath("pic.jpg");
    }

    public static String getImageCacheFilePath(String fileName) {
        createDirFile(TextDecFileUtil.getImgCachePath());
        return TextDecFileUtil.getImgCachePath() + File.separator + fileName;
    }


    public static String getImgCachePath() {
        return getImagePath();
    }


    private static String getImagePath(){

        if(TextUtils.isEmpty(IMAGE_CACHE)){

        }
        return IMAGE_CACHE;
    }

    /**
     * 初始化App根路径
     */
    public static void initAppRootFilePath(Context mContext) {
        try {
            String appRootFilePath;
            if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())) {
                appRootFilePath = Environment.getExternalStorageDirectory().toString() + APP_ROOT_FILE;
            } else {
                appRootFilePath = mContext.getCacheDir().getAbsolutePath() + APP_ROOT_FILE;
            }
            initCachePath(appRootFilePath);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
    public static void initCachePath(String appRootFilePath) {
        IMAGE_CACHE = appRootFilePath + APP_ROOT_FILE + "/cache/image/";
        createDirFile(IMAGE_CACHE + ".nomedia");
    }

}
