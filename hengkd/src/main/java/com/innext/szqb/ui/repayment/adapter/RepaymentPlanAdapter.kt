package com.innext.szqb.ui.repayment.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.innext.szqb.R
import com.innext.szqb.ui.repayment.bean.RepayList

/**
 * Created by HX0010637 on 2018/4/28.
 */
class RepaymentPlanAdapter (layoutResId: Int, data: ArrayList<RepayList>?) :
        BaseQuickAdapter<RepayList, BaseViewHolder>(layoutResId, data){
    override fun convert(holder: BaseViewHolder, item: RepayList) = with(item){
        var totalAmount1 = totalAmount!! /100.00
        holder.setText(R.id.tv_period,"第$period"+"期")
        .setText(R.id.tv_expectRepayDate,"$expectRepayDate")
        .setText(R.id.tv_totalAmount,"￥"+totalAmount1)
        Unit
    }

}