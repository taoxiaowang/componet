package com.hikcreate.library.plugin.netbase.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hikcreate.library.plugin.netbase.NetConstant;
import com.hikcreate.library.util.LogCat;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 使用Interceptor解决Retrofit 多个BaseUrl的问题
 * https://www.jianshu.com/p/ee1ca377386d
 *
 * @author yslei
 * @date 2019/5/28
 * @email leiyongsheng@hikcreate.com
 */
public class MultiDomainInterceptor implements Interceptor {
    private static final String TAG = "MultiDomainInterceptor";

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        if (isIntercept(chain)) {
            return chain.proceed(processRequest(chain.request()));
        } else {
            return chain.proceed(chain.request());
        }
    }

    /**
     * @param request {@link Request}
     * @return {@link Request}
     */
    public Request processRequest(@NonNull Request request) {
        if (request.headers(NetConstant.MultiDomain.BASE_URL).size() > 0) {
            String baseUrl = getBaseUrlFromHeaders(request);
            LogCat.d(TAG, request);
            if (!TextUtils.isEmpty(baseUrl)) {
                Request.Builder requestBuilder = request.newBuilder();
                requestBuilder.removeHeader(NetConstant.MultiDomain.BASE_URL);

                HttpUrl domainUrl = HttpUrl.parse(baseUrl);
                HttpUrl.Builder builder = request.url().newBuilder();
                HttpUrl parsedHttpUrl = builder
                        .scheme(domainUrl.scheme())
                        .host(domainUrl.host())
                        .port(domainUrl.port())
                        .build();

                return requestBuilder.url(parsedHttpUrl).build();
            }
        }
        return request;
    }

    /**
     * get BaseUrl from header
     *
     * @param request
     * @return
     */
    private String getBaseUrlFromHeaders(Request request) {
        List<String> headers = request.headers(NetConstant.MultiDomain.BASE_URL);
        if (headers == null || headers.size() == 0)
            return null;
        if (headers.size() > 1)
            throw new IllegalArgumentException("Only one BaseUrl in the headers");
        return request.header(NetConstant.MultiDomain.BASE_URL);
    }

    private boolean isIntercept(Interceptor.Chain chain) throws IOException {
        return true;
    }
}
