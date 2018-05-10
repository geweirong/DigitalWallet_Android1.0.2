package com.innext.szqb.ui.my.contract;

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli on 2017/2/13 0013.
 */

public interface UploadPOIContract {
    interface View extends BaseView{
        void uploadPOISuccess();
    }
    interface Presenter {
        void uploadPOI(String longitude,
                       String latitude,
                       String address,
                       String time);
    }
}
