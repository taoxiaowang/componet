package com.hikcreate.library.app.ui.view.recycleview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public abstract class BaseListAdapter<T extends ViewDataBinding, D> extends BaseAdapter {

    private T mBinding;
    private List<D> mData;
    private Context mContext;

    public BaseListAdapter(Context ctx, List<D> data) {
        this.mContext = ctx;
        this.mData = data;
    }

    public BaseListAdapter(Context context) {
        this(context, null);
    }

    public List<D> getData() {
        return mData;
    }

    public void setData(List<D> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addAllData(List<D> data) {
        if (mData != null) {
            mData.addAll(data);
            notifyDataSetChanged();
        } else {
            setData(data);
        }
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public D getItem(int position) {
        return mData != null && mData.get(position) != null ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), setLayoutResId(), parent, false);
        if (convertView == null) {
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), setLayoutResId(), parent, false);
        } else {
            mBinding = DataBindingUtil.getBinding(convertView);
        }

        initView(mBinding, position);
        return mBinding.getRoot();
    }

    public T getBinding() {
        return mBinding;
    }

    public abstract int setLayoutResId();

    public abstract void initView(T binding, int position);
}
