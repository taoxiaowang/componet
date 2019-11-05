package com.hikcreate.baidutextdect.util;

import android.util.Log;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class TextDecGenericsUtils {
    
    private static final String TAG = "GenericsUtils";
    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
     */
    public static Class getSuperClassGenericType(Class clazz, TextDecClassTypeEntity entity) {
        return getSuperClassGenericType(clazz, entity, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    public static Class getSuperClassGenericType(Class clazz, TextDecClassTypeEntity entity, int index) throws IndexOutOfBoundsException {

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type，然后将其转换ParameterizedType。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        if (entity == null) {
            entity = new TextDecClassTypeEntity();
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。[0]就是这个数组中第一个了。简而言之就是获得超类的泛型参数的实际类型。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            Log.d(TAG, "getSuperClassGenricType type:" + params[index]);
            if (params[index] instanceof ParameterizedType) {
                createClassTypeEntity(entity, params[index]);
                Log.d(TAG, "getSuperClassGenricType entity:" + entity);
                return (Class) ((ParameterizedType) params[index]).getRawType();
            }
            entity.addClassTyep(Object.class);
            return Object.class;
        }
        entity.addClassTyep(params[index]);
        return (Class) params[index];
    }

    /**
     * 创建类型实例，用于收纳复合类型
     *
     * @param entity
     * @param type
     */
    private static void createClassTypeEntity(TextDecClassTypeEntity entity, Type type) {
        if (type instanceof ParameterizedType) {
            if (List.class.isAssignableFrom((Class) ((ParameterizedType) type).getRawType())) {
                entity.addClassTyep(List.class);
                Type[] tparams = ((ParameterizedType) type).getActualTypeArguments();
                if (tparams != null) {
                    for (Type ttparam : tparams) {
                        TextDecClassTypeEntity tentity = new TextDecClassTypeEntity();
                        entity.setChildTypeEntity(tentity);
                        tentity.addClassTyep(ttparam);
                        createClassTypeEntity(tentity, ttparam);
                    }
                }
            }
        }
    }

    /**
     * class类别比较
     *
     * @param obj    class类型实例
     * @param entity 类型entity
     * @return
     */
    public static boolean classTypeCompare(Object obj, TextDecClassTypeEntity entity) {
        if (obj != null && entity != null) {
            if (obj instanceof List) {
                Log.d(TAG, "classTypeCompare obj is list:" + obj.getClass());
                if (entity.getClassType() != null && entity.getClassType().size() == 1
                        && List.class.isAssignableFrom((Class) (entity.getClassType().get(0)))) {
                    if (((List) obj).size() > 0) {
                        return classTypeCompare(((List) obj).get(0), entity.getChildTypeEntity());
                    }
                }
            } else {
                Log.d(TAG, "classTypeCompare obj is object:" + obj.getClass());
                if (entity.getClassType() != null && entity.getClassType().size() == 1
                        && obj.getClass().isAssignableFrom((Class) (entity.getClassType().get(0)))) {
                    return true;
                }
            }
        }
        return false;
    }
}
