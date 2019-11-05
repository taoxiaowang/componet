package com.hikcreate.library.app.ui.view.listview;

import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * A adapter using View Holder to display the item of a list view;
 *
 * @param <Item>
 * @author http://www.liaohuqiu.net
 */
public abstract class ListViewDataAdapterBase<Item> extends BaseAdapter {

    protected ArrayList<Item> mItemDataList = new ArrayList<Item>();

    public ArrayList<Item> getDataList() {
        return mItemDataList;
    }

    @Override
    public int getCount() {
        return mItemDataList.size();
    }

    @Override
    public Item getItem(int position) {
        if (mItemDataList.size() <= position || position < 0) {
            return null;
        }
        return mItemDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
