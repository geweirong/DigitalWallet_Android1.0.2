package com.innext.szqb.ui.login.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.login.bean.CaptchaUrlBean;

/**
 * Created by hengxinyongli on 2017/2/13 0013.
 */

public interface GetRegisterCodeContract {
    interface View extends BaseView{
        void getCodeSuccess(CaptchaUrlBean captchaUrl);
        void showErrorMsg(String msg,int code);
    }
    interface Presenter {
        void getRegisterCode(String phone,String captcha,String type);
    }
}
