package com.hikcreate.library.app.ui.view.databindadapter;

/**
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public interface IRefreshViewModel {
    // 用于在ViewModel刷新数据
    void refresh();

    // 用于在ViewModel加载更多数据
    void loadMore();
}
