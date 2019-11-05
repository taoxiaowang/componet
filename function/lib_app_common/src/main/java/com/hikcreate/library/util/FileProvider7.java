package com.hikcreate.library.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;

/**
 * 兼容Android7.0 file://url废弃，改用FileProvider操作
 *
 * @author gongwei 2018.12.25
 */
public class FileProvider7 {

    public static final String MIME_TYPE_APP_PACKAGE = "application/vnd.android.package-archive";

    /**
     * 获取File Uri
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public static Uri getUriForFile24(Context context, File file) {
        Uri fileUri = android.support.v4.content.FileProvider.getUriForFile(context,
                context.getPackageName() + ".android7.fileprovider",
                file);
        return fileUri;
    }

    /**
     * 安装Apk
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file, InstallApkCallBack callBack) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        setIntentDataAndType(context, intent, MIME_TYPE_APP_PACKAGE, file, true);
        context.startActivity(intent);
        if (callBack != null) {
            callBack.installed();
        }
    }

    /**
     * 修改Intent的setDataAndType
     *
     * @param context
     * @param intent
     * @param type
     * @param file
     * @param writeAble
     */
    public static void setIntentDataAndType(Context context, Intent intent, String type, File file, boolean writeAble) {
        intent.setDataAndType(getUriForFile(context, file), type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
    }

    public interface InstallApkCallBack {
        void installed();
    }
}
