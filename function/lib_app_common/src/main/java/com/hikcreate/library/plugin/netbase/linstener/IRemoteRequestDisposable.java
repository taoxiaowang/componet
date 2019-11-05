package com.hikcreate.library.plugin.netbase.linstener;

import com.hikcreate.library.plugin.netbase.RequestObserverCallback;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * 用于请求释放
 *
 * @author yslei
 * @data 2019/3/18
 * @email leiyongsheng@hikcreate.com
 */
public interface IRemoteRequestDisposable {
    /**
     * 所有请求是否释放
     */
    boolean isAllDisposed();

    /**
     * 当前tag是否释放
     *
     * @param tag
     * @return
     */
    boolean isTagDisposed(int tag);

    /**
     * 添加Disposable对象
     *
     * @param d   请求Disposable
     * @param <D> 请求Disposable的类型
     * @return
     */
    <D> DisposableObserver<D> addDisposable(RequestObserverCallback<D> d);

    /**
     * 移除Disposable,移除并释放
     *
     * @param d 请求Disposable
     * @return
     */
    boolean removeDisposable(Disposable d);

    /**
     * 移除Disposable，不释放
     *
     * @param d 请求Disposable
     * @return
     */
    boolean deleteDisposable(Disposable d);
}
