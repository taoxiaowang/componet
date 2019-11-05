package com.hikcreate.base;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * 基础activity配置类
 *
 * @author wangtao55
 * @date 2019/9/23
 * @mail wangtao55@hikcreate.com
 */
public abstract class BaseActivityLifeLogic {

    abstract public void onActivityCreated(Activity activity, Bundle savedInstanceState);

    abstract public void onActivityStarted(Activity activity);

    abstract public void onActivityResumed(Activity activity);

    abstract public void onActivityPaused(Activity activity);

    abstract public void onActivityStopped(Activity activity);

    abstract public void onActivityDestroyed(Activity activity);

    abstract public void onActivitySaveInstanceState(Activity activity, Bundle savedInstanceState);


}
