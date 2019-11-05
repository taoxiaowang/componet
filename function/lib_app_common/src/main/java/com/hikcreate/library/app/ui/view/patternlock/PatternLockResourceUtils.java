package com.hikcreate.library.app.ui.view.patternlock;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

public class PatternLockResourceUtils {


    /**
     * 获取颜色值
     */
    public static int getColor(@NonNull Context context, @ColorRes int colorRes) {
        return ContextCompat.getColor(context, colorRes);
    }


    /**
     * 适配px
     */
    public static float getDimensionInPx(@NonNull Context context, @DimenRes int dimenRes) {
        return context.getResources().getDimension(dimenRes);
    }
}
