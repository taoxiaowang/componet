package com.hikcreate.library.app.ui.web;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.studio.plugins.GsonUtils;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.hikcreate.library.app.config.AppConfig;
import com.hikcreate.library.app.config.AppContext;
import com.hikcreate.library.util.LogCat;
import com.hikcreate.library.util.ViewUtil;

import java.util.Map;

/**
 * 公共组件H5 WebView
 *
 * @author gongwei 2019.1.2
 */

public class H5WebView extends BridgeWebView {

    private ProgressBar progressBar;
    private boolean scroll = true; //页面是否滚动,，内嵌到ScrollView中时考虑设置
    private boolean wideViewPort = true; //是否完整适配屏幕，会导致字体变小，内嵌到ScrollView中时考虑设置
    private H5WebViewClientCallBack mCallBack;
    private boolean initSettingCompleted = false;//WebSetting设置是否完成

    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;

    public H5WebView(Context context) {
        super(context);
    }

    public H5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public H5WebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void loadUrl(String url) {
        initWebSetting();
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        initWebSetting();
        super.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
        initWebSetting();
        super.loadUrl(jsUrl, returnCallback);
    }

    @Override
    public void loadDataWithBaseURL(@Nullable String baseUrl, String data, @Nullable String mimeType, @Nullable String encoding, @Nullable String historyUrl) {
        initWebSetting();
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadData(String data, @Nullable String mimeType, @Nullable String encoding) {
        initWebSetting();
        super.loadData(data, mimeType, encoding);
    }

    /**
     * 设置ProgressBar
     *
     * @param progressBar
     */
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * 页面是否滚动,，内嵌到ScrollView中时考虑设置
     *
     * @param scroll
     */
    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }

    /**
     * 设置WebViewClientCallBack For WebView's LifeCycle
     *
     * @param callBack
     */
    public void setCallBack(H5WebViewClientCallBack callBack) {
        this.mCallBack = callBack;
    }

    /**
     * 是否完整适配屏幕，会导致字体变小，内嵌到ScrollView中时考虑设置
     *
     * @param wideViewPort
     */
    public void setWideViewPort(boolean wideViewPort) {
        this.wideViewPort = wideViewPort;
        getSettings().setUseWideViewPort(this.wideViewPort);
    }

    public void initWebSetting() {
        if (!initSettingCompleted) {
            initSettingCompleted = true;

            setLayerType(View.LAYER_TYPE_NONE, null);
            setBackgroundColor(Color.TRANSPARENT);//设置背景色
            setWebChromeClient(new H5WebChromeClient());
            setWebViewClient(new H5WebViewClient(this));

            WebSettings webSettings = getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 设置图片宽度不超过屏幕
            webSettings.setSavePassword(false);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setBlockNetworkImage(false);
            webSettings.setUseWideViewPort(wideViewPort);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setDefaultTextEncodingName("utf-8");
            webSettings.setDomStorageEnabled(true);
            webSettings.setAllowFileAccess(true);//设置可以访问文件
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭缓存，否则可能会存在一些不刷新的问题
            webSettings.setUserAgentString(String.format("%s%s", webSettings.getUserAgentString(), AppConfig.WEB_USER_AGENT_MARK));//在UserAgent增加标识，用以web页面判断是否在我们的App中打开
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            //check js bridge
            addJsBridgeHandler();
        }
    }

    /**
     * 添加Js Bridge
     */
    public void addJsBridgeHandler() {
        //数据类Handler
        registerHandler(JsBridgeProtocolHandler.HN_JS_2_NATIVE_DATA, new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_JS_2_NATIVE_DATA + "' 入参：" + data);
                if (TextUtils.isEmpty(data)) return;
                H5JsBridgeParam h5Param = GsonUtils.jsonDeserializer(data, H5JsBridgeParam.class);
                Activity activityHost = ViewUtil.getActivityFromView(H5WebView.this);
                if (h5Param != null && activityHost != null) {
                    JsBridgeProtocolHandler.getInstance().handleActionProtocol(activityHost, h5Param, (String result) -> {
                        function.onCallBack(result);
                        LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_JS_2_NATIVE_DATA + "' 返回：" + result);
                    });
                }
            }
        });
        //动作类Handler
        registerHandler(JsBridgeProtocolHandler.HN_JS_2_NATIVE_ACTION, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_JS_2_NATIVE_ACTION + "' 入参：" + data);
                if (TextUtils.isEmpty(data)) return;
                H5JsBridgeParam h5Param = GsonUtils.jsonDeserializer(data, H5JsBridgeParam.class);
                Activity activityHost = ViewUtil.getActivityFromView(H5WebView.this);
                if (h5Param != null && activityHost != null) {
                    JsBridgeProtocolHandler.getInstance().handleActionProtocol(activityHost, h5Param, (String result) -> {
                        function.onCallBack(result);
                        LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_JS_2_NATIVE_ACTION + "' 返回：" + result);
                    });
                }
            }
        });
    }

    /**
     * 发送数据给Js
     *
     * @param dataToJs
     */
    public void sendDataToJs(H5JsBridgeParam dataToJs) {
        String dataString = GsonUtils.jsonSerializer(dataToJs);
        callHandler(JsBridgeProtocolHandler.HN_NATIVE_2_JS_DATA, dataString, null);
        LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_NATIVE_2_JS_DATA + "' ：" + dataString);
    }

    /**
     * 发送指令给Js
     *
     * @param dataToJs
     */
    public void sendActionToJs(H5JsBridgeParam dataToJs) {
        String dataString = GsonUtils.jsonSerializer(dataToJs);
        callHandler(JsBridgeProtocolHandler.HN_NATIVE_2_JS_ACTION, dataString, null);
        LogCat.i("Js Bridge '" + JsBridgeProtocolHandler.HN_NATIVE_2_JS_ACTION + "' ：" + dataString);
    }

    /**
     * 添加Js Interface
     */
    @SuppressLint("JavascriptInterface")
    public void addJsInterface(Object obj, String interfaceName) {
        if (getContext() instanceof Activity) {
            addJavascriptInterface(obj, interfaceName);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!scroll) {
            int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 封装后的WebChromeClient
     */
    private class H5WebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (progressBar != null) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
            if (mCallBack != null) {
                mCallBack.updateProgress(newProgress);
            }
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            if (getContext() instanceof Activity) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                Activity activityHost = ViewUtil.getActivityFromView(H5WebView.this);
                if (activityHost != null) {
                    activityHost.startActivityForResult(Intent.createChooser(i, "File Chooser"), AppContext.REQUEST_CODE_WEB_SELECT_FILE_OFF_41);
                }
            }
        }

        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            if (getContext() instanceof Activity) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                Activity activityHost = ViewUtil.getActivityFromView(H5WebView.this);
                if (activityHost != null) {
                    activityHost.startActivityForResult(Intent.createChooser(i, "File Browser"), AppContext.REQUEST_CODE_WEB_SELECT_FILE_OFF_41);
                }
            }
        }


        // For Lollipop 5.0+ Devices
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (getContext() instanceof Activity) {
                uploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    Activity activityHost = ViewUtil.getActivityFromView(H5WebView.this);
                    if (activityHost != null) {
                        activityHost.startActivityForResult(intent, AppContext.REQUEST_CODE_WEB_SELECT_FILE);
                    }
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    return false;
                }
            }
            return true;
        }

        //For Android 4.1 only
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            if (getContext() instanceof Activity) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                Activity activityHost = ViewUtil.getActivityFromView(H5WebView.this);
                if (activityHost != null) {
                    activityHost.startActivityForResult(Intent.createChooser(intent, "File Browser"), AppContext.REQUEST_CODE_WEB_SELECT_FILE_OFF_41);
                }
            }
        }
    }

    private class H5WebViewClient extends BridgeWebViewClient {

        public H5WebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mCallBack != null) {
                mCallBack.onPageStarted(view, url, favicon);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mCallBack != null) {
                mCallBack.onPageFinished(view, url);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (mCallBack != null) {
                mCallBack.onReceivedError(view, errorCode, description, failingUrl);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //支付宝网页支付拦截
            if (parseScheme(url)) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setComponent(null);
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            boolean result = false;
            if (mCallBack != null && !url.contains("alipay.com/")) {
                result = mCallBack.shouldOverrideUrlLoading(view, url);
            }
            if (!result) {
                //url.replaceAll("%(?![0-9a-fA-F]{2})", "%25") 解决% encode异常
                result = super.shouldOverrideUrlLoading(view, url.replaceAll("%(?![0-9a-fA-F]{2})", "%25"));
            }
            return result;
        }

        /**
         * 支付宝网页支付是先后调用如下网页
         * 1.https://mobilecodec.alipay.com/client_download.htm?qrcode=bax05351pgjhc4yegd2y2084
         * 2.https://ds.alipay.com/from=mobilecodec&scheme=alipayqr%3A%2F%2Fplatformapi%2Fstartapp%3FsaId%3D10000007%26clientVersion%3D3.7.0.0718%26qrcode%3Dhttps%253A%252F%252Fqr.alipay.com%252Fbax05351pgjhc4yegd2y2084%253F_s%253Dweb-other
         * 之后返回一个意图，也是用这个意图来打开支付宝app
         * intent://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Fbax05351pgjhc4yegd2y2084%3F_s%3Dwebother&_t=1474448799004#Intent;scheme=alipayqr;package=com.eg.android.AlipayGphone;end
         * <p>
         * 基于以上而做的拦截
         *
         * @param url
         * @return
         */
        public boolean parseScheme(String url) {
            String urlTemp = url.toLowerCase();
            return urlTemp.contains("platformapi/startapp");
        }
    }
}
