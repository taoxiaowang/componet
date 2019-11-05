package com.hikcreate.module_router;

import android.content.Context;

import java.util.HashMap;

/**
 * @author wangtao55
 * @date 2019/9/19
 * @mail wangtao55@hikcreate.com
 */

public abstract class ModuleAction {
    public abstract boolean isAsync(HashMap<String, String> requestData);

    public abstract ModuleActionResult invoke(Context context, HashMap<String, String> requestData);

    public boolean isAsync(HashMap<String, String> requestData, Object object) {
        return false;
    }

    public ModuleActionResult invoke(Context context, HashMap<String, String> requestData, Object object) {
        return new ModuleActionResult.Builder().code(ModuleActionResult.CODE_NOT_IMPLEMENT).
                msg("This method has not yet been implemented.").build();
    }
}
