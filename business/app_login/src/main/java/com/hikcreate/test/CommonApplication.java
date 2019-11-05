package com.hikcreate.test;

import android.app.Application;

import com.hikcreate.base.AppContext;
import com.hikcreate.data.config.ActivityLifeCycleInitConfig;
import com.hikcreate.data.config.AppInitConfig;

/**
 * @author wangtao55
 * @date 2019/9/23
 * @mail wangtao55@hikcreate.com
 */
public class CommonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.getInstance().registerAppLogic(AppInitConfig.LOGIN_CONFIG);
        AppContext.getInstance().registerActivityLifeLogic(ActivityLifeCycleInitConfig.LOGIN_CONFIG);
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
