package com.hikcreate.library.plugin.netbase.transformer;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Gson转换器
 *
 * @author yslei
 * @data 2019/5/5
 * @email leiyongsheng@hikcreate.com
 */
public class MineGsonConverterFactory extends Converter.Factory {

    public static MineGsonConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new MineGsonConverterFactory(gson);
    }

    public static MineGsonConverterFactory create(Gson gson, Class<? extends IGsonRequestBodyConverter> requestBodyConverterClass, Class<? extends IGsonResponseBodyConverter> responseBodyConverterClass) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new MineGsonConverterFactory(gson, requestBodyConverterClass, responseBodyConverterClass);
    }

    private final Gson gson;
    private Class<? extends IGsonRequestBodyConverter> mGsonRequestBodyConverterClass;
    private Class<? extends IGsonResponseBodyConverter> mGsonResponseBodyConverterClass;

    private MineGsonConverterFactory(Gson gson) {
        this(gson, null, null);
    }

    private MineGsonConverterFactory(Gson gson, Class<? extends IGsonRequestBodyConverter> requestBodyConverterClass, Class<? extends IGsonResponseBodyConverter> responseBodyConverterClass) {
        this.gson = gson;
        this.mGsonResponseBodyConverterClass = responseBodyConverterClass;
        this.mGsonRequestBodyConverterClass = requestBodyConverterClass;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        if (mGsonResponseBodyConverterClass != null) {
            try {
                return mGsonResponseBodyConverterClass.newInstance().initResponseBodyConverter(gson, adapter);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return new MineGsonResponseBodyConverter<>().initResponseBodyConverter(gson, adapter);
            } catch (InstantiationException e) {
                e.printStackTrace();
                return new MineGsonResponseBodyConverter<>().initResponseBodyConverter(gson, adapter);
            }
        } else {
            return new MineGsonResponseBodyConverter<>().initResponseBodyConverter(gson, adapter);
        }
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        if (mGsonRequestBodyConverterClass != null) {
            try {
                return mGsonRequestBodyConverterClass.newInstance().initRequestBodyConverter(gson, adapter);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return new MineGsonRequestBodyConverter<>().initRequestBodyConverter(gson, adapter);
            } catch (InstantiationException e) {
                e.printStackTrace();
                return new MineGsonRequestBodyConverter<>().initRequestBodyConverter(gson, adapter);
            }
        } else {
            return new MineGsonRequestBodyConverter<>().initRequestBodyConverter(gson, adapter);
        }
    }
}
