package com.hikcreate.library.plugin.netbase;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 网络请求服务生成器
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class ServiceGenerator {

    private static Retrofit.Builder mRetrofitBuilder = null;

    public static <S> S createService(Class<S> serviceClass, String url) {
        if (mRetrofitBuilder == null || RetrofitManager.getInstance().isHasRebuild()) {
            RetrofitManager.getInstance().setHasRebuild(false);
            mRetrofitBuilder = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(RetrofitManager.getInstance().generatorFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            //.addCallAdapterFactory(LiveDataCallAdapterFactory.create());

            if (RetrofitManager.getInstance().getInterceptorList() != null && RetrofitManager.getInstance().getInterceptorList().size() > 0) {
                for (Interceptor interceptor : RetrofitManager.getInstance().getInterceptorList()) {
                    RetrofitManager.getInstance().getHttpClientBuilder().addInterceptor(interceptor);
                }
            }
        }
        mRetrofitBuilder.baseUrl(url);

        RetrofitManager.getInstance().getHttpClientBuilder().retryOnConnectionFailure(true);
        OkHttpClient client = RetrofitManager.getInstance().getHttpClientBuilder().build();
        Retrofit retrofit = mRetrofitBuilder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
