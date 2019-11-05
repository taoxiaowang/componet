package com.hikcreate.library.plugin.netbase.transformer;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 网络响应body转换器接口
 *
 * @author yslei
 * @data 2019/5/5
 * @email leiyongsheng@hikcreate.com
 */
public interface IGsonResponseBodyConverter<T> extends Converter<ResponseBody, T> {

    Converter<ResponseBody, ?> initResponseBodyConverter(Gson gson, TypeAdapter<? extends T> adapter);
}
