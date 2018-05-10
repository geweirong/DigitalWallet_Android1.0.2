package com.innext.szqb.ui.login.contract;

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli on 2017/2/16 0016.
 */

public interface UpdataPwdContract {
    interface View extends BaseView {
        void UpdataPwdSuccess();
    }

    interface Presenter {
        void UpdataPwd(String old_pwd,
                      String new_pwd);
    }
}
