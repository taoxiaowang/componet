package com.hikcreate.library.plugin.netbase.scheduler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 用于网路请求线程切换帮助类
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class SchedulerProvider implements BaseSchedulerProvider {

    @Nullable
    private static SchedulerProvider INSTANCE;

    // Prevent direct instantiation.
    private SchedulerProvider() {
    }

    public static synchronized SchedulerProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SchedulerProvider();
        }
        return INSTANCE;
    }

    @Override
    @NonNull
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    @NonNull
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    @NonNull
    public Scheduler newThread() {
        return Schedulers.newThread();
    }

    @Override
    @NonNull
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * 网络请求，UI数据呈现通用Scheduler，结果在UI线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().ui());
    }

    /**
     * 网络请求，数据计算呈现通用Scheduler，结果在computation线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applyComputeSchedulers() {
        return observable -> observable.subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().computation());
    }

    /**
     * 网络请求，数据处理呈现通用Scheduler，结果在newThread线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applyNewThreadSchedulers() {
        return observable -> observable.subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().newThread());
    }
}
