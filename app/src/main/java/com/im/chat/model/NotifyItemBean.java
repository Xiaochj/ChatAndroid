package com.im.chat.model;

/**
 * 通告页的item
 * Created by xiaochj on 2017/3/8.
 */

public class NotifyItemBean {

    String imgUrl;
    String title;
    String content;
    String time;
    String url;

    public NotifyItemBean(String imgUrl,String title,String content,String time,String url){
        this.imgUrl = imgUrl;
        this.title = title;
        this.content = content;
        this.time = time;
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
