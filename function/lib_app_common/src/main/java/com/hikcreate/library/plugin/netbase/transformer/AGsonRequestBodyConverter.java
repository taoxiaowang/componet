package com.hikcreate.library.plugin.netbase.transformer;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * 对外通用GSON相应request body转换器
 *
 * @author yslei
 * @data 2019/5/5
 * @email leiyongsheng@hikcreate.com
 */
public abstract class AGsonRequestBodyConverter<T> extends BaseGsonBodyConverter<T> implements IGsonRequestBodyConverter<T> {

    public Converter<?, RequestBody> initRequestBodyConverter(Gson gson, TypeAdapter<? extends T> adapter) {
        setGson(gson);
        setAdapter((TypeAdapter<T>) adapter);
        return this;
    }

}
