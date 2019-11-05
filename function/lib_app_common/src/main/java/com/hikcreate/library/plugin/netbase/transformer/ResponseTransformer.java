package com.hikcreate.library.plugin.netbase.transformer;

import com.hikcreate.library.plugin.netbase.NetConstant;
import com.hikcreate.library.plugin.netbase.entity.ResponseResult;
import com.hikcreate.library.plugin.netbase.exception.ApiException;
import com.hikcreate.library.plugin.netbase.exception.CustomExceptionEngine;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * to do
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class ResponseTransformer {

    /**
     * 统一异常处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<ResponseResult<T>, ResponseResult<T>> handleResult() {
        return handleResult(new ResponseFunction<T>());
    }

    public static ObservableTransformer<ResponseBody, ResponseBody> handleBlockResult() {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorBlockResumeFunction());
    }

    public static <T> ObservableTransformer<ResponseResult<T>, ResponseResult<T>> handleResult(Function<ResponseResult<T>, ObservableSource<ResponseResult<T>>> serviceResponseException) {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<>())
                .flatMap(serviceResponseException);
    }

    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    public static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends ResponseResult<T>>> {

        @Override
        public ObservableSource<? extends ResponseResult<T>> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomExceptionEngine.handleException(throwable));
        }
    }

    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     */
    public static class ErrorBlockResumeFunction implements Function<Throwable, ObservableSource<? extends ResponseBody>> {

        @Override
        public ObservableSource<? extends ResponseBody> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomExceptionEngine.handleException(throwable));
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<ResponseResult<T>, ObservableSource<ResponseResult<T>>> {

        @Override
        public ObservableSource<ResponseResult<T>> apply(ResponseResult<T> tResponse) throws Exception {
            int code = tResponse.getCode();
            String message = tResponse.getMsg();
            if (code == NetConstant.ResponseCode.CODE_RESPONSE_SUCCESS
                    || tResponse.isSuccess()) {
                return Observable.just(tResponse);
            } else {
                return Observable.error(new ApiException(code, message));
            }
        }
    }
}
