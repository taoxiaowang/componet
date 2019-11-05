package com.hikcreate.library.plugin.netbase.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

/**
 * 统一异常处理引擎
 * 本地产生的统一包装Exception，比如解析错误，网络链接错误等等。
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class CustomExceptionEngine {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, CustomerError.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.setDisplayMessage("网络连接有问题");  //均视为网络错误
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, CustomerError.PARSE_ERROR);
            ex.setDisplayMessage("解析错误");            //均视为解析错误
            return ex;
        } else if (e instanceof ConnectException
                || e instanceof SocketTimeoutException
                || e instanceof UnknownHostException) {
            ex = new ApiException(e, CustomerError.NETWORK_ERROR);
            ex.setDisplayMessage("网络连接有问题");  //均视为网络错误
            return ex;
        } else if (e instanceof CertificateException
                || e instanceof SSLHandshakeException) {
            ex = new ApiException(e, CustomerError.CERTIFICATE_ERROR);
            ex.setDisplayMessage("认证失败");  //网络认证错误
            return ex;
        } else if (e instanceof ApiException) {
            return (ApiException) e;
        } else {
            ex = new ApiException(e, CustomerError.UNKNOWN);
            ex.setDisplayMessage("未知错误");          //未知错误
            return ex;
        }
    }
}
