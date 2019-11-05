package com.hikcreate.library.plugin.otto;

import android.app.Activity;

import com.squareup.otto.Bus;

import com.hikcreate.library.app.AppLifecycle;

/**
 * OTTO全局唯一BUS实例
 *
 * @author gongwei 2018/12/18
 */
public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    public static void post(Event event) {
        getInstance().post(event);
    }

    public static void register(Object object) {
        getInstance().register(object);
    }

    public static void bindLifecycle(final Object object) {
        getInstance().register(object);
        AppLifecycle.setLifecycle(object, new AppLifecycle.LifecycleCallback() {
            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                getInstance().unregister(object);
            }
        });
    }

    public static void unregister(Object object) {
        getInstance().unregister(object);
    }

    private BusProvider() {
        // No instances.
    }
}
