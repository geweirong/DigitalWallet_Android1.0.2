package com.innext.szqb.events;

import android.content.Context;


public class LoanEvent extends BaseEvent {

    private Context context;

    public LoanEvent(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


}
