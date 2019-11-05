package com.hikcreate.library.app.ui.core.login;

/**
 * 登录管理器
 *
 * @author gongwei
 * @date 2019/4/3
 * @mail gongwei5@hikcreate.com
 */
public abstract class AppLoginManager {

    private static AppLoginManager INSTANCE = null;

    public static void setLoginManager(AppLoginManager loginManager) {
        INSTANCE = loginManager;
    }

    /**
     * 获取用户ID
     *
     * @return userId or null
     */
    public static String getUserId() {
        if (INSTANCE != null) {
            return INSTANCE.getId();
        }
        return null;
    }

    /**
     * 是否已登录
     *
     * @return
     */
    public static boolean isLogin() {
        if (INSTANCE != null) {
            return INSTANCE.isUserLogin();
        }
        return false;
    }

    protected abstract String getId();

    protected abstract boolean isUserLogin();

    public interface LoginCallback {
        void onSuccess();

        void onError(String response);
    }
}
