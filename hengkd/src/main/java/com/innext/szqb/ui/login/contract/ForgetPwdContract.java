package com.innext.szqb.ui.login.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.login.bean.CaptchaUrlBean;

/**
 * Created by hengxinyongli on 2017/2/9 0009.
 */

public interface ForgetPwdContract {
    interface View extends BaseView {
        void forgetPwdSuccess(CaptchaUrlBean captchaUrl);
    }

    interface presenter {
        void forgetPwd(String phone,
                       String type,
                       String captcha,
                       String type2);
    }
}
