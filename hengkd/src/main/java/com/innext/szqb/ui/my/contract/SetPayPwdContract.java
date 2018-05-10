package com.innext.szqb.ui.my.contract;

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli on 2017/2/18 0018.
 */

public interface SetPayPwdContract {
    interface View extends BaseView {
        void setPayPwdSuccess();
    }

    interface Presenter {
        void setPayPwd(String password);
    }
}
