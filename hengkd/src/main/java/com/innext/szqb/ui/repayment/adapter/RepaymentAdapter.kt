package com.innext.szqb.ui.repayment.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.innext.szqb.R
import com.innext.szqb.ext.onClick
import com.innext.szqb.ui.main.WebViewActivity
import com.innext.szqb.ui.my.bean.AssetRepaymentList
import com.innext.szqb.ui.my.bean.BorrowingRecordBean
import com.innext.szqb.ui.repayment.bean.RepayMentInfoBean
import com.innext.szqb.ui.repayment.bean.RepaymentListBean
import com.innext.szqb.util.TimeUtil
import com.innext.szqb.util.Tool
import com.innext.szqb.util.view.RichTextUtil
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.repayment_status_item_new1.view.*


/**
 * @author      : yan
 * @date        : 2017/12/27 19:24
 * @description : 还款列表适配器
 */
class RepaymentAdapter : BaseRecyclerAdapter<RepaymentAdapter.ViewHolder, AssetRepaymentList>() {
    private var subClickListener: SubClickListener? = null
    override fun mOnCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(mInflater.inflate(R.layout.repayment_status_item_new1, parent, false))

    fun setsubClickListener(topicClickListener: SubClickListener) {
        this.subClickListener = topicClickListener
    }

    interface SubClickListener {
        fun OntopicClickListener(v: TextView?,position: Int)
    }

    @SuppressLint("SetTextI18n")
    override fun mOnBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            tv_sum.text = "￥"+ Tool.toDeciDouble2(item.repaymentAmount.toDouble()/100)
            tv_repayment.onClick {
                if (subClickListener != null) {
                    subClickListener!!.OntopicClickListener(tv_repayment,position)
                }
            }
            tv_delay.onClick {
                if (subClickListener != null) {
                    subClickListener!!.OntopicClickListener(tv_delay,position)
                }
            }
        }
        //设置详细信息，服务费、逾期费等
        setDetailText(holder)
        //状态设置
        setState(holder)
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailText(holder: ViewHolder) {
        holder.itemView.apply {
            tv_repayment_time.text ="请于"+ TimeUtil.formatData(TimeUtil.dateFormatMDofChinese,
                    item.repaymentTime!!.toLong()) + "前还款"
        }
    }
    private fun setState(holder: ViewHolder) {
        val status = item.status
        var str = ""
        var textColor = 0
        var isShowEnter = false
        when (status) {
            21 -> { //待还款
                str = "待还款"
                isShowEnter = false
                textColor = R.color.c_334fe0
            }
            30, 34 -> { //已还款
                str = "已还清"
                textColor = R.color.c_666666
                isShowEnter = false
            }
            -11, -20 -> {   //已逾期、已坏账
                str = "已逾期"
                isShowEnter = true
                textColor = R.color.c_ff4949
            }
        }
        val colorInt = mContext.resources.getColor(textColor)
        val ss = RichTextUtil.setRichText(str to colorInt)
        holder.itemView.apply {
            tv_status.text = ss
            //是否显示enter图片
            tv_status.visibility = if (isShowEnter) View.VISIBLE else View.INVISIBLE
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}