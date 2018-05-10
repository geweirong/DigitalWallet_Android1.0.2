package com.innext.szqb.ui.repayment.bean

/**
 * Created by HX0010637 on 2018/4/28.
 */
    data class RepaymentPlanBean (
            var totalRepayAmount: Int?,  //": 153000,还款总额
            var totalRepayInterestAmount: Int?, //": 37500,利息总额
            var repayList: ArrayList<RepayList>?
    )
    data class RepayList (
        var period: Int?,  //": 1,
        var totalAmount: Int?, //: 153000, 每期还款总额
        var principal: Int?, //112500,  每期应还本金
        var interest: Int?, //37500, 每期应还利息
        var expectRepayDate: String?  //": "2018-04-27"
    )