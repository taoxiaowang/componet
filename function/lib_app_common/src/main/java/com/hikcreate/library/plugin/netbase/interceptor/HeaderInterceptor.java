package com.hikcreate.library.plugin.netbase.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 通用header拦截器
 *
 * @author yslei
 * @data 2019/3/13
 * @email leiyongsheng@hikcreate.com
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .header("mimeType", "application/json")
                .method(original.method(), original.body());
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
