package com.hikcreate.library.app.ui.view.recycleview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public abstract class BaseTagRecycleViewAdapter extends RecyclerView.Adapter<RecycleViewHolder> {

    private RecycleViewItemClickListener mRecycleViewItemClickListener;
    private RecycleViewItemLongClickListener mRecycleViewItemLongClickListener;
    private Context mContext;
    private View mHeaderView;
    private View mFooterView;

    public static final int TYPE_HEADER = -1;
    public static final int TYPE_FOOTER = -2;
    public static final int TYPE_NORMAL = 0;

    public BaseTagRecycleViewAdapter(Context context) {
        this.mContext = context;
        setHasStableIds(true);
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d("yslei", "-------------onCreateViewHolder---------");
        if (hasHeaderView() && viewType == TYPE_HEADER) {
            return new RecycleViewHolder(mHeaderView);
        } else if (hasFooterView() && viewType == TYPE_FOOTER) {
            return new RecycleViewHolder(mFooterView);
        } else {
            ViewDataBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    setContentViewId(viewType),
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
                        mRecycleViewItemClickListener.onRecycleViewItemClick(clickItem, holder.getViewTag());
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
                        mRecycleViewItemLongClickListener.onRecycleViewLongItemClick(longClickItem, holder.getViewTag());
                    }
                }
                return false;
            });

            holder.setBinding(binding);
            holder.setViewTag(viewType);
            return holder;
        }
    }

    public void setHeaderView(View headerView) {
        if (mHeaderView != null) {
            mHeaderView = headerView;
            notifyDataSetChanged();
        } else {
            mHeaderView = headerView;
            notifyItemInserted(0);
        }
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void removeHeaderView() {
        if (mHeaderView != null) {
            mHeaderView = null;
            notifyItemRemoved(0);
        }
    }

    public boolean hasHeaderView() {
        return mHeaderView != null;
    }

    public void setFooterView(View footerView) {
        if (mFooterView != null) {
            mFooterView = footerView;
            notifyDataSetChanged();
        } else {
            mFooterView = footerView;
            notifyItemInserted(getItemCount());
        }
    }

    public void removeFooterView() {
        if (mFooterView != null) {
            mFooterView = null;
            notifyItemRemoved(getItemCount());
        }
    }

    public boolean hasFooterView() {
        return mFooterView != null;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeaderView() && position == 0) {
            return TYPE_HEADER;
        } else if (hasFooterView() && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    /**
     * 根据viewType设置内容layout id
     *
     * @param viewType
     * @return
     */
    public abstract int setContentViewId(int viewType);

    /**
     * 绑定ViewHolder内容数据
     *
     * @param binding
     * @param viewType
     * @param position
     */
    public abstract void bindViewHolderContentData(ViewDataBinding binding, int viewType, int position);

    /**
     * 设置list内容数量
     *
     * @return
     */
    public abstract int setContentItemCount();

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        //Log.d("yslei", "-------------onBindViewHolder---------position:" + position);
        if (getItemViewType(position) == TYPE_HEADER ||
                getItemViewType(position) == TYPE_FOOTER) return;

        if (isInfinite()) {
            bindViewHolderContentData(holder.getBinding(), holder.getViewTag(), position % getDataCount());
        } else {
            bindViewHolderContentData(holder.getBinding(), holder.getViewTag(), position);
        }
    }

    public boolean isInfinite() {
        return false;
    }

    public int getDataCount() {
        return setContentItemCount();
    }

    @Override
    public int getItemCount() {
        int itemCount = setContentItemCount();
        if (hasHeaderView()) {
            itemCount++;
        }
        if (hasFooterView()) {
            itemCount++;
        }
        return itemCount;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        // 修正GridLayoutManager header&footer布局
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                //当前位置是header或是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格。
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            || getItemViewType(position) == TYPE_FOOTER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecycleViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.getItemView().getLayoutParams();
        //修正StaggeredGridLayoutManager header&footer布局
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (holder.getAdapterPosition() == 0 || holder.getAdapterPosition() == getItemCount())) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public void setRecycleViewItemClickListener(RecycleViewItemClickListener recycleViewItemClickListener) {
        mRecycleViewItemClickListener = recycleViewItemClickListener;
    }

    public void setRecycleViewItemLongClickListener(RecycleViewItemLongClickListener recycleViewItemLongClickListener) {
        mRecycleViewItemLongClickListener = recycleViewItemLongClickListener;
    }

    public RecycleViewItemClickListener getRecycleViewItemClickListener() {
        return mRecycleViewItemClickListener;
    }

    public RecycleViewItemLongClickListener getRecycleViewItemLongClickListener() {
        return mRecycleViewItemLongClickListener;
    }

    public interface RecycleViewItemClickListener {
        void onRecycleViewItemClick(int position, int viewTag);
    }

    public interface RecycleViewItemLongClickListener {
        void onRecycleViewLongItemClick(int position, int viewTag);
    }
}
