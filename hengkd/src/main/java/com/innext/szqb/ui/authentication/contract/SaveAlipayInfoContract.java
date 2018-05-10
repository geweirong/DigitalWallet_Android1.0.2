package com.innext.szqb.ui.authentication.contract;

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli on 2017/2/20 0020.
 */

public interface SaveAlipayInfoContract {
    interface View extends BaseView{
        void saveInfoSuccess(String message);
    }
    interface Presenter{
        void toSaveInfo(String data);
    }
}
