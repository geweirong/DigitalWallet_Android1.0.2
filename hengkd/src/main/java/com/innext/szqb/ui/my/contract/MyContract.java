package com.innext.szqb.ui.my.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.my.bean.MoreContentBean;
import com.innext.szqb.ui.my.bean.MyOrderBean;
import com.innext.szqb.ui.my.bean.QueryVipStateBean;

/**
 * Created by hengxinyongli on 2017/2/13 0013.
 */

public interface MyContract {
    interface View extends BaseView{
        void userInfoSuccess(MoreContentBean result);
        void myOrderSuccess(MyOrderBean bean);
//        void shareSuccess();
//        void judgeMemberSuccess(QueryVipStateBean queryVipStateBean);

    }
    interface Presenter {
        void getInfo();
        /**
         * 首页判断是否是会员
         */
//        void judgeMember();
        /*
         *   分享
         */
        void getShareSuccess(String deviceId,String mobilePhone);
        /*
         * 我的订单
         */
        void getOrderInfo();
    }
}
