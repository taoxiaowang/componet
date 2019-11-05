package com.hikcreate.init;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.hikcreate.base.BaseAppLogic;

/**
 * 类说明
 *
 * @author wangtao55
 * @date 2019/9/23
 * @mail wangtao55@hikcreate.com
 */
public class PassPortAppLogic extends BaseAppLogic {
    @Override
    public void onCreate(Context context) {
        Log.v("LoginAppLogic", "onCreate--------------------->");
    }

    @Override
    public void onTerminate(Context context) {
        Log.v("LoginAppLogic", "onTerminate--------------------->");
    }

    @Override
    public void onLowMemory(Context context) {
        Log.v("LoginAppLogic", "onLowMemory--------------------->");
    }

    @Override
    public void onConfigurationChanged(Context context, Configuration newConfig) {
        Log.v("LoginAppLogic", "onConfigurationChanged--------------------->");
    }
}
