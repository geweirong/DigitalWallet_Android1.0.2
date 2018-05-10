package com.innext.szqb.ui.my.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HX0010637 on 2018/5/7.
 */

public class BorrowingRecordBean {
    private List<AssetRepaymentList> assetRepaymentList;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<AssetRepaymentList> getAssetRepaymentList() {
        return assetRepaymentList;
    }

    public void setAssetRepaymentList(List<AssetRepaymentList> assetRepaymentList) {
        this.assetRepaymentList = assetRepaymentList;
    }
}
