package com.hikcreate.library.plugin.netbase.interceptor;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hikcreate.library.plugin.netbase.NetConstant;
import com.hikcreate.library.util.LogCat;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * net request kick out interceptor
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class SSOInterceptor implements Interceptor {
    private final String TAG = "SSOInterceptor";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static KickOutListener mKickOutListener;

    public interface KickOutListener {
        void onKickOut();
    }

    public static void setKickoutListener(KickOutListener kickoutListener) {
        mKickOutListener = kickoutListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        if (!HttpHeaders.hasBody(response)) {
            //END HTTP
        } else if (bodyEncoded(response.headers())) {
            //HTTP (encoded body omitted)
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    //Couldn't decode the response body; charset is likely malformed.
                    return response;
                }
            }
            if (!isPlaintext(buffer)) {
                return response;
            }

            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);
                handleSSO(request, result, response);
            }

        }
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private void handleSSO(Request request, String result, Response response) {
        String msg = "request url=" + request.url() + ", result=" + result;
        LogCat.v(TAG, msg);
        if (TextUtils.isEmpty(result)) {
            return;
        }
        try {
            JsonObject jObj = new JsonParser().parse(result).getAsJsonObject();
            int code = jObj.has("code") ? jObj.get("code").getAsInt() : 0;
            if (code == NetConstant.ResponseCode.CODE_KICK_OUT) {
                //Close the response
                response.close();
                if (null != mKickOutListener) {
                    mKickOutListener.onKickOut();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogCat.e(TAG, msg, e);
        }
    }
}

