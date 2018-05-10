package com.innext.szqb.ui.my.bean;

/**
 * author：wangzuzhen on 2016/9/23 0023 11:35
 */
public class TransactionRecordListBean {

    private String title;
    private String time;
    private String url;
    private String text;
    private String poolId;

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
