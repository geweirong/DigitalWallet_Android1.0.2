package com.innext.szqb.ui.my.contract;/**
 * Created by hengxinyongli on 2017/2/15 0015.
 */

import com.innext.szqb.base.BaseView;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */
public interface DeviceReportContract  {
    interface View extends BaseView{
        void deviceReportSuccess();
    }
    interface Presenter{
        void deviceReport(String device_id,
                          String installed_time,
                          String uid,
                          String username,
                          String net_type,
                          String identifyID,
                          String appMarket);
    }
}
