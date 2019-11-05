package com.hikcreate.library.app;

import android.app.Application;

/**
 * app初始化基础类
 *
 * @author wangtao
 * @date 2019/07/04
 * @mail wangtao55@hikcreate.com
 */
public class HikApplicationCreate {

    protected static Application appContext;

    public static Application getApplication() {
        return appContext;
    }

    public static void setApplication(Application mApplication) {
        appContext = mApplication;
    }
}
