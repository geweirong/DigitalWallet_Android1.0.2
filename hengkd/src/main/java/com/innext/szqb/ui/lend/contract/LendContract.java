package com.innext.szqb.ui.lend.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.lend.bean.AgreeStateBean;
import com.innext.szqb.ui.lend.bean.ConfirmLoanBean;
import com.innext.szqb.ui.lend.bean.ExpenseDetailBean;
import com.innext.szqb.ui.lend.bean.HomeInfoResponseBean;
import com.innext.szqb.ui.my.bean.BorrowingRecordBean;

import java.util.List;

/**
 * Created by hengxinyongli at 2017/2/15 0015
 */

public interface LendContract {
    interface View extends BaseView{
//        void updateAssetOrderSuccess();
        void infoSuccess(HomeInfoResponseBean result);
        void infoAgreeStateSuccess(AgreeStateBean result);
    }
    interface Presenter{
        //首页数据，新接口
        void loadInfo();
        //修改同意收取报告费状态接口
        void loadStateInfo();
//        void updateAssetOrderPool(Integer userid);
    }
}
