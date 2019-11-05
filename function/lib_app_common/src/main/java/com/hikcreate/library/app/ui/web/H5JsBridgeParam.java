package com.hikcreate.library.app.ui.web;

/**
 * Js Bridge 的Handler入参
 *
 * @author gongwei
 * @date 2019/3/6
 */
public class H5JsBridgeParam {
    /**
     * 请求类型 @see JsBridgeProtocolHandler.static
     */
    private String type;
    /**
     * 视type不同而不同 @see JsBridgeProtocolHandler.static
     */
    private String params;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
