package com.hikcreate.baidutextdect;

import android.app.Activity;
import android.content.Intent;

/**
 * to do
 *
 * @author yslei
 * @data 2019/4/2
 * @email leiyongsheng@hikcreate.com
 */
public interface ITextDectManager {

    interface TextDectInitCallback {
        void onInitSuccess(String token);

        void onInitError(String message);
    }

    interface TextDectCallback<T> {
        void onResult(T result);

        void onError(String message);
    }

    void initTextDect(Activity activity, TextDectInitCallback callback, TextDectEnum... types);

    void startTextDect(TextDectEnum type, int requestCode);

    void textDetect(TextDectEnum type, Intent data, TextDectCallbackAdapter callback);

    void releaseTextDect();
}
