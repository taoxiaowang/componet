package com.hikcreate.module_router;


import android.content.Context;
import android.support.annotation.NonNull;

import com.hikcreate.module_router.router.RouterRequest;
import com.hikcreate.module_router.router.RouterResponse;
import com.hikcreate.module_router.tools.Logger;
import com.hikcreate.module_router.tools.ProcessUtil;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author wangtao55
 * @date 2019/9/19
 * @mail wangtao55@hikcreate.com
 */

public class LocalRouter {
    private static final String TAG = "LocalRouter";
    private String mProcessName = ProcessUtil.UNKNOWN_PROCESS_NAME;
    private static LocalRouter sInstance = null;
    private HashMap<String, ModuleProvider> mProviders = null;
    private static ExecutorService threadPool = null;
    private Context mContext;


    private LocalRouter(Context context) {
        mContext = context;
        mProcessName = ProcessUtil.getProcessName(context, ProcessUtil.getMyProcessId());
        mProviders = new HashMap<>();
    }

    public static synchronized LocalRouter getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new LocalRouter(context);
        }
        return sInstance;
    }

    private static synchronized ExecutorService getThreadPool() {
        if (null == threadPool) {
            threadPool = Executors.newCachedThreadPool();
        }
        return threadPool;
    }

    public void registerProvider(String providerName, ModuleProvider provider) {
        provider.registerActions();
        mProviders.put(providerName, provider);
    }


    boolean answerWiderAsync(@NonNull RouterRequest routerRequest) {
        if (mProcessName.equals(routerRequest.getDomain())) {
            return findRequestAction(routerRequest).isAsync(routerRequest.getData());
        } else {
            return true;
        }
    }

    public RouterResponse route(@NonNull RouterRequest routerRequest) {

        Logger.d(TAG, "Process:" + mProcessName + "\nLocal route start: " + System.currentTimeMillis());
        RouterResponse routerResponse = new RouterResponse();

        // Local request
        if (mProcessName.equals(routerRequest.getDomain())) {
            Object attachment = routerRequest.getAndClearObject();
            HashMap<String, String> params = new HashMap<>(routerRequest.getData());
            Logger.d(TAG, "Process:" + mProcessName + "\nLocal find action start: " + System.currentTimeMillis());
            ModuleAction targetAction = findRequestAction(routerRequest);
            routerRequest.isIdle.set(true);
            Logger.d(TAG, "Process:" + mProcessName + "\nLocal find action end: " + System.currentTimeMillis());
            routerResponse.mIsAsync = attachment == null ? targetAction.isAsync(params) :
                    targetAction.isAsync(params, attachment);
            // Sync result, return the result immediately.
            if (!routerResponse.mIsAsync) {
                ModuleActionResult result = attachment == null ? targetAction.invoke(mContext, params) :
                        targetAction.invoke(mContext, params, attachment);
                routerResponse.mResultString = result.toString();
                routerResponse.mObject = result.getObject();
                Logger.d(TAG, "Process:" + mProcessName + "\nLocal sync end: " + System.currentTimeMillis());
            }
            // Async result, use the thread pool to execute the task.
            else {
                LocalTask task = new LocalTask(routerResponse, params, attachment, mContext, targetAction);
                routerResponse.mAsyncResponse = getThreadPool().submit(task);
            }
        }
        return routerResponse;
    }


    private ModuleAction findRequestAction(RouterRequest routerRequest) {

        ModuleProvider targetProvider = mProviders.get(routerRequest.getProvider());
        ErrorAction defaultNotFoundAction = new ErrorAction(false,
                ModuleActionResult.CODE_NOT_FOUND, "Not found the action.");
        if (null == targetProvider) {
            return defaultNotFoundAction;
        } else {
            ModuleAction targetAction = targetProvider.findAction(routerRequest.getAction());
            if (null == targetAction) {
                return defaultNotFoundAction;
            } else {
                return targetAction;
            }
        }
    }

    private class LocalTask implements Callable<String> {
        private RouterResponse mResponse;
        private HashMap<String, String> mRequestData;
        private Context mContext;
        private ModuleAction mAction;
        private Object mObject;

        LocalTask(RouterResponse routerResponse, HashMap<String, String>
                requestData, Object object, Context context, ModuleAction maAction) {
            this.mContext = context;
            this.mResponse = routerResponse;
            this.mRequestData = requestData;
            this.mAction = maAction;
            this.mObject = object;
        }

        @Override
        public String call() {
            try {
                ModuleActionResult result = mObject == null ? mAction.invoke(mContext, mRequestData)
                        : mAction.invoke(mContext, mRequestData, mObject);
                mResponse.mObject = result.getObject();
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "error";
        }
    }

}
