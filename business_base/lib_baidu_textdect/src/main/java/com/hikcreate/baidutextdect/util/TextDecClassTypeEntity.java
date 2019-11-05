package com.hikcreate.baidutextdect.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class TextDecClassTypeEntity {
    private List<Type> classType;
    private TextDecClassTypeEntity childTypeEntity;

    public List<Type> getClassType() {
        return classType;
    }

    public void setClassType(List<Type> classType) {
        this.classType = classType;
    }

    public void addClassTyep(Type type){
        if(classType == null){
            classType = new ArrayList<>();
        }
        classType.add(type);
    }

    public TextDecClassTypeEntity getChildTypeEntity() {
        return childTypeEntity;
    }

    public void setChildTypeEntity(TextDecClassTypeEntity childTypeEntity) {
        this.childTypeEntity = childTypeEntity;
    }

    @Override
    public boolean equals(Object obj) {
        TextDecClassTypeEntity entity = (TextDecClassTypeEntity) obj;

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "ClassTypeEntity{" +
                "classType=" + classType +
                ", childTypeEntity=" + childTypeEntity +
                '}';
    }
}
