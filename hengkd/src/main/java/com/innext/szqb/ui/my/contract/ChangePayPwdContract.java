package com.innext.szqb.ui.my.contract;

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli on 2017/2/18 0018.
 */

public interface ChangePayPwdContract {
    interface View extends BaseView {
        void changePayPwdSuccess();
    }

    interface Presenter {
        void changePayPwd(String old_pwd, String new_pwd);
    }
}
