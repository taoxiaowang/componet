package com.hikcreate.app;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.hikcreate.base.BaseAppLogic;

/**
 * @author wangtao55
 * @date 2019/9/23
 * @mail wangtao55@hikcreate.com
 */
public class AppAppLogic extends BaseAppLogic {
        @Override
    public void onCreate(Context context) {
        Log.v("AppAppLogic", "onCreate--------------------->");
    }

    @Override
    public void onTerminate(Context context) {
        Log.v("AppAppLogic", "onTerminate--------------------->");
    }

    @Override
    public void onLowMemory(Context context) {
        Log.v("AppAppLogic", "onLowMemory--------------------->");
    }

    @Override
    public void onConfigurationChanged(Context context, Configuration newConfig) {
        Log.v("AppAppLogic", "onConfigurationChanged--------------------->");
    }
}
