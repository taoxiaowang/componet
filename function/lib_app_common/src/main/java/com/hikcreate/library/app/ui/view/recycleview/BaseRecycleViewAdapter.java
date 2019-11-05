package com.hikcreate.library.app.ui.view.recycleview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public abstract class BaseRecycleViewAdapter<B extends ViewDataBinding> extends RecyclerView.Adapter<RecycleViewHolder> {

    private RecycleViewItemClickListener mRecycleViewItemClickListener;
    private RecycleViewItemLongClickListener mRecycleViewItemLongClickListener;
    private Context mContext;

    public BaseRecycleViewAdapter(Context context) {
        this.mContext = context;
        setHasStableIds(true);
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d("yslei", "-------------onCreateViewHolder---------");
        B binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                setViewId(),
                parent,
                false);
        RecycleViewHolder holder = new RecycleViewHolder(binding.getRoot());
        holder.getItemView().setOnClickListener(v -> {
            if (mRecycleViewItemClickListener != null) {
                int clickItem;
                if (isInfinite()) {
                    clickItem = holder.getAdapterPosition() % getDataCount();
                } else {
                    clickItem = holder.getAdapterPosition();
                }
                if (clickItem >= 0 && clickItem < getDataCount()) {
                    mRecycleViewItemClickListener.onRecycleViewItemClick(clickItem);
                    handleItemSelect(clickItem);
                }
            }
        });
        holder.getItemView().setOnLongClickListener(v -> {
            if (mRecycleViewItemLongClickListener != null) {
                int longClickItem;
                if (isInfinite()) {
                    longClickItem = holder.getAdapterPosition() % getDataCount();
                } else {
                    longClickItem = holder.getAdapterPosition();
                }
                if (longClickItem >= 0 && longClickItem < getDataCount()) {
                    mRecycleViewItemLongClickListener.onRecycleViewLongItemClick(longClickItem);
                    handleItemSelect(longClickItem);
                }
            }
            return false;
        });

        holder.setBinding(binding);
        return holder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        //Log.d("yslei", "-------------onBindViewHolder---------position:" + position);

        if (isInfinite()) {
            bindViewHolderData((B) holder.getBinding(), position % getDataCount());
        } else {
            bindViewHolderData((B) holder.getBinding(), position);
        }
    }

    public void handleItemSelect(int selectItem) {
    }

    public abstract int setViewId();

    public abstract void bindViewHolderData(B binding, int position);

    public abstract int setItemCount();

    public boolean isInfinite() {
        return false;
    }

    public int getDataCount() {
        return setItemCount();
    }

    @Override
    public int getItemCount() {
        return setItemCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Context getContext() {
        return mContext;
    }

    public void setRecycleViewItemClickListener(RecycleViewItemClickListener recycleViewItemClickListener) {
        mRecycleViewItemClickListener = recycleViewItemClickListener;
    }

    public void setRecycleViewItemLongClickListener(RecycleViewItemLongClickListener recycleViewItemLongClickListener) {
        mRecycleViewItemLongClickListener = recycleViewItemLongClickListener;
    }

    public interface RecycleViewItemClickListener {
        void onRecycleViewItemClick(int position);
    }

    public interface RecycleViewItemLongClickListener {
        void onRecycleViewLongItemClick(int position);
    }
}
