package com.hikcreate.login.moduel.router.provider;

import com.hikcreate.login.moduel.router.action.change.ChangeUserInfo;
import com.hikcreate.login.moduel.router.action.login.GetUserInfoAction;
import com.hikcreate.module_router.ModuleProvider;

import static com.hikcreate.data.config.AppProvider.GET_USER_INFO_ACTION;
import static com.hikcreate.data.config.AppProvider.USER_INFO_CHANGE_ACTION;


/**
 * 登录Provider
 *
 * @author wangtao55
 * @date 2019/9/24
 * @mail wangtao55@hikcreate.com
 */
public class LoginProvider extends ModuleProvider {
    @Override
    public void registerActions() {
        registerAction(GET_USER_INFO_ACTION, new GetUserInfoAction());
        registerAction(USER_INFO_CHANGE_ACTION, new ChangeUserInfo());
    }
}
