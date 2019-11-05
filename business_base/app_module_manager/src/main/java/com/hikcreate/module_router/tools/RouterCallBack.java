package com.hikcreate.module_router.tools;

import com.hikcreate.module_router.router.RouterResponse;

/**
 * 异步操作回调
 *
 * @author wangtao55
 * @date 2019/9/25
 * @mail wangtao55@hikcreate.com
 */
public interface RouterCallBack {
    void callSuccess(RouterResponse response);
    void callFail(RouterResponse response);
}
