package com.innext.szqb.ui.my.adapter

import android.os.Build
import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.innext.szqb.R
import com.innext.szqb.config.Constant
import com.innext.szqb.ui.authentication.bean.SelectPicBean
import com.innext.szqb.util.check.StringUtil
import kotlinx.android.synthetic.main.layout_upload_pic_item.view.*

/**
 * @author      : yan
 * @date        : 2017/12/28 17:37
 * @description : 央行征信RecyclerView的adapter
 */
class CbCreditRvAdapter(layoutResId: Int, data: MutableList<SelectPicBean>?) :
        BaseQuickAdapter<SelectPicBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(holder: BaseViewHolder, item: SelectPicBean?) = with(holder.itemView) {
        if (item == null) return@with

        holder.addOnClickListener(R.id.iv_delete)
        tv_upload_text.visibility = View.GONE
        when (item.type) {
            SelectPicBean.Type_None -> {
                tv_status.visibility = View.GONE
                iv_delete.visibility = View.VISIBLE
            }
            SelectPicBean.Type_Add -> {
                tv_upload_text.visibility = View.VISIBLE
                tv_status.visibility = View.GONE
                iv_delete.visibility = View.GONE
                iv_pic.setImageResource(R.mipmap.add_pic_normal)
            }
            SelectPicBean.Type_Uploaded -> {
                tv_status.visibility = View.VISIBLE
                iv_delete.visibility = View.GONE
                tv_status.text = "已上传"
            }
            SelectPicBean.Type_Uploading -> {
                tv_status.visibility = View.VISIBLE
                iv_delete.visibility = View.GONE
                tv_status.text = "上传中..."
            }
            SelectPicBean.Type_UploadFailed -> {
                tv_status.visibility = View.VISIBLE
                iv_delete.visibility = View.VISIBLE
                tv_status.text = "上传失败"
            }
        }

        if (!StringUtil.isBlank(item.url)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv_pic.transitionName = Constant.TRANSITION_ANIMATION_SHOW_PIC + getParentPosition(item)
            }
            Glide.with(mContext)
                    .load(item.url)
                    .centerCrop()
                    .placeholder(R.drawable.image_default)
                    .error(R.drawable.image_default)
                    .crossFade()
                    .into(iv_pic)
        }
    }
}