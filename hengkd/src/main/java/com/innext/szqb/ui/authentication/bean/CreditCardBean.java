package com.innext.szqb.ui.authentication.bean;

import java.util.List;

/**
 * Created by HX0010637 on 2018/5/2.
 */

public class CreditCardBean {
    public List<EmailInfo> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<EmailInfo> emailList) {
        this.emailList = emailList;
    }

    private List<EmailInfo> emailList;
}
