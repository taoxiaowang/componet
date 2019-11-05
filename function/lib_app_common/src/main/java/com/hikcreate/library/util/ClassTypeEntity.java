package com.hikcreate.library.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public class ClassTypeEntity {
    private List<Type> classType;
    private ClassTypeEntity childTypeEntity;

    public List<Type> getClassType() {
        return classType;
    }

    public void setClassType(List<Type> classType) {
        this.classType = classType;
    }

    public void addClassTyep(Type type) {
        if (classType == null) {
            classType = new ArrayList<>();
        }
        classType.add(type);
    }

    public ClassTypeEntity getChildTypeEntity() {
        return childTypeEntity;
    }

    public void setChildTypeEntity(ClassTypeEntity childTypeEntity) {
        this.childTypeEntity = childTypeEntity;
    }

    @Override
    public boolean equals(Object obj) {
        ClassTypeEntity entity = (ClassTypeEntity) obj;

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
