package com.hikcreate.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.design.widget.AppBarLayout;
import android.view.View;

/**
 * 用于存放View的公共方法
 *
 * @author yslei
 * @date 2019/5/23
 * @email leiyongsheng@hikcreate.com
 */
public class ViewUtil {


    /**
     * 启动CoordinatorLayout滑动折叠
     * https://www.jianshu.com/p/7b997c2e436c
     *
     * @param appBarLayout
     */
    public static void enableExitUtilCollapsed(AppBarLayout appBarLayout) {
        int i0 = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;
        int i1 = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED;
        View appBarChildAt = appBarLayout.getChildAt(0);
        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) appBarChildAt.getLayoutParams();
        appBarParams.setScrollFlags(i0 | i1);// 重置折叠效果
        appBarChildAt.setLayoutParams(appBarParams);
    }

    /**
     * 关闭CoordinatorLayout滑动折叠
     *
     * @param appBarLayout
     */
    public static void disableCollapsed(AppBarLayout appBarLayout) {
        View appBarChildAt = appBarLayout.getChildAt(0);
        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) appBarChildAt.getLayoutParams();
        appBarParams.setScrollFlags(0);//这个加了之后不可滑动
        appBarChildAt.setLayoutParams(appBarParams);
    }

    /**
     * 通过View获取Activity实例
     * 使用请判空
     *
     * @return host activity; or null if not available
     */
    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        if (context != null) {
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }
}