package com.innext.szqb.ui.lend.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.innext.szqb.R
import com.innext.szqb.ext.onClick
import com.innext.szqb.ui.lend.bean.CompanyContent
import com.innext.szqb.ui.main.WebViewActivity
import com.innext.szqb.ui.repayment.adapter.RepaymentAdapter
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.borrow_platform_item.view.*
import kotlinx.android.synthetic.main.repayment_status_item_new1.view.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by HX0010637 on 2018/5/8.
 */
class BorrowPlatformAdapter : BaseRecyclerAdapter<BorrowPlatformAdapter.ViewHolder, CompanyContent>() {
    private var subClickListener: SubClickListener? = null
    override fun mOnCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
            ViewHolder(mInflater.inflate(R.layout.borrow_platform_item, parent, false))
    fun setsubClickListener(topicClickListener: SubClickListener) {
        this.subClickListener = topicClickListener
    }

    interface SubClickListener {
        fun OntopicClickListener(v: TextView?, position: Int)
    }
    override fun mOnBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            tv_company.text = item.title
            tv_apple_persons.text = item.apply_persons
            tv_content.text = item.content
            tv_tagline.text = item.tagline
            Glide.with(mContext)
                .load(item.pic_link)
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
                .into(pic_link)//设置图片
            tv_apply_btn.onClick{
                if (subClickListener != null) {
                    subClickListener!!.OntopicClickListener(tv_apply_btn,position)
                }
            }
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}