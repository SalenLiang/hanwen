package com.fahai.cc.service.util;

import java.io.Serializable;
import java.util.List;

/**
 * 分页相关信息
 * @param <T>
 */
public class Page<T> implements Serializable{

    //当前页码
    private int currentPage = 1;
    //数据总数
    private int totalCount;
    //返回结果集
    private List<T> list;
    //每页默认条数
    private int pageSize = 15;
    //总共多少页
    private int totalPage;

    public Page() {
    }

    public Page(int currentPage, int totalCount, int pageSize,List<T> list) {
        this.currentPage = currentPage;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.list = list;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 页数为计算出的结果值，不能进行人工set
     * @return 页数
     */
    public int getTotalsPage() {
        return totalCount % pageSize ==0 ? totalCount/pageSize : totalCount/pageSize+1;
    }

}
