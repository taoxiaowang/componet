package com.hikcreate.library.plugin.netbase.transformer;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * 网络请求body转换器接口
 *
 * @author yslei
 * @data 2019/5/5
 * @email leiyongsheng@hikcreate.com
 */
public interface IGsonRequestBodyConverter<T> extends Converter<T, RequestBody> {

    Converter<?, RequestBody> initRequestBodyConverter(Gson gson, TypeAdapter<? extends T> adapter);

}
