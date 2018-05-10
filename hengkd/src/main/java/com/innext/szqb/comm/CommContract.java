package com.innext.szqb.comm;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.login.bean.CaptchaUrlBean;

/**
 * Created by hengxinyongli on 2017/2/13 0013.
 */

public interface CommContract {
    interface View extends BaseView{
        void getVerifyCodeSuccess(CaptchaUrlBean captchaUrl);
        void showCodeErrorMsg(String msg, int code);
    }
    interface Presenter {
        void getVerifyCode(String phone, String captcha, String type);
    }
}
