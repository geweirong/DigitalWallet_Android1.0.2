package com.innext.szqb.ui.login.contract;/**
 * Created by hengxinyongli on 2017/2/15 0015.
 */

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */
public interface VerifyResetPwdContract {
    interface View extends BaseView {
        void verifySuccess();
    }

    interface presenter {
        void verifyResetPwd(String phone,
                            String code,
                            String realname,
                            String id_card,
                            String type,
                            String captcha);
    }
}
