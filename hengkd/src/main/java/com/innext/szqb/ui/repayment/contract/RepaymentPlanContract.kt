package com.innext.szqb.ui.repayment.contract

import com.innext.szqb.base.BaseView
import com.innext.szqb.ui.repayment.bean.RepaymentPlanBean

/**
 * Created by HX0010637 on 2018/5/2.
 */
    interface RepaymentPlanContract {
        interface View : BaseView {
            fun getRepaymentPlanSuccess(bean: RepaymentPlanBean)
            fun getRepaymentPlanListSuccess(bean: RepaymentPlanBean)

        }

        interface Presenter {
            fun getRepaymentPlanList(borrowAmount: String,productNo: String)  //获取预期还款计划列表
            fun getRepaymentPlan(assetOrderId: String)      //借款详情还款计划接口
        }
    }