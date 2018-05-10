package com.innext.szqb.ext

import android.annotation.SuppressLint
import com.innext.szqb.util.TimeUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author      : gwr
 * @date        : 2018/4/3 11:37
 * @description : String 的扩展方法
 */

/**
 * 根据传过来的时间字符串返回年份
 * 如：传入2018-01-02 10:10,返回2018
 */
fun String?.getYearStr(): String = this?.substring(0, 4) ?: ""

/**
 * 根据传过来的时间字符串返回年份
 * 如：传入2018-01-02 10:10,返回2018
 */
fun String?.getMonthStr(): String = this?.substring(5, 7) ?: ""

/**
 * 根据传过来的时间字符串返回月日
 * 如：传入2018-01-02 10:10,返回2018-01
 */
fun String?.getYMStr(): String = this?.substring(0, 7) ?: ""

/**
 * 根据传过来的时间字符串返回月份
 * 如：传入2018-01-02 10:10,返回01-02
 */
fun String?.getMDStr(): String = this?.substring(5, 10) ?: ""

/**
 * 根据传过来的时间字符串返回时分
 * 如：传入2018-01-02 10:10,返回10:10
 */
fun String?.getHMStr(): String = this?.let { split(" ")[1] } ?: ""

/**
 * 日期格式转换
 */
@SuppressLint("SimpleDateFormat")
fun String?.dateFormat(): String {
    val pat1 = "yyyy-MM"
    val pat2 = "yyyy年MM月"
    val sdf1 = SimpleDateFormat(pat1)
    val sdf2 = SimpleDateFormat(pat2)
    var d: Date? = null
    try {
        d = sdf1.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return sdf2.format(d)
}

/**
 * 根据传过来的时间字符串与当前时间对比返回友好提示
 * 如：传入2018-01-02 10:10,返回昨天
 */
@SuppressLint("SimpleDateFormat")
fun String?.getFriendDate(): String {
    return this?.let {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val offsetDay = TimeUtil.getOffectDay(Date().time, format.parse(it).time)

        when {
            offsetDay <= 0 -> "今天"
            offsetDay == 1 -> "昨天"
            else           -> getMDStr().replace("-", "/")
        }
    } ?: ""
}

/**
 * 根据传过来的时间字符串与当前时间对比返回友好提示
 * 如：传入2018-01-02 10:10,返回本月
 */
@SuppressLint("SimpleDateFormat")
fun String?.getFriendMonth(): String {
    return this?.let {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val year = getYearStr().toInt()

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val month = getMonthStr().toInt()
        if (currentYear == year) {
            if (currentMonth == month) "本月"
            else getMonthStr() + "月"
        } else {
            dateFormat()
        }
    } ?: ""
}