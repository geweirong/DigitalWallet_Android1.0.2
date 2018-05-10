package com.innext.szqb.ui.login.contract;/**
 * Created by hengxinyongli on 2017/2/15 0015.
 */

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */
public interface ResetPwdContract {
    interface View extends BaseView {
        void resetPwdSuccess();
    }

    interface Presenter {
        void resetPwd(String phone,
                      String code,
                      String password);
    }
}
