package com.hikcreate.test;

import android.app.Application;

import com.hikcreate.base.AppContext;

/**
 * @author wangtao55
 * @date 2019/9/23
 * @mail wangtao55@hikcreate.com
 */
public class CommonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.getInstance().registerAppLogic("com.hikcreate.init.PassPortAppLogic");
        AppContext.getInstance().registerActivityLifeLogic("com.hikcreate.init.PassPortActivityLifeCycle");
        AppContext.getInstance().logicOnCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppContext.getInstance().logicOnTerminate(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppContext.getInstance().logicOnLowMemory(this);
    }

}
