package com.innext.szqb.ui.lend.bean;

/**
 * Created by hengxinyongli on 2017/3/31 0031.
 */

public class ActivityBean {

    /**
     * tcUrl : test11
     * tcImage :
     * tcStatus : 0
     */

    private String tcUrl;
    private String tcImage;
    private String tcStatus;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTcUrl() {
        return tcUrl;
    }

    public void setTcUrl(String tcUrl) {
        this.tcUrl = tcUrl;
    }

    public String getTcImage() {
        return tcImage;
    }

    public void setTcImage(String tcImage) {
        this.tcImage = tcImage;
    }

    public String getTcStatus() {
        return tcStatus;
    }

    public void setTcStatus(String tcStatus) {
        this.tcStatus = tcStatus;
    }
}
