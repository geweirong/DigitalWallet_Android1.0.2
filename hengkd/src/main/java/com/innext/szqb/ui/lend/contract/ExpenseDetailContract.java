package com.innext.szqb.ui.lend.contract;

import com.innext.szqb.base.BaseView;
import com.innext.szqb.ui.lend.bean.ExpenseDetailBean;

import java.util.List;

/**
 * 首页费用详情
 * Created by hengxinyongli on 2017/3/7 0007.
 */

public interface ExpenseDetailContract {
    interface View extends BaseView{
        void loadExpenseDetailSuccess(List<ExpenseDetailBean> result);
    }
    interface Presenter {
        void loadExpenseDetail(String money,String day);
    }
}
