package com.innext.szqb.widget.refresh.base;

/**
 * Created by hengxinyongli on 2015/8/17.
 */
public interface SwipeTrigger {
    void onPrepare();

    void onSwipe(int y, boolean isComplete);

    void onRelease();

    void complete();

    void onReset();
}
