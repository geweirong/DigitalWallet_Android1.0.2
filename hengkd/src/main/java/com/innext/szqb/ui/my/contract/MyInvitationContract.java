package com.innext.szqb.ui.my.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.my.bean.InvitationDataInfo;

/**
 * Created by HX0010637 on 2018/1/29.
 */

public interface MyInvitationContract {
    interface View extends BaseView {
        void myCodeSuccess(InvitationDataInfo bean);
    }
    interface presenter{
        void getCode();
    }
}
