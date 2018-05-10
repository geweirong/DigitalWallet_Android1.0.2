package com.innext.szqb.ui.authentication.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.innext.szqb.BuildConfig
import com.innext.szqb.R
import com.innext.szqb.app.App
import com.innext.szqb.base.BaseActivity
import com.innext.szqb.dialog.MaterialDialog
import com.innext.szqb.events.AuthenticationRefreshEvent
import com.innext.szqb.events.FragmentRefreshEvent
import com.innext.szqb.events.UIBaseEvent
import com.innext.szqb.ext.setVisible
import com.innext.szqb.ui.authentication.bean.PerfectInfoBean
import com.innext.szqb.ui.authentication.bean.Verification
import com.innext.szqb.ui.authentication.contract.PerfectInfoContract
import com.innext.szqb.ui.authentication.presenter.PerfectInfoPresenter
import com.innext.szqb.ui.authentication.states.PerfectStateProcessor
import com.innext.szqb.ui.lend.activity.LendConfirmLoanActivity
import com.innext.szqb.ui.main.WebViewActivity
import com.innext.szqb.ui.my.activity.FeedBackActivity
import com.innext.szqb.ui.my.bean.MoreContentBean
import com.innext.szqb.ui.my.bean.MoxieApproveBean
import com.innext.szqb.ui.my.bean.MoxieApproveStateBean
import com.innext.szqb.ui.my.service.ApproveService
import com.innext.szqb.util.Tool
import com.innext.szqb.util.common.ToastUtil
import com.innext.szqb.util.view.RichTextUtil
import com.innext.szqb.widget.loading.LoadingLayout
import com.moxie.client.manager.MoxieCallBack
import com.moxie.client.manager.MoxieCallBackData
import com.moxie.client.manager.MoxieContext
import com.moxie.client.manager.MoxieSDK
import com.moxie.client.model.MxParam
import kotlinx.android.synthetic.main.activity_write_info.*
import kotlinx.android.synthetic.main.authentication_score.*
import kotlinx.android.synthetic.main.authentication_score.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.yesButton

/**
 * @author      : yan
 * @date        : 2017/12/27 10:17
 * @description : 完善资料界面
 */
class PerfectInformationActivity : BaseActivity<PerfectInfoPresenter>(), PerfectInfoContract.View {
    var bean: MoreContentBean? = null
    private var perfectInfoBean: PerfectInfoBean? = null
    private lateinit var stateProcessor: PerfectStateProcessor

    override fun getLayoutId(): Int = R.layout.activity_write_info

    override fun initPresenter() = mPresenter.init(this)

    override fun loadData() {
        mTitle.setTitle(true, {
           onBackPressed()
        }, "完善资料")
        //设置意见反馈文案颜色
        Tool.TextSpannable(mContext,tv_credit_feedback,resources.getString(R.string.feedback),"#334fc0",
                5,resources.getString(R.string.feedback).length)
        swipe_refresh_layout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.theme_color))
        bean = intent.getParcelableExtra("bean")
        stateProcessor = PerfectStateProcessor(this)
        initListener()
        EventBus.getDefault().register(this)
        mPresenter.getInfo()
    }

    private fun initListener() {
        swipe_refresh_layout.setOnRefreshListener { mPresenter.getInfo() }
        //个人信息
        iiv_personal_info.setOnClickListener { stateProcessor.clickPersonalInfo() }
        //联系人
        iiv_contacts.setOnClickListener { stateProcessor.clickContacts() }
        //手机运营商
        iiv_mobile_operator.setOnClickListener { stateProcessor.clickMobileOperator() }
        //芝麻信用
        iiv_zm_credit.setOnClickListener { stateProcessor.clickZmCredit() }
        //信用卡
        iiv_xyk.setOnClickListener { stateProcessor.clickXyk(mPresenter) }
        //checkbox
        cb_agree.setOnCheckedChangeListener { buttonView, isChecked ->
            if (stateProcessor.canApply) btn_apply.isEnabled = isChecked   //只有支付宝认证过了，才能申请
        }
        //辅助checkbox
        rl_agreement.setOnClickListener { cb_agree.isChecked = !cb_agree.isChecked }
        //协议
        tv_credit_agreement.setOnClickListener {
            startActivity<WebViewActivity>("url" to App.getConfig().CREDIT_AUTHORIZATION_AGREEMENT)
        }
        //申请
        btn_apply.setOnClickListener {
            stateProcessor.clickApply(mPresenter)
        }
        //新增反馈入口
        tv_credit_feedback.setOnClickListener { startActivity<FeedBackActivity>("holiday" to bean!!.service.holiday,
                "peacetime" to bean!!.service.peacetime,"qq_group" to bean!!.service.qq_group,
                "service_phone" to bean!!.service.service_phone,"services_qq" to bean!!.service.services_qq) }
    }

    override fun showLoading(content: String?) {
        if (perfectInfoBean == null) loading_layout.status = LoadingLayout.Loading
        else App.loadingContent(this, content)
    }

    override fun stopLoading() {
        App.hideLoading()
        if (swipe_refresh_layout.isRefreshing) swipe_refresh_layout.isRefreshing = false
    }

    @SuppressLint("SetTextI18n")
    override fun getInfoSuccess(bean: PerfectInfoBean) = with(bean) {
        // 状态转移到PerfectStateProcessor中处理
        perfectInfoBean = this
        cb_agree.isChecked = true
        //设置一些状态
        stateProcessor.bean = this
        tv_progress.text = "您的资料进度已完成$mustBeCount%"
        progress_bar.progress = mustBeCount

        if (stateProcessor.personalInfoState == 1)  iiv_personal_info.setRightText("已完成")  //个人信息
           else iiv_personal_info.setRightText("去认证",R.color.c_ffbe4f,false)

        if (stateProcessor.contactsState == 1) iiv_contacts.setRightText("已完成")  //联系人
           else iiv_contacts.setRightText("去认证",R.color.c_ffbe4f,false)

        if (stateProcessor.mobileOperationState == 2) iiv_mobile_operator.setRightText("已认证")  //运营商
           else iiv_mobile_operator.setRightText("去认证",R.color.c_ffbe4f,false)

        if (stateProcessor.zmCreditState == 2) iiv_zm_credit.setRightText("已认证")  //芝麻信用
           else iiv_zm_credit.setRightText("去认证",R.color.c_ffbe4f,false)

        if (stateProcessor.xykState == 1)  iiv_xyk.setRightText("已认证") //信用卡
           else iiv_xyk.setRightText("去认证",R.color.c_ffbe4f,false)

        //是否隐藏个人信息、联系人的enter箭头
        iiv_personal_info.hideIvEnter(stateProcessor.applyState == 1)
        iiv_contacts.hideIvEnter(stateProcessor.applyState == 1)

        if (stateProcessor.canApply) {
            btn_apply.isEnabled = true
        }
        if (commitStatus == 1) {
            //已提交
            btn_apply.isEnabled = false
            btn_apply.text = "申请已提交"
            rl_agreement.visibility = View.INVISIBLE
        } else {
            btn_apply.text = "提交申请"
            rl_agreement.isClickable = true
            cb_agree.isClickable = true
            rl_agreement.visibility = View.VISIBLE
        }
        loading_layout.status = LoadingLayout.Success
    }

    override fun applyInfoSuccess() {
        ToastUtil.showToast("申请成功")
        //通知首页刷新,跳转到借款界面
        EventBus.getDefault().post(FragmentRefreshEvent(UIBaseEvent.EVENT_LOAN_SUCCESS))
        startActivity<LendConfirmLoanActivity>()
        finish()
    }

    override fun showErrorMsg(msg: String?, type: String?) {
        ToastUtil.showToast(msg)
        if ("网络不可用" == msg) {
            loading_layout.status = LoadingLayout.No_Network
        } else {
            loading_layout.setErrorText(msg).status = LoadingLayout.Error
        }
        loading_layout.setOnReloadListener { mPresenter.getInfo() }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshPerfectInfo(event: AuthenticationRefreshEvent) {
        mPresenter.getInfo()
    }
    override fun onDestroy() {
        super.onDestroy()
        //用来清理数据或解除引用
//        MoxieSDK.getInstance().clear()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {  //只需要判断芝麻信用是否认证完
        if (stateProcessor.zmCreditState != 2)
            showDialog()
            else super.onBackPressed()
    }
    private fun showDialog() {
        val mDialogMD = MaterialDialog(mContext)
        val stateView: View = View.inflate(mContext, R.layout.authentication_score, null)
        stateView.apply {
            if (stateProcessor.zmCreditState == 2){
                stateView.tv_current_credit.setVisible(true)
                stateView.tv_current_person.setVisible(false)
                stateView.tv_score_title.text = "完成芝麻信用认证即送168积分"
                stateView.iv_credit.setImageResource(R.mipmap.ic_credit)
                stateView.iv_operator.setImageResource(R.mipmap.ic_operator)
                stateView.iv_contacts.setImageResource(R.mipmap.ic_contacts)
                stateView.tv_credit.setTextColor(resources.getColor(R.color.c_333333))
                stateView.tv_operator.setTextColor(resources.getColor(R.color.c_333333))
                stateView.tv_contacts.setTextColor(resources.getColor(R.color.c_333333))
                stateView.tv_integral_credit.setTextColor(resources.getColor(R.color.c_ffb333))
                stateView.tv_integral_operator.setTextColor(resources.getColor(R.color.c_ffb333))
                stateView.tv_integral_contacts.setTextColor(resources.getColor(R.color.c_ffb333))
            }else if (stateProcessor.mobileOperationState == 2){
                stateView.tv_current_person.setVisible(false)
                stateView.tv_current_operator.setVisible(true)
                stateView.tv_score_title.text = "完成芝麻信用认证即送168积分"
                stateView.iv_operator.setImageResource(R.mipmap.ic_operator)
                stateView.iv_contacts.setImageResource(R.mipmap.ic_contacts)
                stateView.tv_contacts.setTextColor(resources.getColor(R.color.c_333333))
                stateView.tv_operator.setTextColor(resources.getColor(R.color.c_333333))
                stateView.tv_integral_operator.setTextColor(resources.getColor(R.color.c_ffb333))
                stateView.tv_integral_contacts.setTextColor(resources.getColor(R.color.c_ffb333))
            }else if (stateProcessor.contactsState == 1){
                stateView.tv_current_person.setVisible(false)
                stateView.tv_current_contacts.setVisible(true)
                stateView.tv_score_title.text = "完成运营商认证即送99积分"
                stateView.tv_contacts.setTextColor(resources.getColor(R.color.c_333333))
                stateView.iv_contacts.setImageResource(R.mipmap.ic_contacts)
                stateView.tv_integral_contacts.setTextColor(resources.getColor(R.color.c_ffb333))
            }else{
                stateView.tv_score_title.text = "完成紧急联系人认证即送88积分"
                stateView.tv_current_person.setVisible(true)
            }
        }
        mDialogMD.setView(stateView)
        mDialogMD.setPositiveButton("继续申请",View.OnClickListener {
            mDialogMD.dismiss()
        }).setNegativeButton("放弃奖励",View.OnClickListener {
              finish();
        }).show()
    }
}


