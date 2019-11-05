package com.hikcreate.module_bus.driver;

import android.util.Log;

import com.hikcreate.module_bus.bean.GeneralMessageBean;
import com.hikcreate.module_bus.util.ObjectUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import info.hook.com.lib_module_common.BuildConfig;

/**
 * author : taowang
 * date :2019/9/19
 * description:消息控制中枢
 **/
public final class MessageWrap {

    private static volatile MessageWrap defaultInstance;
    private EventBus messageEventBus;
    private int messageCount; //消息数量
    private static final String MESSAGE_TAG = "HIK-MessageWrap";

    public static MessageWrap getMessageWrapDefault() {
        if (defaultInstance == null) {
            synchronized (MessageWrap.class) {
                if (defaultInstance == null) {
                    defaultInstance = new MessageWrap();
                }
            }
        }
        return defaultInstance;
    }

    private MessageWrap() {
        messageEventBus = new EventBus();
    }

    public void register(Object subscriber) {
        messageEventBus.register(subscriber);
        messageCount++;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void unregister(Object subscriber) {
        messageEventBus.unregister(subscriber);
        messageCount--;
    }

    public void sendMessage(GeneralMessageBean mGeneralMessageBean) {
        //可以添加一些业务处理的代码，控制消息的发送
        messageEventBus.post(mGeneralMessageBean);
        showInfo(mGeneralMessageBean);
    }

    public void sendMessageSticky(GeneralMessageBean mGeneralMessageBean) {
        //可以添加一些业务处理的代码，控制消息的发送
        messageEventBus.postSticky(mGeneralMessageBean);
        showInfo(mGeneralMessageBean);
    }

    public String getMessageInfo(GeneralMessageBean mGeneralMessageBean, String name) {
        return ObjectUtil.readAttributeValueByName(mGeneralMessageBean, name);
    }

    private void showInfo(GeneralMessageBean mGeneralMessageBean) {
        if (BuildConfig.DEBUG) {
            //打印消息信息
            Log.v(MESSAGE_TAG, "moduleName-----" + mGeneralMessageBean.getModuleName());
            Log.v(MESSAGE_TAG, "dataAction-----" + mGeneralMessageBean.getDataAction());
            if(mGeneralMessageBean.getDataValue() != null){
                for (Map.Entry<String, String> entry : mGeneralMessageBean.getDataValue().entrySet()) {
                    Log.v(MESSAGE_TAG, "dataAction-----" + "key= " + entry.getKey() + " and value= " + entry.getValue());
                }
            }
        }
    }

}
