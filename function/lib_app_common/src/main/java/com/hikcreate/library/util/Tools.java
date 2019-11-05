package com.hikcreate.library.util;

import android.studio.plugins.GsonUtils;
import android.view.ViewTreeObserver;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hikcreate.library.plugin.netbase.scheduler.SchedulerProvider;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 常用工具方法
 *
 * @author gongwei
 * @date 2019/3/6
 */
public class Tools {

    /**
     * 判断是否是本地的图片地址
     *
     * @param str
     * @return
     */
    public static boolean isLocalImageUrl(String str) {
        if (str == null) {
            return false;
        }
        File file = new File(str);
        return file.exists();
    }

    /**
     * 判断是否是网络图片地址（不带http前缀）
     *
     * @param str
     * @return
     */
    public static boolean isOnlineImageUrl(String str) {
        if (str == null) {
            return false;
        }
        File file = new File(str);
        return !file.exists() && !URLUtil.isNetworkUrl(str);
    }

    /**
     * TextView的文字是否是否超过行数
     *
     * @param textView
     * @param maxLines
     * @param callBack
     */
    public static void isOverFlowed(final TextView textView, final int maxLines, final TextOverFlowedCallBack callBack) {
        if (callBack == null) return;
        textView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //这个回调会调用多次，获取完行数记得注销监听
                textView.getViewTreeObserver().removeOnPreDrawListener(this);
                int lines = textView.getLineCount();
                LogCat.e("xwm", textView.getText() + "--" + lines);
                if (lines > maxLines) {
                    callBack.overFlowed(true);
                } else {
                    callBack.overFlowed(false);
                }
                return true;
            }
        });
//        ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();
//        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Layout l = textView.getLayout();
//                if (l != null) {
//                    int lines = l.getLineCount();
//                    LogCat.e("xwm", textView.getText() + "--" + lines);
//                    if (lines > maxLines) {
//                        callBack.overFlowed(true);
//                    } else {
//                        callBack.overFlowed(false);
//                    }
//                }
//            }
//        });
    }

    /**
     * Google Gson
     *
     * @param jsonInString
     * @return
     */
    public final static boolean isJsonForGsonValid(String jsonInString) {
        try {
            new Gson().fromJson(jsonInString, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }

    /**
     * 比较两个对象是否相等
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static void objectEquals(Object obj1, Object obj2, ObjectEqualsCallBack callBack) {
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            if (obj1 == obj2) {
                e.onNext(true);
            } else {
                String json1 = GsonUtils.jsonSerializer(obj1);
                String json2 = GsonUtils.jsonSerializer(obj2);
                e.onNext(json1.equals(json2));
            }
        }).compose(SchedulerProvider.applySchedulers())
                .subscribe(equals -> {
                    if (callBack != null) {
                        callBack.result(equals);
                    }
                }, throwable -> {
                    if (callBack != null) {
                        callBack.result(false);
                    }
                });
    }

    public interface ObjectEqualsCallBack {
        void result(boolean equals);
    }

    public interface TextOverFlowedCallBack {
        void overFlowed(boolean isOverFlowed);
    }
}
