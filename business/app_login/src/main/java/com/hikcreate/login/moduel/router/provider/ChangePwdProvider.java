package com.hikcreate.login.moduel.router.provider;

import com.hikcreate.login.moduel.router.action.change.ChangeUserInfo;
import com.hikcreate.module_router.ModuleProvider;
import static com.hikcreate.data.config.AppProvider.USER_INFO_CHANGE_ACTION;

/**
 * 登录Provider
 *
 * @author wangtao55
 * @date 2019/9/24
 * @mail wangtao55@hikcreate.com
 */
public class ChangePwdProvider extends ModuleProvider {
    @Override
    protected void registerActions() {
        registerAction(USER_INFO_CHANGE_ACTION, new ChangeUserInfo());
    }
}
