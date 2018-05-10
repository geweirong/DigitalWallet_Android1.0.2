package com.innext.szqb.ui.login.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.my.bean.UserInfoBean;

/**
 * Created by hengxinyongli on 2017/2/9 0009.
 */

public interface LoginContract {
    interface View extends BaseView{
        void loginSuccess(UserInfoBean bean);
    }
    interface presenter{
        void login(String username,String password);
    }
}
