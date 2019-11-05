package com.hikcreate.library.app.ui.view.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A adapter using View Holder to display the item of a list view;
 *
 * @param <Item>
 * @author http://www.liaohuqiu.net
 */
public class ListViewDataAdapter<Item> extends ListViewDataAdapterBase<Item> {

    private static String LOG_TAG = "cube_list";
    protected ViewHolderCreator<Item> mViewHolderCreator;

    public ListViewDataAdapter(ViewHolderCreator<Item> viewHolderCreator) {
        mViewHolderCreator = viewHolderCreator;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ViewHolderBase<Item> holder = mViewHolderCreator.createViewHolder();
            if (holder != null) {
                holder.setAdapter(this);
                convertView = holder.performCreateView(inflater, parent);
                if (convertView != null) {
                    holder.setItemData(position);
                    holder.showData(position, item);
                    convertView.setTag(holder);
                }
            }
        } else {
            ViewHolderBase<Item> holder = (ViewHolderBase<Item>) convertView.getTag();
            if (holder != null) {
                holder.setItemData(position);
                holder.showData(position, item);
            }
        }
        return convertView;
    }
}
