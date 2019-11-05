package com.hikcreate.library.app.ui.view.recycleview;

import java.util.List;

/**
 * to do
 *
 * @author yslei
 * @data 2019/3/28
 * @email leiyongsheng@hikcreate.com
 */
public interface INormalRecycleViewAdapter<D> {
    void addDatas(List<D> data);

    void setDatas(List<D> data);

    void addData(D data);

    List<D> getData();

    int getDataCount();

    D getDataAt(int index);

    D removeDataAt(int position);
}
