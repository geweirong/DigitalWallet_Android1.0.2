package com.innext.szqb.ui.authentication.states

import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.innext.szqb.app.App
import com.innext.szqb.config.Constant
import com.innext.szqb.dialog.AlertFragmentDialog
import com.innext.szqb.ui.authentication.activity.AuthEmergencyContactActivity
import com.innext.szqb.ui.authentication.activity.CreditCardActivity
import com.innext.szqb.ui.authentication.activity.PersonalInformationActivity
import com.innext.szqb.ui.authentication.bean.PerfectInfoBean
import com.innext.szqb.ui.authentication.presenter.PerfectInfoPresenter
import com.innext.szqb.ui.main.WebViewActivity
import org.jetbrains.anko.startActivity

/**
 * @author      : yan
 * @date        : 2017/12/29 15:21
 * @description : 认证状态处理器
 */
class PerfectStateProcessor(val context: FragmentActivity) {
    var bean: PerfectInfoBean? = null
        set(value) {
            field = value
            setStates()
        }

    /**
     * 所有认证状态，包括申请状态
     */
    var personalInfoState = 0
    var contactsState = 0
    var mobileOperationState = 0
    var zmCreditState = 0
    var gjjState = 0
    var xykState = 0
    var applyState = 0
    /** 是否能提交申请，没有对applyState进行判断 */
    var canApply = false

    private fun setStates() {
        bean?.let {
            applyState = it.commitStatus
            it.VerificationList.forEach {
                when (it.code) {
                    Constant.CODE_PERSON_INFO     -> personalInfoState = it.status //个人信息
                    Constant.CODE_CONTACTS        -> contactsState = it.status //联系人
                    Constant.CODE_MOBILE_OPERATOR -> mobileOperationState = it.status //运营商
                    Constant.CODE_ZM_CREDIT       -> zmCreditState = it.status   //芝麻信用
                    Constant.CODE_GJJ             -> gjjState = it.status //公积金
                    Constant.CODE_XYK             -> xykState = it.status //信用卡
                }
            }
        }
        if (personalInfoState == 1 && contactsState == 1 && mobileOperationState == 2
                && zmCreditState == 2)
            canApply = true
    }

    fun clickPersonalInfo() {
        if (applyState == 0) context.startActivity<PersonalInformationActivity>()
    }

    fun clickContacts() {
        if (applyState == 1) return
        if (personalInfoState == 0)
            showDialog("请先填写个人信息")
        else
            context.startActivity<AuthEmergencyContactActivity>()
    }

    fun clickMobileOperator() {
        if (applyState == 1) return
        when {
            personalInfoState == 0    -> showDialog("请先填写个人信息")
            contactsState == 0        -> showDialog("请先填写联系人")
            mobileOperationState == 1 -> {
                //没认证运营商
                var url: String? = null
                bean?.let {
                    it.VerificationList.forEach {
                        if (Constant.CODE_MOBILE_OPERATOR == it.code) url = it.url
                    }
                }
                context.startActivity<WebViewActivity>("url" to url)
            }
        }
    }

    fun clickZmCredit() {
        if (applyState == 1) return
        when {
            personalInfoState == 0    -> showDialog("请先填写个人信息")
            contactsState == 0        -> showDialog("请先填写联系人")
            mobileOperationState != 2 -> showDialog("请先认证运营商")
            zmCreditState != 2        -> {
                //没认证芝麻信用
                var url: String? = null
                bean?.let {
                    it.VerificationList.forEach {
                        if (Constant.CODE_ZM_CREDIT == it.code) url = it.url
                    }
                }
                context.startActivity<WebViewActivity>("url" to url)
            }
        }
    }

    fun clickXyk(presenter: PerfectInfoPresenter) {
        if (applyState == 1) {
            if (xykState == 0) {
                //没认证信用卡,才可以认证
//                presenter.moXieApprove(TYPE_GJJ)
                val intent = Intent(context, CreditCardActivity::class.java)
                context.startActivity(intent)
            }

        } else {
            when {
                personalInfoState == 0    -> showDialog("请先填写个人信息")
                contactsState == 0        -> showDialog("请先填写联系人")
                mobileOperationState != 2 -> showDialog("请先认证运营商")
                xykState == 0             -> {
                    //没认证信用卡
                    val intent = Intent(context, CreditCardActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    fun clickApply(presenter: PerfectInfoPresenter) {
        if (applyState == 1) return
        when {
            personalInfoState == 0    -> showDialog("请先填写个人信息")
            contactsState == 0        -> showDialog("请先填写联系人")
            mobileOperationState != 2 -> showDialog("请先认证运营商")
            zmCreditState != 2        -> showDialog("请先认证芝麻信用")
            else                      -> {
                //申请操作
                showApplyDialog(presenter)
            }
        }
    }

    private fun showDialog(content: String) {
        AlertFragmentDialog.Builder(context)
            .setContent(content)
            .setRightBtnText("确定")
            .build()
    }

    private fun showApplyDialog(presenter: PerfectInfoPresenter) {
        AlertFragmentDialog.Builder(context)
            .setTitle("温馨提示")
            .setContent("请确保您提供的资料真实、准确，信息提交后不可修改。信息虚假将无法申请成功")
            .setLeftBtnText("再检查下")
            .setRightBtnText("确定提交")
            .setRightCallBack { presenter.applyInfo() }
            .build()
    }
}