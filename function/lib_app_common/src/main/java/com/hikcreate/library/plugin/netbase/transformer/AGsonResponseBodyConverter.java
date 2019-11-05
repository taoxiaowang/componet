package com.hikcreate.library.plugin.netbase.transformer;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 对外通用GSON相应response body转换器
 *
 * @author yslei
 * @data 2019/5/5
 * @email leiyongsheng@hikcreate.com
 */
public abstract class AGsonResponseBodyConverter<T> extends BaseGsonBodyConverter<T> implements IGsonResponseBodyConverter<T> {

    @Override
    public Converter<ResponseBody, ?> initResponseBodyConverter(Gson gson, TypeAdapter<? extends T> adapter) {
        setAdapter((TypeAdapter<T>) adapter);
        setGson(gson);
        return this;
    }
}
