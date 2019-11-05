package com.hikcreate.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.hikcreate.base.BaseActivityLifeLogic;

/**
 * @author wangtao55
 * @date 2019/9/23
 * @mail wangtao55@hikcreate.com
 */
public class AppActivityLifeCycle extends BaseActivityLifeLogic {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Toast.makeText(activity, "AppActivityLifeCycle", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle savedInstanceState) {

    }
}
