package com.innext.szqb.ui.login.contract;/**
 * Created by hengxinyongli on 2017/2/16 0016.
 */

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.my.bean.UserInfoBean;

/**
 * Created by hengxinyongli at 2017/2/16 0016
 */
public interface RegisterContract {
    interface View extends BaseView {
        void registerSuccess(UserInfoBean info);

        void registerError();
    }

    interface Presenter {
        void toRegister(String phone,
                        String code,
                        String password,
                        String source,
                        String invite_code,
                        String user_from,
                        String captcha);
    }
}
