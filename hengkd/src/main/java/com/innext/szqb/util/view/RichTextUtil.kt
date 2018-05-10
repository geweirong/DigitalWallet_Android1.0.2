package com.innext.szqb.util.view

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan

/**
 * @author      : HX0010239ANDROID
 * @date        : 2017/11/20 11:24
 * @description : 富文本工具类
 */
object RichTextUtil {

    /**
     * 设置打客服电话富文本
     */
    fun setCallDialogRichText(phoneNum: String): SpannableString {
        val pre = "您将要拨打客服热线 : "
        val last = " ? "
        val ss = SpannableString(pre + phoneNum + last)
        //设置颜色
        ss.setSpan(ForegroundColorSpan(Color.BLACK), 0, pre.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(ForegroundColorSpan(Color.RED), pre.length, pre.length + phoneNum.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    fun setDialogTitle(title: String, color: Int): SpannableString {
        val ss = SpannableString(title)
        ss.setSpan(ForegroundColorSpan(color), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置富文本,以文本->颜色形式传递
     * 如：setRichText("123" to Color.BLACK, "haha" to Color.RED)    -->"123haha"  其中123为黑色，haha为红色
     */
    fun setRichText(vararg pair: Pair<String, Int>): SpannableString {
        val sb = StringBuilder()
        pair.forEach { sb.append(it.first) }

        val ss = SpannableString(sb.toString())
        val newSb = StringBuilder()
        pair.forEach {
            val preLength = newSb.length
            val lastLength = newSb.append(it.first).length
            //设置颜色
            ss.setSpan(ForegroundColorSpan(it.second), preLength, lastLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return ss
    }
}