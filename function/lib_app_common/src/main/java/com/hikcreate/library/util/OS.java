package com.hikcreate.library.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hikcreate.library.app.HikApplicationCreate;

import java.io.FileOutputStream;
import java.util.UUID;

/**
 * 系统相关工具方法类
 *
 * @author gongwei
 */
public abstract class OS {

    /**
     * 获取屏幕Display
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 获得状态栏高度
     * ps:非全屏+非沉浸式的Activity才可用
     *
     * @param activity
     */
    public static int getStateHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 开启软键盘
     *
     * @param editText
     */
    public static void showSoftKeyboard(final EditText editText) {
        showSoftKeyboard(editText, false);
    }

    /**
     * 开启软键盘
     *
     * @param editText
     * @param lazy
     */
    public static void showSoftKeyboard(final EditText editText, boolean lazy) {
        if (lazy) {
            editText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    editText.requestFocus();
                    InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }, 200);
        } else {
            editText.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 开启软键盘
     *
     * @param activity
     */
    public static void showSoftKeyboard(final Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null && activity.getCurrentFocus() instanceof EditText) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
                }
            }, 200);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = activity.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    /**
     * 获取设备机型
     *
     * @return
     */
    public static String getDeviceModel() {
        return String.format("%s %s", Build.BRAND, Build.MODEL); //手机厂商 手机型号
    }

    /**
     * 获得DeviceCode
     *
     * @return
     */
    public static String getDeviceCode() {
        String result = getAndroidId();
        if (TextUtils.isEmpty(result)) {
            result = getBuildSerial();
        }
        return result;
    }

    /**
     * 获取Android Id
     *
     * @return AndroidId or null
     */
    private static String getAndroidId() {
        String androidId = Settings.Secure.getString(HikApplicationCreate.getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId)) {
            try {
                UUID androidUuid = new UUID(androidId.hashCode(), androidId.hashCode() << 32 | androidId.hashCode());
                return androidUuid.toString().replace("-", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取Serial号
     * 失败时则通过硬件信息生成的伪设备码
     *
     * @return Build.SERIAL or Pseudo DeviceId
     */
    public static String getBuildSerial() {
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        String uuid = "";
        try {
            //API>=9 使用serial号
            uuid = new UUID(m_szDevIDShort.hashCode(), Build.SERIAL.hashCode()).toString();
        } catch (Exception exception) {
            //使用硬件信息构建出来的15位号码
            uuid = new UUID(m_szDevIDShort.hashCode(), "unknown".hashCode()).toString();
        }
        return uuid.replace("-", "");
    }

    /**
     * 根据宽度和比例计算高度
     *
     * @param ratio eg: 16f/7
     * @param width
     * @return
     */
    public static int getHeightByRatioAndWidth(float ratio, int width) {
        return (int) (width / ratio);
    }

    /**
     * 根据屏幕宽度和比例计算高度
     *
     * @param context
     * @param ratio   eg: 16f/7
     * @return
     */
    public static int getHeightByRatioAndScreenWidth(Activity context, float ratio) {
        return getHeightByRatioAndWidth(ratio, getScreenWidth(context));
    }

    /**
     * 判断application是否是在主线程
     *
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return packageName.equals(processName);
    }

    /**
     * 获取当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        String processName = null;

        // MessageFlowActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;

                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取meta数据
     */
    public static String getMetaData(Context ctx, String metaData) {

        if (ctx == null) {
            LogCat.d("getMetaData error! ctx is null!");
            return "";
        }

        ApplicationInfo appInfo = null;
        try {
            appInfo = ctx.getPackageManager()
                    .getApplicationInfo(ctx.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String result;
        if (appInfo != null) {
            result = appInfo.metaData.getString(metaData);
            if (result != null) {
                return result;
            } else {
                return String.valueOf(appInfo.metaData.getInt(metaData));
            }
        }
        return "";
    }

    /**
     * 把文件扫描到媒体库
     *
     * @param context
     * @param filepath
     */
    public static void scanFile(Context context, String... filepath) {
        if (filepath != null) {
            for (String path : filepath) {
                syncDestination(path);
            }
        }
        MediaScannerConnection.scanFile(context, filepath, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
            }
        });
    }

    private static void syncDestination(String filepath) {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(filepath, true);
            outStream.getFD().sync();
        } catch (Exception ex) {
            android.studio.os.LogCat.e("IOException trying to sync " + filepath + ": " + ex);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (Exception ex) {
                    android.studio.os.LogCat.e("IOException while closing synced file: ", ex);
                }
            }
        }
    }
}
