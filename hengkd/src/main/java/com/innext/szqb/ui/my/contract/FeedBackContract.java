package com.innext.szqb.ui.my.contract;

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli on 2017/2/15 0015.
 */

public interface FeedBackContract {
    interface View extends BaseView {
        void feedBackSuccess();
    }
    interface Presenter {
        void feedBack(String content);
    }
}
