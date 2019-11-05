package com.hikcreate.baidutextdect;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fujiayi on 2017/5/19.
 */

public class FileUtil {

    private static final String sAudioDir = "baiduTTS";
    public static String APP_ROOT_FILE_PATH;//sd卡根路劲，在Application中初始化
    public static final String APP_ROOT_FILE = "/hikcreate/";//app文件根路径
    public static String IMAGE_CACHE = APP_ROOT_FILE_PATH + APP_ROOT_FILE + "/cache/image/";//图片缓存路径


    // 创建一个临时目录，用于复制临时文件，如assets目录下的离线资源文件
    public static String createTmpDir(Context context) {
        String tmpDir = Environment.getExternalStorageDirectory().toString() + "/" + sAudioDir;
        if (!FileUtil.makeDir(tmpDir)) {
            tmpDir = context.getExternalFilesDir(sAudioDir).getAbsolutePath();
            if (!FileUtil.makeDir(sAudioDir)) {
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

    public static String getImageCacheFilePath() {
        return getImageCacheFilePath("pic.jpg");
    }

    public static String getImageCacheFilePath(String fileName) {
        createDirFile(FileUtil.getImgCachePath());
        return FileUtil.getImgCachePath() + File.separator + fileName;
    }

    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String getImgCachePath() {
        return IMAGE_CACHE;
    }
}

