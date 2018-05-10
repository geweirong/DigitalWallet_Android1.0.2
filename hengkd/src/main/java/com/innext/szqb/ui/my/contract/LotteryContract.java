package com.innext.szqb.ui.my.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.my.bean.Lottery;

import java.util.List;

/**
 * Created by hengxinyongli on 2017/2/15 0015.
 * 描述：
 */

public interface LotteryContract {
    interface View extends BaseView {
        void lotterySuccess(List<Lottery.ItemBean> itemBean);
    }

    interface presenter {
        void lotteryRequest(String phone, String page, String pageSize);
    }
}
