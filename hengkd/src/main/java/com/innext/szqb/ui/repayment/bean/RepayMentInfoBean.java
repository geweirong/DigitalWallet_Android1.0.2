package com.innext.szqb.ui.repayment.bean;

/**
 * Created by HX0010637 on 2018/4/9.
 */

public class RepayMentInfoBean {
    private String msg;   //":"您将支付135.0元延期还款费用延期后还款日可延后30天",
    private String money;  //":135
    private String url;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
