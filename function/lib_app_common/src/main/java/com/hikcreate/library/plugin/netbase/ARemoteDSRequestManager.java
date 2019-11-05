package com.hikcreate.library.plugin.netbase;

import android.util.SparseArray;

import com.hikcreate.library.plugin.netbase.entity.CustomerCompositeDisposable;
import com.hikcreate.library.plugin.netbase.linstener.IDataSourceDataClear;
import com.hikcreate.library.plugin.netbase.linstener.IRemoteRequestDisposable;
import com.hikcreate.library.util.LogCat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * 远端请求管理
 *
 * @author yslei
 * @date 2019/8/15
 * @email leiyongsheng@hikcreate.com
 */
public class ARemoteDSRequestManager implements IDataSourceDataClear, IRemoteRequestDisposable {

    private static volatile List<ARemoteDSRequestManager> sARemoteDSRequestManagers = Collections.synchronizedList(new ArrayList());
    ;
    private static volatile ARemoteDSRequestManager INSTANCE;

    private static final int DEFAULT_CACHE_COUNT = 10;
    private static final String TAG = "RDSClear";

    private String mTag = TAG;
    private SparseArray<CustomerCompositeDisposable> mDisposableSparseArray;
    private int mCacheDisposableCount;

    public static ARemoteDSRequestManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ARemoteDSRequestManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ARemoteDSRequestManager();
                    sARemoteDSRequestManagers.add(INSTANCE);
                }
            }
        }
        return INSTANCE;
    }

    public static ARemoteDSRequestManager newInstance(String tagName) {
        return newInstance(tagName, DEFAULT_CACHE_COUNT);
    }

    public static ARemoteDSRequestManager newInstance(String tagName, int cacheCount) {
        ARemoteDSRequestManager requestManager = new ARemoteDSRequestManager();
        requestManager.setCacheDisposableCount(cacheCount);
        requestManager.setTag(tagName + "-" + TAG);
        sARemoteDSRequestManagers.add(requestManager);
        return requestManager;
    }

    public static void clearDisposable(Disposable disposable) {
        for (ARemoteDSRequestManager requestManager : sARemoteDSRequestManagers) {
            requestManager.deleteDisposable(disposable);
        }
    }

    public ARemoteDSRequestManager() {
        mDisposableSparseArray = new SparseArray<>();
        setCacheDisposableCount(DEFAULT_CACHE_COUNT);
    }

    public int getCacheDisposableCount() {
        return mCacheDisposableCount;
    }

    public void setCacheDisposableCount(int cacheDisposableCount) {
        mCacheDisposableCount = cacheDisposableCount;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    @Override
    public synchronized void clear(int tag) {
        LogCat.d(mTag, "remote data source clear tag:" + tag);
        CustomerCompositeDisposable compositeDisposable = mDisposableSparseArray.get(tag);
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            mDisposableSparseArray.remove(tag);
        }
    }

    @Override
    public synchronized void clearAll() {
        LogCat.d(mTag, "remote data source clear all");
        for (int i = 0; i < mDisposableSparseArray.size(); i++) {
            CustomerCompositeDisposable compositeDisposable = mDisposableSparseArray.valueAt(i);
            compositeDisposable.clear();
        }
        mDisposableSparseArray.clear();
    }

    @Override
    public synchronized boolean isAllDisposed() {
        for (int i = 0; i < mDisposableSparseArray.size(); i++) {
            CustomerCompositeDisposable compositeDisposable = mDisposableSparseArray.valueAt(i);
            if (!compositeDisposable.isDisposed()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized boolean isTagDisposed(int tag) {
        if (mDisposableSparseArray.get(tag) != null) {
            return mDisposableSparseArray.get(tag).isDisposed();
        } else {
            return true;
        }
    }

    @Override
    public synchronized <D> DisposableObserver<D> addDisposable(RequestObserverCallback<D> d) {
        DisposableObserver observer;
        CustomerCompositeDisposable compositeDisposable;
        int tag;
        if (d == null) {
            observer = new DisposableObserver() {
                @Override
                public void onNext(Object value) {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            };
            tag = RequestObserverCallback.DEFAULT_REQUEST_TAG;
        } else {
            observer = d;
            tag = d.getTag();
        }
        compositeDisposable = mDisposableSparseArray.get(tag);
        if (compositeDisposable != null) {
            compositeDisposable.add(observer);
        } else {
            compositeDisposable = new CustomerCompositeDisposable();
            compositeDisposable.add(observer);
            mDisposableSparseArray.put(tag, compositeDisposable);
        }
        resetCompositeDisposable();
        return observer;
    }

    private void resetCompositeDisposable() {
        int total = statisticsTotalDisposable();
        LogCat.d(mTag, "start to reset dispose, current total disposable :" + total);
        if (total > mCacheDisposableCount) {
            LogCat.d(mTag, " start to clear disposable size：" + mDisposableSparseArray.size());
            CustomerCompositeDisposable compositeDisposable;
            for (int i = 0; i < mDisposableSparseArray.size(); i++) {
                compositeDisposable = mDisposableSparseArray.valueAt(i);
                if (compositeDisposable.isCurrentAllDisposed()) {
                    mDisposableSparseArray.removeAt(i);
                }
            }
            LogCat.d(mTag, "end to clear disposable size：" + mDisposableSparseArray.size());
        }
        total = statisticsTotalDisposable();
        LogCat.d(mTag, "total leave disposable :" + total);
    }

    private int statisticsTotalDisposable() {
        int total = 0;
        CustomerCompositeDisposable compositeDisposable;
        for (int i = 0; i < mDisposableSparseArray.size(); i++) {
            compositeDisposable = mDisposableSparseArray.valueAt(i);
            if (compositeDisposable != null) {
                total += compositeDisposable.size();
            }
        }
        return total;
    }

    @Override
    public synchronized boolean removeDisposable(Disposable d) {
        CustomerCompositeDisposable compositeDisposable = null;
        int index = -1;
        for (int i = 0; i < mDisposableSparseArray.size(); i++) {
            compositeDisposable = mDisposableSparseArray.valueAt(i);
            if (compositeDisposable.remove(d)) {
                index = i;
                break;
            }
        }
        if (index > 0) {
            if (compositeDisposable.size() == 0) {
                mDisposableSparseArray.removeAt(index);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean deleteDisposable(Disposable d) {
        CustomerCompositeDisposable compositeDisposable = null;
        int index = -1;
        for (int i = 0; i < mDisposableSparseArray.size(); i++) {
            compositeDisposable = mDisposableSparseArray.valueAt(i);
            if (compositeDisposable.delete(d)) {
                index = i;
                break;
            }
        }
        if (index > 0) {
            if (compositeDisposable.size() == 0) {
                mDisposableSparseArray.removeAt(index);
            }
            return true;
        } else {
            return false;
        }
    }
}
