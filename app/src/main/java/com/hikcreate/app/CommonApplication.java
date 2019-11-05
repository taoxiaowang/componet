package com.hikcreate.app;

import android.app.Application;

import com.hikcreate.base.AppContext;
import com.hikcreate.data.config.ActivityLifeCycleInitConfig;
import com.hikcreate.data.config.AppInitConfig;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;

/**
 * @author wangtao55
 * @date 2019/9/23
 * @mail wangtao55@hikcreate.com
 */
public class CommonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerAppInit();
        registerActivityLife();
        AppContext.getInstance().logicOnCreate(this);
        LeakCanary.install(this);
    }

    private void registerAppInit() {

        //登录组件
        AppContext.getInstance().registerAppLogic(AppInitConfig.LOGIN_CONFIG);
        //app壳组件
        AppContext.getInstance().registerAppLogic(AppInitConfig.APP_CONFIG);
    }

    private void registerActivityLife() {
        //登录组件的acitivity生命周期监控
        AppContext.getInstance().registerActivityLifeLogic(ActivityLifeCycleInitConfig.LOGIN_CONFIG);
        //app壳组件的acitivity生命周期监控
        AppContext.getInstance().registerActivityLifeLogic(ActivityLifeCycleInitConfig.APP_CONFIG);
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
