package com.hikcreate.library.app.ui.view.recycleview;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public abstract class NormalTagRecycleViewAdapter<D>
        extends BaseTagRecycleViewAdapter implements INormalRecycleViewAdapter<D> {

    private List<D> mData;

    public NormalTagRecycleViewAdapter(Context context) {
        super(context);
    }

    @Override
    public void setDatas(List<D> dataList) {
        mData = dataList;
        notifyDataSetChanged();
    }

    @Override
    public void addDatas(List<D> dataList) {
        if (dataList != null && dataList.size() > 0) {
            if (mData == null) {
                mData = new ArrayList<>();
            }
            int startIndex = mData.size();
            mData.addAll(dataList);
            notifyItemRangeInserted(startIndex, dataList.size());
        }
    }

    @Override
    public void addData(D data) {
        if (data != null) {
            if (mData == null) {
                mData = new ArrayList<>();
            }
            mData.add(data);
            notifyItemInserted(mData.size() - 1);
        }
    }

    @Override
    public D getDataAt(int position) {
        if (mData != null) {
            if (hasHeaderView() && mData.size() >= position) {
                return mData.get(position - 1);
            } else if (mData.size() > position) {
                return mData.get(position);
            }
        }
        return null;
    }

    @Override
    public D removeDataAt(int position) {
        D data = null;
        if (mData != null && mData.size() > position) {
            data = mData.remove(position);
            notifyItemRemoved(position);
        }
        return data;
    }

    @Override
    public int setContentItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public List<D> getData() {
        return mData;
    }

    public abstract int setContentViewId(int viewType);

    public abstract void bindViewHolderContentData(ViewDataBinding binding, int viewType, int position);
}
