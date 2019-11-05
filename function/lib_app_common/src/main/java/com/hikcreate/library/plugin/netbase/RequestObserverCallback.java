package com.hikcreate.library.plugin.netbase;

import com.hikcreate.library.plugin.netbase.exception.ApiException;
import com.hikcreate.library.plugin.netbase.exception.CustomerError;
import com.hikcreate.library.util.ClassTypeEntity;
import com.hikcreate.library.util.GenericsUtils;
import com.hikcreate.library.util.LogCat;

import io.reactivex.observers.DisposableObserver;

/**
 * 用于统一控制Rx网络请求回调
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public abstract class RequestObserverCallback<T> extends DisposableObserver<T> {

    public static final int DEFAULT_REQUEST_TAG = -1001;
    private Class mClazzType;
    private ClassTypeEntity mClassTypeEntity;
    private int mTag;

    public RequestObserverCallback() {
        this(DEFAULT_REQUEST_TAG);
    }

    public RequestObserverCallback(int tag) {
        mTag = tag;
        mClassTypeEntity = new ClassTypeEntity();
        mClazzType = GenericsUtils.getSuperClassGenericType(getClass(), mClassTypeEntity);
    }

    public int getTag() {
        return mTag;
    }

    public Class getClazzType() {
        return mClazzType;
    }

    public ClassTypeEntity getClassTypeEntity() {
        return mClassTypeEntity;
    }

    public abstract void loadStart();

    @Override
    public void onStart() {
        super.onStart();
        LogCat.d(NetConstant.TAG, "request start");
        loadStart();
    }

    @Override
    public void onComplete() {
        if (!this.isDisposed()) this.dispose();
        ARemoteDSRequestManager.clearDisposable(this);
        LogCat.d(NetConstant.TAG, "result finish");
        loadFinish();
    }

    public abstract void loadFinish();

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (!this.isDisposed()) this.dispose();
        ARemoteDSRequestManager.clearDisposable(this);
        LogCat.d(NetConstant.TAG, "get error:" + e.getMessage());
        if (e instanceof ApiException) {
            loadError((ApiException) e);
        } else {
            loadError(new ApiException(e, CustomerError.UNKNOWN));
        }
    }

    public abstract void loadError(ApiException ex);

    @Override
    public void onNext(T t) {
        LogCat.d(NetConstant.TAG, "result data:" + t);
        loadData(t);
    }

    public abstract void loadData(T t);
}
