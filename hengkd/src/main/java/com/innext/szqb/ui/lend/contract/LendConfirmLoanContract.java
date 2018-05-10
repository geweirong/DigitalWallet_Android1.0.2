package com.innext.szqb.ui.lend.contract;


import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.lend.bean.ConfirmLoanBean;

/**
 * Created by hengxinyongli on 2017/5/16.
 */

public interface LendConfirmLoanContract {
    interface View extends BaseView {
        void toLoanSuccess(ConfirmLoanBean result);
        void borrowDataSuccess(ConfirmLoanBean confirmLoanBean);
    }
    interface Presenter{

        /**
         * 验证借款信息
         * @param money
         * @param period
         */
        void toLoan(String money, String period,String loan_perioed);
        void borrowData();
    }
}
