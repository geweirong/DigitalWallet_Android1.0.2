package com.innext.szqb.ui.my.bean;

import java.util.List;

/**
 * author：wangzuzhen on 2016/9/23 0023 11:47
 */
public class TransactionBean {

    private List<TransactionRecordListBean> item;

    private String link_url;

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public List<TransactionRecordListBean> getItem() {
        return item;
    }

    public void setItem(List<TransactionRecordListBean> item) {
        this.item = item;
    }
}
