package com.im.chat.model;

/**
 * 登陆的bean
 * Created by xiaochj on 2017/3/27.
 */

public class LoginBean {

    private int status;
    private String error;
    private int code;
    private LoginModel data;

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

    public LoginModel getData() {
        return data;
    }

    public void setData(LoginModel data) {
        this.data = data;
    }
}
