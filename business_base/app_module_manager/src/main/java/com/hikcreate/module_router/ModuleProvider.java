package com.hikcreate.module_router;

import java.util.HashMap;

/**
 * @author wangtao55
 * @date 2019/9/19
 * @mail wangtao55@hikcreate.com
 */
public abstract class ModuleProvider {
    private boolean mValid = true;
    private HashMap<String, ModuleAction> mActions;

    public ModuleProvider() {
        mActions = new HashMap<>();
        registerActions();
    }

    protected void registerAction(String actionName, ModuleAction action) {
        mActions.put(actionName, action);
    }

    public ModuleAction findAction(String actionName) {
        return mActions.get(actionName);
    }

    public boolean isValid() {
        return mValid;
    }

    protected abstract void registerActions();
}
