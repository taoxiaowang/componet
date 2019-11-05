package com.hikcreate.library.app.ui.view.listview;

/**
 * A interface that defines what a View Holder Creator should do.
 *
 * @param <ItemData> the generic type of the data in each item of a list.
 * @author http://www.liaohuqiu.net
 */
public interface ViewHolderCreator<ItemData> {
    ViewHolderBase<ItemData> createViewHolder();
}