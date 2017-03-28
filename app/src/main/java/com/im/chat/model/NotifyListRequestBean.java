package com.im.chat.model;

/**
 * Created by xiaochj on 2017/3/27.
 */

public class NotifyListRequestBean {

    private int pageNo;
    private int pageSize;

    public NotifyListRequestBean(int pageNo,int pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
