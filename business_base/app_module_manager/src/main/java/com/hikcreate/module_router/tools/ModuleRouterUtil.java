package com.hikcreate.module_router.tools;

import android.annotation.SuppressLint;
import android.content.Context;

import com.hikcreate.module_router.LocalRouter;
import com.hikcreate.module_router.ModuleActionResult;
import com.hikcreate.module_router.router.RouterRequest;
import com.hikcreate.module_router.router.RouterResponse;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author : taowang
 * date :2019/9/25
 * description:
 **/
public class ModuleRouterUtil {

    public static RouterResponse router(Context context, String provider, String action) {
        return LocalRouter.getInstance(context).
                route(RouterRequest.obtain(context).
                        provider(provider).action(action));
    }

    public static RouterResponse router(Context context, String provider, String action,
                                                   HashMap<String, String> value) {
        return LocalRouter.getInstance(context).
                route(RouterRequest.obtain(context).
                provider(provider).data(value).action(action));
    }

    @SuppressLint("CheckResult")
    public static void routeSyncToUi(Context context, String provider, String action, RouterCallBack callBack) {
        Observable.create((ObservableOnSubscribe<RouterResponse>) emitter -> {
            emitter.onNext(LocalRouter.getInstance(context).
                    route(RouterRequest.obtain(context).
                            provider(provider).action(action)));
        }).compose(applySchedulers()).subscribe(routerResponse -> {
             if(routerResponse.getCode() == ModuleActionResult.CODE_SUCCESS){
                 callBack.callSuccess(routerResponse);
             }else {
                 callBack.callFail(routerResponse);
             }
        });
    }
    @SuppressLint("CheckResult")
    public static void routeSyncToUi(Context context, String provider, String action,
                                     HashMap<String, String> value, RouterCallBack callBack) {
        Observable.create((ObservableOnSubscribe<RouterResponse>) emitter -> {
            emitter.onNext(LocalRouter.getInstance(context).
                    route(RouterRequest.obtain(context).data(value).
                            provider(provider).action(action)));
        }).compose(applySchedulers()).subscribe(routerResponse -> {
            if(routerResponse.getCode() == ModuleActionResult.CODE_SUCCESS){
                callBack.callSuccess(routerResponse);
            }else {
                callBack.callFail(routerResponse);
            }
        });

    }

    @SuppressLint("CheckResult")
    public static void routerSyncNewThread(Context context, String provider, String action, RouterCallBack callBack) {
        Observable.create((ObservableOnSubscribe<RouterResponse>) emitter -> {
            emitter.onNext(LocalRouter.getInstance(context).
                    route(RouterRequest.obtain(context).
                            provider(provider).action(action)));
        }).compose(applyNewThreadSchedulers()).subscribe(routerResponse -> {
            if(routerResponse.getCode() == ModuleActionResult.CODE_SUCCESS){
                callBack.callSuccess(routerResponse);
            }else {
                callBack.callFail(routerResponse);
            }
        });
    }

    @SuppressLint("CheckResult")
    public static void routerSyncNewThread(Context context, String provider, String action,
                                  HashMap<String, String> value, RouterCallBack callBack) {
        Observable.create((ObservableOnSubscribe<RouterResponse>) emitter -> {
            emitter.onNext(LocalRouter.getInstance(context).
                    route(RouterRequest.obtain(context).
                            provider(provider).action(action)));
        }).compose(applyNewThreadSchedulers()).subscribe(routerResponse -> {
            if(routerResponse.getCode() == ModuleActionResult.CODE_SUCCESS){
                callBack.callSuccess(routerResponse);
            }else {
                callBack.callFail(routerResponse);
            }
        });
    }
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> applyNewThreadSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }
}
