package com.hikcreate.library.app.ui.web;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * H5WebView的回调
 *
 * @author gongwei on 2019/1/2.
 */
public abstract class H5WebViewClientCallBack {

    public abstract void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
    }

    public void onPageFinished(WebView view, String url) {
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    public void updateProgress(int progress) {
    }
}