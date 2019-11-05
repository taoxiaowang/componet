package com.init;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.hikcreate.base.AppContext;
import com.hikcreate.base.BaseAppLogic;
import com.hikcreate.data.config.AppProvider;
import com.hikcreate.login.moduel.router.provider.ChangePwdProvider;
import com.hikcreate.login.moduel.router.provider.LoginProvider;
import com.hikcreate.module_router.LocalRouter;

/**
 * 类说明
 *
 * @author wangtao55
 * @date 2019/9/23
 * @mail wangtao55@hikcreate.com
 */
public class LoginAppLogic extends BaseAppLogic {
    @Override
    public void onCreate(Context application) {
        Log.v("LoginAppLogic", "onCreate--------------------->");
        initProviderCreate();
    }

    @Override
    public void onTerminate(Context applicatio) {
        Log.v("LoginAppLogic", "onTerminate--------------------->");
    }

    @Override
    public void onLowMemory(Context applicatio) {
        Log.v("LoginAppLogic", "onLowMemory--------------------->");
    }

    @Override
    public void onConfigurationChanged(Context applicatio, Configuration newConfig) {
        Log.v("LoginAppLogic", "onConfigurationChanged--------------------->");
    }

    private void initProviderCreate() {
        LocalRouter.getInstance(AppContext.
                getInstance().getApplication()).
                registerProvider(AppProvider.LOGIN_PROVIDER,
                        new LoginProvider());
        LocalRouter.getInstance(AppContext.
                getInstance().getApplication()).
                registerProvider(AppProvider.USER_INFO_CHANGE_PROVIDER,
                        new ChangePwdProvider());
    }
}
