package com.hikcreate.module_bus.util;

import com.google.gson.Gson;
import com.hikcreate.module_bus.bean.GeneralMessageBean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author : taowang
 * date :2019/9/19
 * description:消息实体操作类
 **/
public class ObjectUtil {

    /**
     * @param className   : 全路径的类名
     * @param dataContent
     * @return
     */
    public static Object getObjectBean(String className, String dataContent) {
        Object messageBeanObject = null;
        try {
            Class messageClass = Class.forName(className);
            messageBeanObject = messageClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageBeanObject;
    }

    private static Object invokeMethod(Object object, String methodName) {
        try {
            Method method = object.getClass().getMethod(methodName);
            return method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getInfoByMethod(String className, String dataContent, String methodName) {
        return invokeMethod(getObjectBean(className, dataContent), methodName);
    }


    public static <T> T createInstance(Class<T> clz) {
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T createInstance(Class<T> checkType, String className) {

        try {
            Class<T> clz = (Class<T>) Class.forName(className);
            Object obj = clz.newInstance();
            if (!checkType.isInstance(obj)) {
                throw new Exception("类型不匹配");
            }
            return (T) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void readAttributeValue(Object obj) {
        String nameValues = "";
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                field.setAccessible(true);
                Object value = field.get(obj);
                nameValues += field.getName() + ":" + value + ",";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int lastIndex = nameValues.lastIndexOf(",");
        String result = nameValues.substring(0, lastIndex);
        System.out.println(result);
    }

    public static String readAttributeValueByName(Object obj, String name) {
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (name.equals(field.getName())) {
                    return (String) field.get(obj);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
