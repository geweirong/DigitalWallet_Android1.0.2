package com.innext.szqb.ui.lend.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.lend.bean.ConfirmLoanBean;

/**
 * 申请借款
 * Created by hengxinyongli at 2017/2/15 0015
 */
public interface ApplyLoanContract {
    interface View extends BaseView{
        void applyLoanSuccess();
        void applyLoanFaild(int code,String message);
    }
    interface Presenter{
        void applyLoan(String money,String pay_password, String loanUse,String loan_periods);
    }
}
