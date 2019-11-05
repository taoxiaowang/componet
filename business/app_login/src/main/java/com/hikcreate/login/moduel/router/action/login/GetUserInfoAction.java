package com.hikcreate.login.moduel.router.action.login;

import android.content.Context;

import com.hikcreate.module_router.ModuleAction;
import com.hikcreate.module_router.ModuleActionResult;

import java.util.HashMap;

/**
 * 用户信息action
 *
 * @author wangtao55
 * @date 2019/9/24
 * @mail wangtao55@hikcreate.com
 */
public class GetUserInfoAction extends ModuleAction {
    @Override
    public boolean isAsync(HashMap<String, String> requestData) {
        return false;
    }

    @Override
    public ModuleActionResult invoke(Context context, HashMap<String, String> requestData) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "test");
        hashMap.put("mobile", "15828028350");
        return new ModuleActionResult.Builder()
                .code(ModuleActionResult.CODE_SUCCESS)
                .msg("登录成功")
                .data("我是来自登录界面的数据")
                .object(hashMap)
                .build();
    }
}
