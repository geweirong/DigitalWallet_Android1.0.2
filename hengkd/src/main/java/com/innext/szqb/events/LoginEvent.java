package com.innext.szqb.events;

import android.content.Context;

import com.innext.szqb.ui.my.bean.UserInfoBean;


public class LoginEvent extends BaseEvent {

    private UserInfoBean bean;
    private Context context;
    private boolean isToNext = true;

    public boolean isToNext() {
        return isToNext;
    }

    public void setToNext(boolean toNext) {
        isToNext = toNext;
    }

    public LoginEvent(Context context, UserInfoBean bean) {
        this.context = context;
        this.bean = bean;
    }

    public LoginEvent(Context context, UserInfoBean bean, boolean isToNext) {
        this.context = context;
        this.bean = bean;
        this.isToNext = isToNext;
    }

    public UserInfoBean getBean() {
        return bean;
    }

    public void setBean(UserInfoBean bean) {
        this.bean = bean;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


}
