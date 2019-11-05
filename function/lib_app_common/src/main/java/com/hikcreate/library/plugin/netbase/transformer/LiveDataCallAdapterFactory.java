package com.hikcreate.library.plugin.netbase.transformer;

import android.arch.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import retrofit2.CallAdapter;
import retrofit2.CallAdapter.Factory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * LiveData转换器
 *
 * @author yslei
 * @date 2019/9/11
 * @email leiyongsheng@hikcreate.com
 */
public class LiveDataCallAdapterFactory extends Factory {

    /**
     * Returns an instance which creates Live data that do not operate on any scheduler
     * by default.
     */
    public static LiveDataCallAdapterFactory create() {
        return new LiveDataCallAdapterFactory();
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (Factory.getRawType(returnType) != LiveData.class) {
            throw new IllegalStateException("return type must be parameterized");
        }
        Type observableType = Factory.getParameterUpperBound(0, (ParameterizedType) returnType);
        Class rawObservableType = Factory.getRawType(observableType);
        Type responseType;
        if (rawObservableType == Response.class) {
            if (observableType instanceof ParameterizedType) {
                throw new IllegalArgumentException("Response must be parameterized");
            }
            responseType = Factory.getParameterUpperBound(0, (ParameterizedType) observableType);
        } else {
            responseType = observableType;
        }
        return new LiveDataCallAdapter<>(responseType);
    }
}
