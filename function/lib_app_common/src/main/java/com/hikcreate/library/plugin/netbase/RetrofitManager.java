package com.hikcreate.library.plugin.netbase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hikcreate.library.plugin.netbase.transformer.AGsonRequestBodyConverter;
import com.hikcreate.library.plugin.netbase.transformer.AGsonResponseBodyConverter;
import com.hikcreate.library.plugin.netbase.transformer.IGsonRequestBodyConverter;
import com.hikcreate.library.plugin.netbase.transformer.IGsonResponseBodyConverter;
import com.hikcreate.library.plugin.netbase.transformer.MineGsonConverterFactory;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * retrofit 统一配置管理器
 *
 * @author yslei
 * @data 2019/5/6
 * @email leiyongsheng@hikcreate.com
 */
public class RetrofitManager {

    private static volatile RetrofitManager INSTANCE;

    public static RetrofitManager getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitManager();
                }
            }
        }
        return INSTANCE;
    }

    private List<Interceptor> mInterceptorList;
    private Class<? extends IGsonResponseBodyConverter> mGsonResponseBodyConverterClass;
    private Class<? extends IGsonRequestBodyConverter> mGsonRequestBodyConverterClass;
    private Gson mGson;
    private OkHttpClient.Builder mHttpClientBuilder;
    private boolean mHasRebuild;

    public RetrofitManager() {
        mGson = new GsonBuilder()
                .setLenient()
                .create();
        mHttpClientBuilder = new OkHttpClient.Builder();
//                .connectTimeout(60, TimeUnit.SECONDS) // 默认10s
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS);
    }

    public boolean isHasRebuild() {
        return mHasRebuild;
    }

    public void setHasRebuild(boolean hasRebuild) {
        mHasRebuild = hasRebuild;
    }

    public Gson getGson() {
        return mGson;
    }

    public RetrofitManager setGson(Gson gson) {
        mGson = gson;
        return this;
    }

    public OkHttpClient.Builder getHttpClientBuilder() {
        return mHttpClientBuilder;
    }

    public RetrofitManager setHttpClientBuilder(OkHttpClient.Builder httpClientBuilder) {
        mHttpClientBuilder = httpClientBuilder;
        return this;
    }

    public List<Interceptor> getInterceptorList() {
        return mInterceptorList;
    }

    public RetrofitManager setInterceptorList(List<Interceptor> interceptorList) {
        mInterceptorList = interceptorList;
        return this;
    }

    public Class<? extends IGsonResponseBodyConverter> getGsonResponseBodyConverterClass() {
        return mGsonResponseBodyConverterClass;
    }

    public RetrofitManager setGsonResponseBodyConverterClass(Class<? extends IGsonResponseBodyConverter> gsonResponseBodyConverterClass) {
        mGsonResponseBodyConverterClass = gsonResponseBodyConverterClass;
        return this;
    }

    public Class<? extends IGsonRequestBodyConverter> getGsonRequestBodyConverterClass() {
        return mGsonRequestBodyConverterClass;
    }

    public RetrofitManager setGsonRequestBodyConverterClass(Class<? extends IGsonRequestBodyConverter> gsonRequestBodyConverterClass) {
        mGsonRequestBodyConverterClass = gsonRequestBodyConverterClass;
        return this;
    }

    public void rebuild() {
        mHasRebuild = true;
    }

    public <S> S generatorService(Class<S> serviceClass, String url) {
        return ServiceGenerator.createService(serviceClass, url);
    }

    public MineGsonConverterFactory generatorFactory() {
        if (mGsonRequestBodyConverterClass == null && mGsonResponseBodyConverterClass == null) {
            return MineGsonConverterFactory.create(mGson);
        } else {
            return MineGsonConverterFactory.create(mGson, mGsonRequestBodyConverterClass, mGsonResponseBodyConverterClass);
        }
    }
}
