package com.hikcreate.library.plugin.netbase.scheduler;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;

/**
 * to do
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public interface BaseSchedulerProvider {

    // 用于科学计算
    Scheduler computation();

    // 用于io读写
    Scheduler io();

    // 用于ui渲染
    Scheduler ui();

    // 用于其他任务
    Scheduler newThread();
}
