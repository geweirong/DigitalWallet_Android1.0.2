package com.innext.szqb.ui.repayment.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.my.bean.BorrowingRecordBean;
import com.innext.szqb.ui.repayment.bean.RepayMentInfoBean;

/**
 * 还款
 */

public interface RepaymentContract {
    interface View extends BaseView{
        void repaymentInfoSuccess(RepayMentInfoBean result);
        void repaymentInfoStatusSuccess(RepayMentInfoBean result);
        void showListAssetRepaymentSuccess(BorrowingRecordBean bean);
    }

    interface Presenter {
        void gteRepaymentInfo(String assetId);
        void getPaymentStatus(String assetOrderId);
        void getListAssetRepayment(String assetOrderId);
    }
}
