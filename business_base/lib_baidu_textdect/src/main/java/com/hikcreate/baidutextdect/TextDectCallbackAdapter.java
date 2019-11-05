package com.hikcreate.baidutextdect;


import com.hikcreate.baidutextdect.util.TextDecClassTypeEntity;
import com.hikcreate.baidutextdect.util.TextDecGenericsUtils;

/**
 * to do
 *
 * @author yslei
 * @data 2019/4/2
 * @email leiyongsheng@hikcreate.com
 */
public class TextDectCallbackAdapter<T> implements ITextDectManager.TextDectCallback<T> {
    private Class<T> mClazzType;
    private TextDecClassTypeEntity mClassTypeEntity;

    public TextDectCallbackAdapter(){
        mClassTypeEntity = new TextDecClassTypeEntity();
        mClazzType = TextDecGenericsUtils.getSuperClassGenericType(getClass(), mClassTypeEntity);
    }

    public Class<T> getClazzType() {
        return mClazzType;
    }

    @Override
    public void onResult(T result) {

    }

    @Override
    public void onError(String message) {

    }
}
