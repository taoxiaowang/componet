package com.hikcreate.library.plugin.netbase.transformer;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import okhttp3.RequestBody;
import okio.Buffer;

/**
 * 默认网络请求body转换器
 *
 * @author yslei
 * @data 2019/5/5
 * @email leiyongsheng@hikcreate.com
 */
public class MineGsonRequestBodyConverter<T> extends AGsonRequestBodyConverter<T> {

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = getGson().newJsonWriter(writer);
        getAdapter().write(jsonWriter, value);
        jsonWriter.close();
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }
}
