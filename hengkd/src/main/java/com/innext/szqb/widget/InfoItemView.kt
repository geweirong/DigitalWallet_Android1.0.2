package com.innext.szqb.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.innext.szqb.R
import kotlinx.android.synthetic.main.info_item_view.view.*

/**
 * @author      : yan
 * @date        : 2017/12/27 10:28
 * @description : 完善资料单个条目
 */
class InfoItemView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    constructor(context: Context) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.info_item_view, this)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.InfoItemView)

        val leftImgResId = ta.getResourceId(R.styleable.InfoItemView_iivLeftImage, R.mipmap.ic_personlinfor)
        val leftText = ta.getString(R.styleable.InfoItemView_iivLeftText)
        val rightText = ta.getString(R.styleable.InfoItemView_iivRightText) ?: "去认证"
        val greyLineVisible = ta.getBoolean(R.styleable.InfoItemView_iivLineVisible, true)

        //设置数据
        iv_left_icon.setImageResource(leftImgResId)
        tv_left_text.text = leftText
        tv_right_text.text = rightText
        bottom_grey_line.visibility = if (greyLineVisible) View.VISIBLE else View.INVISIBLE

        ta.recycle()
    }

    fun setRightText(str: String, colorId: Int = R.color.c_999999, hideEnter: Boolean = true) = with(tv_right_text) {
        text = str
        setTextColor(resources.getColor(colorId))
        hideIvEnter(hideEnter)
    }

    fun hideIvEnter(hideEnter: Boolean) {
        iv_enter.visibility = if (hideEnter) View.INVISIBLE else View.VISIBLE
    }
}