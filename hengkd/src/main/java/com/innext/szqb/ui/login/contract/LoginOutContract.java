package com.innext.szqb.ui.login.contract;

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli on 2017/2/13 0013.
 */

public interface LoginOutContract {
    interface View extends BaseView{
        void loginOutSuccess();
    }
    interface Presenter{
        void loginOut();
    }
}
