package com.hikcreate.module_bus.bean;

import java.util.HashMap;

/**
 * 事件驱动消息流类
 *
 * @author wangtao55
 * @date 2019/9/19
 * @mail wangtao55@hikcreate.com
 */
public class GeneralMessageBean {
    private String dataAction;//消息来自于哪个事件
    private String moduleName;//消息moduleName
    private HashMap<String,String> dataValue;


    public String getDataAction() {
        return dataAction;
    }

    public void setDataAction(String dataAction) {
        this.dataAction = dataAction;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public HashMap<String, String> getDataValue() {
        return dataValue;
    }

    public void setDataValue(HashMap<String, String> dataValue) {
        this.dataValue = dataValue;
    }
}
