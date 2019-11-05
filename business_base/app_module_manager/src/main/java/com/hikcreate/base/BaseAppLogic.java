package com.hikcreate.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * 基础app配置类
 *
 * @author wangtao55
 * @date 2019/9/23
 * @mail wangtao55@hikcreate.com
 */
public abstract class BaseAppLogic {

    private Application mApplication;

    abstract public void onCreate(Context context);

    abstract public void onTerminate(Context context);

    abstract public void onLowMemory(Context context);

    abstract public void onConfigurationChanged(Context context, Configuration newConfig);


}
