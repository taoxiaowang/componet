package com.hikcreate.library.plugin.netbase.interceptor;

import com.hikcreate.library.plugin.netbase.NetConstant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp3 连接，读写超时拦截器
 *
 * @author yslei
 * @data 2019/3/15
 * @email leiyongsheng@hikcreate.com
 */
public class TimeHeaderInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        int connectTimeout = chain.connectTimeoutMillis();
        int readTimeout = chain.readTimeoutMillis();
        int writeTimeout = chain.writeTimeoutMillis();

        Request.Builder builder = request.newBuilder();

        int newConnectTimeout = getHeaderTimeOut(request, builder, NetConstant.RequestTimeOut.CONNECT_TIMEOUT);
        int newReadTimeout = getHeaderTimeOut(request, builder, NetConstant.RequestTimeOut.READ_TIMEOUT);
        int newWriteTimeout = getHeaderTimeOut(request, builder, NetConstant.RequestTimeOut.WRITE_TIMEOUT);

        if (newConnectTimeout > 0 || newReadTimeout > 0 || newWriteTimeout > 0) {
            connectTimeout = newConnectTimeout > 0 ? newConnectTimeout : connectTimeout;
            readTimeout = newReadTimeout > 0 ? newReadTimeout : readTimeout;
            writeTimeout = newWriteTimeout > 0 ? newWriteTimeout : writeTimeout;

            chain.withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
            chain.withReadTimeout(readTimeout, TimeUnit.MILLISECONDS);
            chain.withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS);

            return chain.proceed(builder.build());
        } else {
            return chain.proceed(request);
        }
    }

    private int getHeaderTimeOut(Request request, Request.Builder builder, String timeOutType) {
        int timeOut = -1;
        if (request.header(timeOutType) != null) {
            timeOut = Integer.parseInt(request.header(timeOutType));
            builder.removeHeader(timeOutType);
        }
        return timeOut;
    }
}
