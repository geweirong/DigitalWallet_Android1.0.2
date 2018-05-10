package com.innext.szqb.ui.my.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.innext.szqb.R
import com.innext.szqb.ext.getFriendDate
import com.innext.szqb.ext.getFriendMonth
import com.innext.szqb.ext.getHMStr
import com.innext.szqb.ui.my.bean.RecordData

/**
 * @author      : gwr
 * @date        : 2017/12/26 19:44
 * @description : 借款记录RecyclerView的adapter
 */
class ScoreRecordRvAdapter(layoutResId: Int, data: ArrayList<RecordData>?) :
        BaseQuickAdapter<RecordData, BaseViewHolder>(layoutResId, data) {

    override fun convert(holder: BaseViewHolder, item: RecordData) = with(item) {
        var mAmout: String = ""
        if (amount >= 0){
            mAmout = "+"+ amount
        }else{
            mAmout = amount.toString()
        }
        holder.setGone(R.id.rl_header, dateStr != null)
                .setText(R.id.tv_header_date, dateStr.getFriendMonth())
                .setText(R.id.tv_header_income, "收入:$incomeIntegral")
                .setText(R.id.tv_header_expend, "支出:$payIntegral")
                .setText(R.id.tv_date, finished_at.getFriendDate())
                .setText(R.id.tv_time, finished_at.getHMStr())
                .setText(R.id.tv_score_type, usage)
                .setText(R.id.tv_total_score, mAmout)
        Unit
    }

}