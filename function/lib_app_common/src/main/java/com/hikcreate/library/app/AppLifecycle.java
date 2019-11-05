package com.hikcreate.library.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.studio.os.LogCat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * 管理各组件生命周期
 *
 * @author gongwei 2018/12/19
 */
public abstract class AppLifecycle {

    public static void setLifecycle(Object context, LifecycleCallback callback) {
        if (context instanceof FragmentActivity) {
            setLifecycle((FragmentActivity) context, callback);
        } else if (context instanceof Activity) {
            setLifecycle((Activity) context, callback);
        } else if (context instanceof Fragment) {
            setLifecycle((Fragment) context, callback);
        } else if (context instanceof android.app.Fragment) {
            setLifecycle((android.app.Fragment) context, callback);
        } else {
            //throw new IllegalArgumentException("context is: " + context);
            LogCat.e("IllegalArgumentException: context is: " + context);
        }
    }

    public static void setLifecycle(Activity activity, LifecycleCallback callback) {
        FragmentLifecycle fragment = new FragmentLifecycle();
        fragment.setCallback(callback);
        activity.getFragmentManager()
                .beginTransaction()
                .add(fragment, "_no_ui")
                //.commit();
                .commitAllowingStateLoss();
    }

    public static void setLifecycle(FragmentActivity activity, LifecycleCallback callback) {
        FragmentV4Lifecycle fragment = new FragmentV4Lifecycle();
        fragment.setCallback(callback);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(fragment, "_no_ui")
                //.commit();
                .commitAllowingStateLoss();
    }

    public static void setLifecycle(Fragment fragment, LifecycleCallback callback) {
        FragmentV4Lifecycle fragmentLifecycle = new FragmentV4Lifecycle();
        fragmentLifecycle.setCallback(callback);
        fragment.getChildFragmentManager()
                .beginTransaction()
                .add(fragmentLifecycle, "_no_ui")
                //.commit();
                .commitAllowingStateLoss();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void setLifecycle(android.app.Fragment fragment, LifecycleCallback callback) {
        FragmentLifecycle fragmentLifecycle = new FragmentLifecycle();
        fragmentLifecycle.setCallback(callback);
        fragment.getChildFragmentManager()
                .beginTransaction()
                .add(fragmentLifecycle, "_no_ui")
                //.commit();
                .commitAllowingStateLoss();
    }

    static public class FragmentV4Lifecycle extends Fragment {

        LifecycleCallback callback;

        public void setCallback(LifecycleCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            if (callback != null) {
                callback.onActivityCreated(getActivity(), savedInstanceState);
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (callback != null) {
                callback.onActivityResult(requestCode, resultCode, data);
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            if (callback != null) {
                callback.onActivityStarted(getActivity());
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            if (callback != null) {
                callback.onActivityResumed(getActivity());
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            if (callback != null) {
                callback.onActivityPaused(getActivity());
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            if (callback != null) {
                callback.onActivityStopped(getActivity());
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            if (callback != null) {
                callback.onActivityDestroyed(getActivity());
            }
        }
    }


    static public class FragmentLifecycle extends android.app.Fragment {

        LifecycleCallback callback;

        public void setCallback(LifecycleCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            if (callback != null) {
                callback.onActivityCreated(getActivity(), savedInstanceState);
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (callback != null) {
                callback.onActivityResult(requestCode, resultCode, data);
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            if (callback != null) {
                callback.onActivityStarted(getActivity());
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            if (callback != null) {
                callback.onActivityResumed(getActivity());
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            if (callback != null) {
                callback.onActivityPaused(getActivity());
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            if (callback != null) {
                callback.onActivityStopped(getActivity());
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            if (callback != null) {
                callback.onActivityDestroyed(getActivity());
            }
        }
    }

    static public class LifecycleCallback implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

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
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {

        }
    }

}
