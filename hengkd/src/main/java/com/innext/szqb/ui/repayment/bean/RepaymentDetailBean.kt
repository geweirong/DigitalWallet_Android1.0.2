package com.innext.szqb.ui.repayment.bean

/**
 * @author      : yan
 * @date        : 2017/12/18 10:32
 * @description : 还款列表明细bean
 */
data class RepaymentDetailBean(
	var  assetOrderId: String?,       //分期表订单id
	var  createdAt	: String?,       //借款时间 例：2018-04-28
	var  loanTerm: String?,        //	借款期限 例：30天或3个月，6个月
	var  repayMethod: String?,     //还款方式 例：等额本息
	var  moneyAmount: String?, //	借款金额 （单位元）,                                                                    #借款金额
	var  contractImg: String?,     //	var  http://172.30.2.122:8080/hkd_api/mnt/contract/9815232403584433.png	var    #合同地址
    var  status: Int?           //0   审核中 1   审核不通过2   放款中3   还款中4   已逾期5   已还完
)