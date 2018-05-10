package com.innext.szqb.events;

import android.content.Context;

public class LogoutEvent extends BaseEvent {

    private Context context;
    private int TAG;//为0时（登录失效时）调用退出接口，为1时（手动退出已调用退出接口）不调用

    public LogoutEvent(Context context, int tag) {
        this.context = context;
        this.TAG = tag;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getTAG() {
        return TAG;
    }

    public void setTAG(int TAG) {
        this.TAG = TAG;
    }

}
