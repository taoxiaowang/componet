package com.hikcreate.library.plugin.netbase.entity;

import java.util.List;

/**
 * 用于数据是列表[有分页]
 *
 * @author yslei
 * @data 2019/3/11
 * @email leiyongsheng@hikcreate.com
 */
public class ListContentResult<T> {
    private int totalCount;
    private int totalPage;
    private int page;
    private int size;
    private List<T> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean hasNextPage() {
        return page < totalPage;
    }

    @Override
    public String toString() {
        return "ListContentResult{" +
                "totalCount=" + totalCount +
                ", totalPage=" + totalPage +
                ", page=" + page +
                ", size=" + size +
                ", list=" + list +
                '}';
    }
}
