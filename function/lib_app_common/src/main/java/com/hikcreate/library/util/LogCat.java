package com.hikcreate.library.util;

import android.util.Log;

import java.util.Locale;

public class LogCat {
    public static String TAG = "hik_create";
    public static boolean debug = true;

    public LogCat() {
    }

    public static void setDebug(boolean debug) {
        LogCat.debug = debug;
    }

    public static void v(String format, Object... args) {
        v(TAG, format, args);
    }

    public static void v(String tag, String format, Object... args) {
        if (debug) {
            Log.v(tag, buildMessage(format, args));
        }

    }

    public static void d(String format, Object... args) {
        d(TAG, format, args);

    }

    public static void d(String tag, String format, Object... args) {
        if (debug) {
            Log.d(tag, buildMessage(format, args));
        }

    }

    public static void i(String format, Object... args) {
        i(TAG, format, args);
    }

    public static void i(String tag, String format, Object... args) {
        if (debug) {
            String message = buildMessage(format, args);
            Log.i(tag, message);
        }
    }

    public static void w(String format, Object... args) {
        w(TAG, format, args);
    }

    public static void w(String tag, String format, Object... args) {
        if (debug) {
            String message = buildMessage(format, args);
            Log.w(tag, message);
        }
    }

    public static void e(String format, Object... args) {
        e(TAG, format, args);

    }

    public static void e(String tag, String format, Object... args) {
        if (debug) {
            String message = buildMessage(format, args);
            Log.e(tag, message);
        }

    }

    public static void e(Throwable err, String format, Object... args) {
        e(TAG, err, format, args);

    }

    public static void e(String tag, Throwable err, String format, Object... args) {
        if (debug) {
            String message = buildMessage(format, args);
            Log.e(tag, message);
        }

    }

    public static void wtf(String format, Object... args) {
        if (debug) {
            Log.wtf(TAG, buildMessage(format, args));
        }

    }

    public static void wtf(Throwable err, String format, Object... args) {
        if (debug) {
            Log.wtf(TAG, buildMessage(format, args), err);
        }

    }

    private static String buildMessage(String format, Object... args) {
        String msg = (args == null || args.length == 0) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = (new Throwable()).fillInStackTrace().getStackTrace();
        String caller = "<unknown>";

        for (int i = 2; i < trace.length; ++i) {
            Class clazz = trace[i].getClass();
            if (!clazz.equals(LogCat.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf(46) + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf(36) + 1);
                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }

        return String.format(Locale.CHINA, "[%d] %s: %s", Thread.currentThread().getId(), caller, msg).replaceAll("\r\n", "");
    }
}
