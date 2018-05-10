package com.innext.szqb.ui.my.bean;

/**
 * Created by 34472 on 2016/10/19.
 */
public class WebShareBean {

    private int type;   //1为 在标题右边添加分享按钮，其他为直接弹出分享
    private String share_title; //分享标题
    private String share_body;  //分享的内容
    private String share_url;   //分享后点击打开的地址
    private String share_logo;  //分享的图标

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_body() {
        return share_body;
    }

    public void setShare_body(String share_body) {
        this.share_body = share_body;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShare_logo() {
        return share_logo;
    }

    public void setShare_logo(String share_logo) {
        this.share_logo = share_logo;
    }
}
