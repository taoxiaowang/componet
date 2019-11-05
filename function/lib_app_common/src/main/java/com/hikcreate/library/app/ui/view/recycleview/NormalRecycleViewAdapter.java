package com.hikcreate.library.app.ui.view.recycleview;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public abstract class NormalRecycleViewAdapter<D, B extends ViewDataBinding>
        extends BaseRecycleViewAdapter<B> implements INormalRecycleViewAdapter<D> {

    private boolean mIsSelectMutex;//是否是互斥选择
    private Map<D, Boolean> mSelectMaps;
    private List<D> mData;

    public NormalRecycleViewAdapter(Context context) {
        this(context, false);
    }

    public NormalRecycleViewAdapter(Context context, List<D> data) {
        this(context, false, data);
    }

    public NormalRecycleViewAdapter(Context context, boolean isSelectMutex) {
        this(context, isSelectMutex, null);
    }

    public NormalRecycleViewAdapter(Context context, boolean isSelectMutex, List<D> data) {
        super(context);
        mData = data;

        mIsSelectMutex = isSelectMutex;
        mSelectMaps = new HashMap<>();
    }

    @Override
    public void handleItemSelect(int selectItem) {
        super.handleItemSelect(selectItem);
        if (mIsSelectMutex) {
            mSelectMaps.clear();
        }
        mSelectMaps.put(getDataAt(selectItem), true);
    }

    public List<D> getAllSelectItems() {
        if (mSelectMaps.size() > 0) {
            List<D> datas = new ArrayList<>();
            for (D data : mSelectMaps.keySet()) {
                datas.add(data);
            }
            return datas;
        }
        return null;
    }

    public boolean isEmpty() {
        return mData == null || mData.isEmpty();
    }

    public boolean isItemSelect(int positon) {
        D data = getDataAt(positon);
        return mSelectMaps.get(data);
    }

    @Override
    public void addDatas(List<D> data) {
        if (data != null && data.size() > 0) {
            if (mData == null) {
                mData = data;
                notifyDataSetChanged();
            } else {
                int positionStart = getDataCount() - 1;
                mData.addAll(data);
                notifyItemRangeInserted(positionStart, data.size());
            }
        }
    }

    @Override
    public void setDatas(List<D> datas) {
        if (datas != null && datas.size() > 0) {
            mData = datas;
        } else {
            mData = null;
        }
        mSelectMaps.clear();
        notifyDataSetChanged();
    }

    @Override
    public void addData(D data) {
        if (data == null) return;
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(data);
        notifyItemChanged(mData.size() - 1);
    }

    @Override
    public List<D> getData() {
        return mData;
    }

    @Override
    public int getDataCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public D getDataAt(int index) {
        if (mData != null && mData.size() > index) {
            return mData.get(index);
        }
        return null;
    }

    @Override
    public D removeDataAt(int position) {
        D data = null;
        if (mData != null && mData.size() > position) {
            data = mData.remove(position);
            mSelectMaps.remove(data);
            notifyItemRemoved(position);
        }
        return data;
    }

    @Override
    public int setItemCount() {
        return getDataCount();
    }
}
