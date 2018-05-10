package com.innext.szqb.ui.my.bean

/**
 * @author      : yan
 * @date        : 2017/12/29 15:41
 * @description : 查询会员状态bean
 */
data class QueryVipStateBean(var item: VipStateItem)

data class VipStateItem(
        var uid: String,    //客户编号
        var member_state: String,   //会员标识
        var expire_time: String,     //会员到期时间
        var url: String     //支付会员url
)
