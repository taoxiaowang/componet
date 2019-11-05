package com.hikcreate.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import java.util.ArrayList;
import java.util.List;

/**
 * app初始化类
 *
 * @author wangtao55
 * @date 2019/9/21
 * @mail wangtao55@hikcreate.com
 */
@SuppressWarnings("ALL")
public class AppContext {

    private Application mContext;
    private List<String> logicList = new ArrayList<>();
    private List<BaseAppLogic> logicClassList = new ArrayList<>();

    private List<String> logicActivityLifeList = new ArrayList<>();
    private List<BaseActivityLifeLogic> logicActivityLifeClassList = new ArrayList<>();

    private static AppContext mAppContext;

    private AppContext() {
    }

    public static AppContext getInstance() {
        if (mAppContext == null) {
            mAppContext = new AppContext();
        }
        return mAppContext;
    }

    public void registerAppLogic(String logicClass) {
        logicList.add(logicClass);
    }

    public void registerActivityLifeLogic(String logicClass) {
        logicActivityLifeList.add(logicClass);
    }

    public void logicOnCreate(Application application) {

        for (String logicClass : logicList) {
            try {
                Class appClass = Class.forName(logicClass);
                BaseAppLogic appLogic = (BaseAppLogic) appClass.newInstance();
                logicClassList.add(appLogic);
                appLogic.onCreate(application);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (String logicActivityClass : logicActivityLifeList) {
            try {
                Class appClass = Class.forName(logicActivityClass);
                BaseActivityLifeLogic appLogic = (BaseActivityLifeLogic) appClass.newInstance();
                logicActivityLifeClassList.add(appLogic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        init(application);
    }

    public void logicOnTerminate(Application application) {
        for (BaseAppLogic logicClass : logicClassList) {
            try {
                logicClass.onTerminate(application);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void logicOnLowMemory(Application application) {
        for (BaseAppLogic logicClass : logicClassList) {
            try {
                logicClass.onLowMemory(application);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void logicOnConfigurationChanged(Application application, Configuration newConfig) {
        for (BaseAppLogic logicClass : logicClassList) {
            try {
                logicClass.onConfigurationChanged(application, newConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void init(Application application) {
        mContext = application;
        initActivityLifeCycle(application);
        MultiDex.install(application);
    }

    public void initActivityLifeCycle(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                for (BaseActivityLifeLogic logicClass : logicActivityLifeClassList) {
                    try {
                        logicClass.onActivityCreated(activity, savedInstanceState);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                for (BaseActivityLifeLogic logicClass : logicActivityLifeClassList) {
                    try {
                        logicClass.onActivityStarted(activity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                for (BaseActivityLifeLogic logicClass : logicActivityLifeClassList) {
                    try {
                        logicClass.onActivityResumed(activity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                for (BaseActivityLifeLogic logicClass : logicActivityLifeClassList) {
                    try {
                        logicClass.onActivityPaused(activity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                for (BaseActivityLifeLogic logicClass : logicActivityLifeClassList) {
                    try {
                        logicClass.onActivityStopped(activity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                for (BaseActivityLifeLogic logicClass : logicActivityLifeClassList) {
                    try {
                        logicClass.onActivitySaveInstanceState(activity, outState);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                for (BaseActivityLifeLogic logicClass : logicActivityLifeClassList) {
                    try {
                        logicClass.onActivityDestroyed(activity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public Context getApplication() {
        return mContext;
    }
}
