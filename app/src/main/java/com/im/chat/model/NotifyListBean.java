package com.im.chat.model;

import java.util.List;

/**
 * Created by xiaochj on 2017/3/27.
 */

public class NotifyListBean {
    private int status;
    private String error;
    private int code;
    private List<NotifyListModel> data;

    public List<NotifyListModel> getData() {
        return data;
    }

    public void setData(List<NotifyListModel> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
