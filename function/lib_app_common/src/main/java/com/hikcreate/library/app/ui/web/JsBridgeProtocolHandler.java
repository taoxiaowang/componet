package com.hikcreate.library.app.ui.web;

import android.app.Activity;
import android.text.TextUtils;

/**
 * JsBridge 的协议解析器
 *
 * @author gongwei
 * @date 2019/3/6
 */
public abstract class JsBridgeProtocolHandler {

    /**
     * JS->Native
     */
    //数据类Handler Name
    public static final String HN_JS_2_NATIVE_DATA = "com.hikcreate.native_echo.data";
    //动作类Handler Name
    public static final String HN_JS_2_NATIVE_ACTION = "com.hikcreate.native_echo.action";
    /**
     * Native->JS
     */
    //数据类Handler Name
    public static final String HN_NATIVE_2_JS_DATA = "com.hikcreate.javascript_echo.data";
    //动作类Handler Name
    public static final String HN_NATIVE_2_JS_ACTION = "com.hikcreate.javascript_echo.action";
    //public static final String HN_NATIVE_2_JS_ACTION = "commonCall";

    /**
     * type:获取用户信息
     * param:null
     */
    public static final String DATA_USER_INFO = "userInfo";
    /**
     * type:获取app设备相关信息
     * param:null
     */
    public static final String DATA_APP_INFO = "appInfo";
    /**
     * type:原生页面跳转
     * param:routeUrl
     */
    public static final String ACTION_ROUTE = "route";
    /**
     * type:分享App
     * param:{title: '',summary: '',imageUrl: '',url: ''}
     */
    public static final String ACTION_SHARE_CONTENT = "shareContent";
    /**
     * type:关闭原生界面
     * param:null
     */
    public static final String ACTION_CLOSE_NATIVE_PAGE = "closeNativePage";
    /**
     * type:设置原生界面标题
     * param:{title: ""}
     */
    public static final String ACTION_SET_PAGE_TITLE = "setPageTitle";
    /**
     * type:调用原生图片查看器
     * param:"{"imageList": ["图片全路径数组"],currentIndex: 1 // 当前图片索引，用于指明打开大图浏览器后默认显示哪张图片}"
     */
    public static final String ACTION_OPEN_IMAGE_BROWSER = "openImageBrowser";
    /**
     * type:原生返回按钮可用性控制
     * param:"{"status": true // 原生返回是否可用}"
     */
    public static final String ACTION_NATIVE_BACK_CONTROL = "nativeBackControl";
    /**
     * type:调用原生图片查看器
     * param:"{"status": true // 原生web界面被返回时是否刷新}"
     */
    public static final String ACTION_REFRESH_WHEN_APPEAR = "refreshWhenAppear";
    /**
     * type:JS发现用户登录状态（token）不可用
     * param:"{"msg": "登录不可用的具体信息"}"
     */
    public static final String ACTION_TOKEN_INVALID = "tokenInvalidHandler";
    /**
     * type:JS发现用户被强制下线
     * param:"{"msg": "被踢提示消息"}"
     */
    public static final String ACTION_FORCED_OFFLINE = "forcedOfflineHandler";
    /**
     * type:JS发现用户专网token异常
     * param:"{"msg": "专网token异常的具体信息"}"
     */
    public static final String ACTION_TF_TOKEN_INVALID = "pvtTokenInvalidHandler";
    /**
     * type:JS发现用户账号被冻结
     * param:"{"msg": "冻结提示信息"}"
     */
    public static final String ACTION_FROZEN = "frozenAccountHandler";

    protected static JsBridgeProtocolHandler INSTANCE = null;

    public static JsBridgeProtocolHandler getInstance() {
        return INSTANCE;
    }

    public static void setInstance(JsBridgeProtocolHandler instance) {
        INSTANCE = instance;
    }

    /**
     * 处理协议
     *
     * @param param
     * @return
     */
    public void handleActionProtocol(Activity activity, H5JsBridgeParam param, JsBridgeProtocolCallBack callBack) {
        if (param == null || TextUtils.isEmpty(param.getType())) {
            return;
        }
        switch (param.getType()) {
            case DATA_USER_INFO:
                INSTANCE.handleUserInfo(callBack);
                break;
            case DATA_APP_INFO:
                INSTANCE.handleAppInfo(callBack);
                break;
            case ACTION_ROUTE:
                INSTANCE.handleRouter(param.getParams(), callBack);
                break;
            case ACTION_SHARE_CONTENT:
                INSTANCE.handleShareContent(activity, param.getParams(), callBack);
                break;
            case ACTION_CLOSE_NATIVE_PAGE:
                INSTANCE.closeNativePage(activity, callBack);
                break;
            case ACTION_SET_PAGE_TITLE:
                INSTANCE.setPageTitle(activity, param.getParams(), callBack);
                break;
            case ACTION_OPEN_IMAGE_BROWSER:
                INSTANCE.openImageBrowser(activity, param.getParams(), callBack);
                break;
            case ACTION_NATIVE_BACK_CONTROL:
                INSTANCE.nativeBackControl(activity, param.getParams(), callBack);
                break;
            case ACTION_REFRESH_WHEN_APPEAR:
                INSTANCE.refreshWhenAppear(activity, param.getParams(), callBack);
                break;
            case ACTION_TOKEN_INVALID:
                INSTANCE.tokenInvalidHandler(param.getParams());
                break;
            case ACTION_FORCED_OFFLINE:
                INSTANCE.forcedOfflineHandler(param.getParams());
                break;
            case ACTION_TF_TOKEN_INVALID:
                INSTANCE.tfTokenInvalidHandler(param.getParams());
                break;
            case ACTION_FROZEN:
                INSTANCE.frozenHandler(param.getParams());
                break;
        }
    }

    protected abstract void handleUserInfo(JsBridgeProtocolCallBack callBack);

    protected abstract void handleAppInfo(JsBridgeProtocolCallBack callBack);

    protected abstract void handleRouter(String params, JsBridgeProtocolCallBack callBack);

    protected abstract void handleShareContent(Activity activity, String params, JsBridgeProtocolCallBack callBack);

    protected abstract void closeNativePage(Activity activity, JsBridgeProtocolCallBack callBack);

    protected abstract void setPageTitle(Activity activity, String params, JsBridgeProtocolCallBack callBack);

    protected abstract void openImageBrowser(Activity activity, String params, JsBridgeProtocolCallBack callBack);

    protected abstract void nativeBackControl(Activity activity, String params, JsBridgeProtocolCallBack callBack);

    protected abstract void refreshWhenAppear(Activity activity, String params, JsBridgeProtocolCallBack callBack);

    protected abstract void tokenInvalidHandler(String params);

    protected abstract void forcedOfflineHandler(String params);

    protected abstract void tfTokenInvalidHandler(String params);

    protected abstract void frozenHandler(String params);

    /**
     * Native->Js 协议类型：action
     * 原生点击了界面退出按钮，告知JS做相应的处理，原生不做退出界面的处理（只在JS通过nativeBackControl协议告知原生需要做返回拦截的情况下调用）
     *
     * @return
     */
    public H5JsBridgeParam getNativeBackClickParam() {
        H5JsBridgeParam param = new H5JsBridgeParam();
        param.setType("nativeBackClick");
        return param;
    }

    public interface JsBridgeProtocolCallBack {
        void call(String result);
    }
}
