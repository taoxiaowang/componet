package com.hikcreate.login.moduel.router.action.change;

import android.content.Context;

import com.hikcreate.library.util.LogCat;
import com.hikcreate.module_router.ModuleAction;
import com.hikcreate.module_router.ModuleActionResult;

import java.util.HashMap;

/**
 * 修改密码
 *
 * @author wangtao55
 * @date 2019/9/24
 * @mail wangtao55@hikcreate.com
 */
public class ChangeUserInfo extends ModuleAction {
    @Override
    public boolean isAsync(HashMap<String, String> requestData) {
        return true;
    }

    @Override
    public ModuleActionResult invoke(Context context, HashMap<String, String> requestData) {

        LogCat.d("userInfo", "new-->" + requestData.get("name"));
        LogCat.d("userInfo", "new-->" + requestData.get("mobile"));

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "newTest");
        hashMap.put("mobile", "18888888888");
        return new ModuleActionResult.Builder()
                .code(ModuleActionResult.CODE_SUCCESS)
                .msg("修改密码成功")
                .data("18888888888-newTest")
                .object(hashMap)
                .build();
    }
}
