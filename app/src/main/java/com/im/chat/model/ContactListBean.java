package com.im.chat.model;

/**
 * Created by xiaochj on 2017/3/27.
 */

public class ContactListBean {

    private int status;
    private String error;
    private int code;
    private ContactListModel data;

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

    public ContactListModel getData() {
        return data;
    }

    public void setData(ContactListModel data) {
        this.data = data;
    }
}
