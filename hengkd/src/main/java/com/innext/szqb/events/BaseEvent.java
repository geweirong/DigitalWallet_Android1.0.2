package com.innext.szqb.events;

import android.content.Context;

public class BaseEvent {

    private Context applicationContext;
    private UIBaseEvent uiEvent;

    public BaseEvent() {
    }


    public BaseEvent(UIBaseEvent uiEvent) {
        this.uiEvent = uiEvent;
    }

    public UIBaseEvent getUiEvent() {
        return uiEvent;
    }

    public void setUiEvent(UIBaseEvent uiEvent) {
        this.uiEvent = uiEvent;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

}
