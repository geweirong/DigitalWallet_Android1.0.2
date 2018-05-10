package com.innext.szqb.ui.my.activity

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.innext.szqb.R
import com.innext.szqb.app.App
import com.innext.szqb.base.BaseActivity
import com.innext.szqb.ui.my.contract.FeedBackContract
import com.innext.szqb.ui.my.presenter.FeedBackPresenter
import com.innext.szqb.util.common.ToastUtil
import com.innext.szqb.util.Tool
import kotlinx.android.synthetic.main.activity_setting_feed_back.*
import java.util.*

/**
 * Created by hengxinyongli on 2017/2/15 0015.
 */

class FeedBackActivity : BaseActivity<FeedBackPresenter>(), FeedBackContract.View {
    /**
     * 反馈意见是否为空
     */
    private var sign1 = false
    private var qqList: Array<String>? = null//客服qq
    private val random: Random = Random()
    private var holiday: String = ""
    private var peacetime: String = ""
    private var qq_group: String = ""
    private var service_phone: String = ""
    private var qqGroupList: ArrayList<String>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_setting_feed_back
    }

    override fun initPresenter() {
        mPresenter.init(this)
    }

    override fun loadData() {
        mTitle.setTitle(true, { finish() }, "意见反馈")
        input_feedback.addTextChangedListener(inputWatcher)
        if (intent != null) {
            holiday = intent.getStringExtra("holiday")
            peacetime = intent.getStringExtra("peacetime")
            qq_group = intent.getStringExtra("qq_group")
            service_phone = intent.getStringExtra("service_phone")
            qqGroupList = intent.getStringArrayListExtra("services_qq")
        }
        init()
    }

    fun init() {
        if (!Tool.isBlank(peacetime) && !Tool.isBlank(holiday) && !Tool.isBlank(qq_group)) {
            tv_worktime.text = "平时: " + peacetime
            tv_holiday.text = "节假: " + holiday
            tv_qq_group.text = "客服QQ: " + qq_group
        }
        tv_submit.setOnClickListener {
            commit()
        }
    }

    private val inputWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (input_feedback.text.length > 160) {
                number.text = "0/160"
            } else {
                number.text = (160 - input_feedback.text.length).toString() + "/160"
            }

            sign1 = input_feedback.text.length > 0

            tv_submit.isEnabled = sign1
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    private fun commit() {
        if (Tool.isFastDoubleClick()) {
            return
        }
        val inPutFeedbackStr = input_feedback.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(inPutFeedbackStr)) {
            ToastUtil.showToast("请输入您的反馈内容")
            return
        }

        if (containsEmoji(inPutFeedbackStr)) {
            ToastUtil.showToast("请勿输入表情符号")
            return
        }
        App.loadingDefault(this)
        mPresenter.feedBack(inPutFeedbackStr)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (currentFocus != null && currentFocus.windowToken != null) {
                manager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        return super.onTouchEvent(event)
    }

    override fun feedBackSuccess() {
        ToastUtil.showToast("感谢您的反馈，我们会第一时间处理！给您带来的不便，敬请谅解")
        input_feedback.setText("")
        finish()
    }

    override fun showLoading(content: String) {
        App.loadingContent(this, content)
    }

    override fun stopLoading() {
        App.hideLoading()
    }

    override fun showErrorMsg(msg: String, type: String) {
        ToastUtil.showToast(msg)
    }

    val qq: String
        get() {
            return if (qqGroupList != null) {
                qqGroupList!![random.nextInt(qqGroupList!!.size)]
            } else {
                if (null == qqList) {
                    qqList = arrayOf("3356190848")
                }
                qqList!![random.nextInt(qqList!!.size)]
            }

        }

    companion object {
        /**
         * 检测是否有emoji表情
         *
         * @param source
         * @return
         */
        fun containsEmoji(source: String): Boolean {
            val len = source.length
            for (i in 0..len - 1) {
                val codePoint = source[i]
                if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                    return true
                }
            }
            return false
        }


        /**
         * 判断是否是Emoji
         *
         * @param codePoint
         * 比较的单个字符
         * @return
         */
        private fun isEmojiCharacter(codePoint: Char): Boolean {
            return codePoint.toInt() == 0x0 || codePoint.toInt() == 0x9 || codePoint.toInt() == 0xA
                    || codePoint.toInt() == 0xD
                    || codePoint.toInt() >= 0x20 && codePoint.toInt() <= 0xD7FF
                    || codePoint.toInt() >= 0xE000 && codePoint.toInt() <= 0xFFFD
                    || codePoint.toInt() >= 0x10000 && codePoint.toInt() <= 0x10FFFF
        }
    }
}
