package com.innext.szqb.ui.authentication.bean;

import java.util.List;

/**
 * 作者：${hengxinyongli} on 2017/2/14 0014 15:16
 * <p>
 * 邮箱：3244345578@qq.com
 */
public class PertfecInformationBean {
    private int real_verify_status;//
    private int contacts_status;//认证状态
    private int mustBeCount;//必填项认证数
    private List<AuthenticationinformationBean> list;
    private List<AuthenticationinformationBean> noMustBeList;
    private List<AuthenticationinformationBean> isMustBeList;

    public int getReal_verify_status() {
        return real_verify_status;
    }

    public void setReal_verify_status(int real_verify_status) {
        this.real_verify_status = real_verify_status;
    }

    public int getContacts_status() {
        return contacts_status;
    }

    public void setContacts_status(int contacts_status) {
        this.contacts_status = contacts_status;
    }

    public int getMustBeCount() {
        return mustBeCount;
    }

    public void setMustBeCount(int mustBeCount) {
        this.mustBeCount = mustBeCount;
    }

    public List<AuthenticationinformationBean> getList() {
        return list;
    }

    public void setList(List<AuthenticationinformationBean> list) {
        this.list = list;
    }

    public List<AuthenticationinformationBean> getNoMustBeList() {
        return noMustBeList;
    }

    public void setNoMustBeList(List<AuthenticationinformationBean> noMustBeList) {
        this.noMustBeList = noMustBeList;
    }

    public List<AuthenticationinformationBean> getIsMustBeList() {
        return isMustBeList;
    }

    public void setIsMustBeList(List<AuthenticationinformationBean> isMustBeList) {
        this.isMustBeList = isMustBeList;
    }
}
