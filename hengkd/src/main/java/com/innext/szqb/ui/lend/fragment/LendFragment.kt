package com.innext.szqb.ui.lend.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.anupcowkur.reservoir.Reservoir
import com.innext.szqb.R
import com.innext.szqb.app.App
import com.innext.szqb.base.BaseFragment
import com.innext.szqb.config.Constant
import com.innext.szqb.dialog.ChargeDialog
import com.innext.szqb.events.FragmentRefreshEvent
import com.innext.szqb.events.LendFragmentLendEvent
import com.innext.szqb.events.UIBaseEvent
import com.innext.szqb.ext.onClick
import com.innext.szqb.ui.authentication.activity.PerfectInformationActivity
import com.innext.szqb.ui.lend.activity.LendConfirmLoanActivity
import com.innext.szqb.ui.lend.bean.*
import com.innext.szqb.ui.lend.contract.LendContract
import com.innext.szqb.ui.lend.presenter.LendPresenter
import com.innext.szqb.ui.main.WebViewActivity
import com.innext.szqb.util.Tool
import com.innext.szqb.util.common.ToastUtil
import com.innext.szqb.widget.BannerImageLoader
import com.innext.szqb.widget.loading.LoadingLayout
import com.innext.szqb.widget.statusBar.ImmersionBar
import com.umeng.analytics.MobclickAgent
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_new_lend_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.startActivity
import com.google.gson.reflect.TypeToken
import com.innext.szqb.ui.lend.adapter.BorrowPlatformAdapter
import com.innext.szqb.ui.main.MainActivity
import com.innext.szqb.ui.repayment.fragment.RepaymentFragment
import com.innext.szqb.util.ConvertUtil
import com.innext.szqb.util.common.DialogUtil
import kotlinx.android.synthetic.main.lend_item_state_36789.view.*
import kotlinx.android.synthetic.main.lend_item_state_4.view.*
import kotlinx.android.synthetic.main.lend_item_state_4and5.view.*
import kotlinx.android.synthetic.main.lend_item_state_8.view.*
import kotlinx.android.synthetic.main.lend_item_state_amount.view.*
import kotlinx.android.synthetic.main.lend_message.view.*

/**
 * 新借款页面
 */
class LendFragment : BaseFragment<LendPresenter>(), View.OnClickListener, LendContract.View {

    private var mChargeDialog: ChargeDialog ?= null
    companion object {
        val instance by lazy { LendFragment() }
        /** 默认显示金额 */
        private var DEFAULT_MONEY = 50000
        /**
         * 首页几个状态
         */
        private const val HOME_STATE_1 = 1  //1、未出额
        private const val HOME_STATE_2 = 2  //2、已出额
        private const val HOME_STATE_3 = 3  //3、审核中
        private const val HOME_STATE_4 = 4  //4、审核失败，60天再借
        private const val HOME_STATE_5 = 5  //5、审核失败，授信金额小于借款金额
        private const val HOME_STATE_6 = 6  //6、待放款
        private const val HOME_STATE_7 = 7  //7、打款中
        private const val HOME_STATE_8 = 8  //8、打款失败
        private const val HOME_STATE_9 = 9  //9、打款成功(已提现)
    }

    private var maxMoney: Int = 0
    private var loanMoney: Int = 0
    private var loanDay: Int = 0
    private var minMoney: Int = 0
    private var mHomeInfoBean: HomeInfoResponseBean? = null
    private var mHomeInfoBean1: HomeInfoResponseBean? = null
    private var mMoneyPeriodBean: AmountDaysListBean? = null
    private var mBannerListBean: List<IndexImagesBean>? = null
    private var mainActivity: MainActivity? = null
    private var mAdapter: BorrowPlatformAdapter? = null
    override fun getLayoutId(): Int = R.layout.fragment_new_lend_main

    override fun initPresenter() {
        mPresenter.init(this)
    }

    override fun loadData() {
        mainActivity = mActivity as MainActivity
        EventBus.getDefault().register(this)
    }

    /**
     * 初始化视图
     */
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        refresh.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.theme_color))
        //加载数据
        loading_layout.status = LoadingLayout.Loading
        ImmersionBar.with(this)
                .statusBarAlpha(0.0f)
                .statusBarDarkFont(false, 0.2f)//白色背景处理
                //                .fullScreen(true)
                .addTag("lendBanner")  //给上面参数打标记，以后可以通过标记恢复
                .init()
    }

    /**
     * 获取首页数据
     */
    private fun getIndexData() {
        //判断是否存在缓存
        val objectExists = Reservoir.contains("HomeInfoResponseBean")
        if (objectExists) {
            val turnsType = object : TypeToken<HomeInfoResponseBean>() {}.type
            mHomeInfoBean1 = Reservoir.get("HomeInfoResponseBean", turnsType)
            if (mHomeInfoBean1 != null) {
                loading_layout.status = LoadingLayout.Success    //这里是成功时，将有数据的界面展示出来。
                setData(mHomeInfoBean1!!)
            }
        }
        mPresenter.loadInfo()
    }

    private fun initListener() {
        refresh.setOnRefreshListener { getIndexData() }
    }

    override fun showLoading(content: String) {
        if (mHomeInfoBean != null) {
            App.loadingContent(mActivity,content)
        }
    }

    override fun stopLoading() {
        App.hideLoading()
        if (refresh.isRefreshing) {
            refresh.isRefreshing = false
        }
    }

    /**
     * @param msg  请求异常信息
     * @param type 若有多个请求，用于区分不同请求（不同请求失败或有不同的处理）
     */
    override fun showErrorMsg(msg: String, type: String) {
        if (TextUtils.isEmpty(type)) {
            return
        }
        if (type == LendPresenter.TYPE_INDEX) {//首页接口
            ToastUtil.showToast(msg)
            loadingStatusView(msg)
        }else if (type == LendPresenter.TYPE_STATE) {//服务费用修改提示
            ToastUtil.showToast(msg)
        }

    }

    private fun loadingStatusView(message: String) {
        if ("网络不可用" == message) {
            if (mHomeInfoBean1 != null) {
                ToastUtil.showToast("请检查网络是否正常或稍后重试")
            }else {
                loading_layout.status = LoadingLayout.No_Network
            }
        } else {
            loading_layout.setErrorText(message).status = LoadingLayout.Error
        }
        loading_layout.setOnReloadListener({ getIndexData() })
    }

    /**
     * 新的首页info信息
     * @param result
     */
    override fun infoSuccess(result: HomeInfoResponseBean) {
        loading_layout.status = LoadingLayout.Success    //这里是成功时，将有数据的界面展示出来。
        setData(result)
        //
        try {
            Reservoir.put("HomeInfoResponseBean", result);
        } catch (e: Exception) {
            //failure;
        }
    }
    private fun setData(result: HomeInfoResponseBean){
        //借款金额清零
        loanMoney = 0
        mHomeInfoBean = result
        if (result.maxAmount != 0) DEFAULT_MONEY = result.maxAmount / 100
        //热门活动集合banner
        mBannerListBean = result.index_images
        mMoneyPeriodBean = result.amount_days_list
        /**设置banner */
        setBanner()
        setStateView()
    }

    private fun setBanner() {
        if (mBannerListBean != null && mBannerListBean!!.isNotEmpty()) {
            banner_guide_content.visibility = View.VISIBLE
            //设置banner样式
            banner_guide_content.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(BannerImageLoader())            //设置图片加载器
                    .setImages(mBannerListBean!!.map { it.url })    //设置图片集合
                    .setBannerAnimation(Transformer.DepthPage)      //设置banner动画效果
                    .isAutoPlay(true)                    //设置自动轮播，默认为true
                    .setDelayTime(5000)                             //设置轮播时间
                    .setIndicatorGravity(BannerConfig.RIGHT)        //设置指示器（小圆点）位置（当banner模式中有指示器时）
                    .setOnBannerListener { position ->
                        val dto = mBannerListBean!![position]
                        // 3跳转页面 productType 分类ID pageLink页面链接
                        if (dto.reurl != null && dto.reurl!!.isNotEmpty()) {
                            startActivity<WebViewActivity>("url" to dto.reurl)
                        }
                    }
                    .start()    //banner设置方法全部调用完毕时最后调用
        } else {
            banner_guide_content.visibility = View.GONE
        }
    }

    /**
     * 通过状态设置视图
     */
    private fun setStateView() {
        /**
         * 1、未出额；2、已出额；3、审核中；4、审核失败，30天再借；
         * 5、审核失败，授信金额小于借款金额；6、待放款；7、打款中；8、打款失败；
         * 9、打款成功(已提现)
         */
        //加载不同的视图
        mStateLL.removeAllViews() //先移除所有视图
        var resInt = 0
        if (mHomeInfoBean?.statusMap == null) { //未登录
            resInt = R.layout.lend_item_state_amount
        }
        when (mHomeInfoBean?.statusMap?.risk_status) {  //登录了
            HOME_STATE_1,HOME_STATE_2 -> resInt = R.layout.lend_item_state_amount
            HOME_STATE_5 -> resInt = R.layout.lend_item_state_36789
            HOME_STATE_3,HOME_STATE_6, HOME_STATE_7,HOME_STATE_8 -> resInt = R.layout.lend_item_state_4and5
            HOME_STATE_4 -> resInt = R.layout.lend_item_state_4
            HOME_STATE_9 -> resInt = R.layout.lend_item_state_8
        }

        val stateView: View = View.inflate(context, resInt, null)
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        stateView.layoutParams = params
        initItemView(stateView, mHomeInfoBean?.statusMap?.risk_status)
        mStateLL.addView(stateView)
    }

    private fun initItemView(view: View, state: Int?) {
        //根据状态更改view的显示状态
        if (state == null) {
            //未登录 设置点击事件
            showNoOrHasLimitView(view)
            return
        }
        view.apply {
            when (state) {
                HOME_STATE_1 -> showNoOrHasLimitView(this)  //1、未出额
                HOME_STATE_2 -> showNoOrHasLimitView(this,true)   //2、已出额

                HOME_STATE_3 -> showApplyOtherStateView(this, state)   //3、审核中(未加急)
                HOME_STATE_4 -> showApplyFailView(this)  //4、审核失败，30天再借
                HOME_STATE_5 -> showApplyFailedView(this, state)  //5、审核失败，授信金额小于借款金额

                HOME_STATE_6 -> showApplyOtherStateView(this, state)   //6、待放款
                HOME_STATE_7 -> showApplyOtherStateView(this, state)   //7、打款中
                HOME_STATE_8 -> showApplyOtherStateView(this, state)   //8、打款失败
                HOME_STATE_9 -> showOtherStateView(this)   //9、打款成功,已逾期
            }
            view.lend_main_iv_message.onClick(this@LendFragment)//设置消息点击事件
        }
    }

    /**
     * 显示出额度的UI,默认为未出额度
     */
    private fun showNoOrHasLimitView(view: View, hasLimit: Boolean = false) {
        mHomeInfoBean?.let {
            if (hasLimit){
                maxMoney = it.amount_days_list.amountAvailable/100
                view.tv_money_sign.text = "可借金额（元）"
                view.tv_money.text = maxMoney.toString()
            }else {
                val amounts = it.amount_days_list.amounts?.map { it.toInt() }
                if (amounts != null && amounts.isNotEmpty()) {
                    maxMoney = amounts[amounts.size - 1] / 100
                    minMoney = amounts[0] / 100
                } else {
                    maxMoney = 0
                }
                view.tv_money_sign.text = "最高可借金额（元）"
                view.tv_money.text = DEFAULT_MONEY.toString() //默认数字1500
            }
        }
        //设置期数
        mMoneyPeriodBean?.let {
            loanDay = it.days.toInt()
        }
        view.tv_rent_btn.onClick(this)
        view.lend_main_iv_message.onClick(this@LendFragment)//设置消息点击事件
    }
    /**
     * 审核失败，授信金额小于借款金额
     */
    private fun showApplyFailedView(view: View, state: Int) = with(view) {
        view.tv_credit_sign.text = "授信金额（元）"
        view.tv_credit_money.text = (mMoneyPeriodBean!!.amountAvailable / 100).toString()
        view.tv_wait.text = mHomeInfoBean!!.statusMap?.loan_infos?.lists?.get(0)?.body
        view.tv_adjust_btn.onClick(this@LendFragment)
    }
    /*
     * 4、审核失败，30天再借；
     */
    private fun showApplyFailView(view: View){
        view.tv_state_error.text = mHomeInfoBean!!.statusMap?.loan_infos?.lists?.get(0)?.body
        banner_guide_content.visibility = View.GONE
        view.borrow_platform.apply {
            layoutManager = LinearLayoutManager(mContext)
            mAdapter = BorrowPlatformAdapter()
            adapter = mAdapter
        }
        mAdapter?.setsubClickListener(object : BorrowPlatformAdapter.SubClickListener {
            override fun OntopicClickListener(v: TextView?, position: Int) {
                if (Tool.isFastDoubleClick()) return
                startActivity<WebViewActivity>("url"
                        to mHomeInfoBean!!.recommends!!.list!!.get(0)!!.apply_link.toString())//一键申请
            }
        })
        mAdapter?.clearData()
        mAdapter?.addData(mHomeInfoBean?.recommends?.list)
        view.tv_my_bank.onClick(this@LendFragment)
    }
    /*
     * 审核中 打款中 打款失败 待放款
     */
    private fun showApplyOtherStateView(view: View, state: Int) = with(view) {
        //根据不同状态设置值
        when (state) {
            HOME_STATE_3, HOME_STATE_6 -> {//审核中
                view.tv_status.text = "审核中"
                if (mHomeInfoBean?.statusMap?.agree_status.equals("False")
                        || mHomeInfoBean?.statusMap?.agree_status.equals("false")) {  //false 弹窗 true不弹窗  agree_status  状态3,6的时候存在
                    if (mChargeDialog == null || mChargeDialog?.isVisible == false) {
                        mChargeDialog = ChargeDialog.Builder(mActivity).setRightBtnText("同意")
                                .setRightCallBack(ChargeDialog.RightClickCallBack {
                                    mPresenter.loadStateInfo()
                                }).build()
                    }
                }else{
                    mChargeDialog?.dismiss()
                }
            }else -> {//7打款中 8打款失败
               view.tv_status.text = "打款中"
            }
        }
        val spanableInfo = SpannableString("如有疑问，可联系客服"
                + mHomeInfoBean?.statusMap?.service_phone + "或关注公众号"+ mHomeInfoBean?.statusMap?.wechat_public)
        spanableInfo.setSpan(ConvertUtil.Clickable(clickListener,mContext),10,20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(ConvertUtil.OnLongClickable(onLongOnClick,mContext),26,spanableInfo.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.tv_body.setText(spanableInfo);
        view.tv_body.setMovementMethod(LinkMovementMethod.getInstance());
        view.tv_title.text = mHomeInfoBean?.statusMap?.loan_infos?.lists?.get(0)?.body
    }
    //点击电话拨打
    private val clickListener = object : View.OnClickListener{
        override fun onClick(v: View) {
            DialogUtil.showCallDialog(mActivity, mHomeInfoBean?.statusMap?.service_phone.toString().trim())
        }
    }
    //长按公众号粘贴
    private val onLongOnClick = object :View.OnLongClickListener{
        override fun onLongClick(v: View?): Boolean {
            DialogUtil.copy2Clipboard(mContext,mHomeInfoBean?.statusMap?.wechat_public.toString())
            return true
        }
    }

    /*
     * 打款成功   已逾期
     */
    private fun showOtherStateView(view: View) = with(view){
        if (mHomeInfoBean?.repay_info?.repayment_amount != 0) {
            view.tv_repay_money.text =  Tool.toDeciDouble2(mHomeInfoBean?.repay_info?.repayment_amount!! / 100.00).toString()
        }
        view.tv_repayment_time.text = "请于"+ mHomeInfoBean?.repay_info?.repayment_time +"前还款"
        //设置客服电话公众号字体颜色
        val spanableInfo = SpannableString("如有疑问，可联系客服"
                + mHomeInfoBean?.statusMap?.service_phone + "或关注公众号"+ mHomeInfoBean?.statusMap?.wechat_public)
        spanableInfo.setSpan(ConvertUtil.Clickable(clickListener,mContext),10,20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(ConvertUtil.OnLongClickable(onLongOnClick,mContext),26,spanableInfo.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.tv_tel_serve.setText(spanableInfo);
        view.tv_tel_serve.setMovementMethod(LinkMovementMethod.getInstance());
        view.tv_statu.visibility = if (mHomeInfoBean?.repay_info?.is_overdue!!) View.VISIBLE else View.INVISIBLE
        view.tv_repayment_btn.onClick(this@LendFragment)
    }
    /**
     * 获取同意收取报告费状态
     */
    override fun infoAgreeStateSuccess(result: AgreeStateBean?) {
        mPresenter.loadInfo()
    }

    override fun onClick(view: View) {
        if (Tool.isFastDoubleClick()) return
        when (view.id) {
            R.id.lend_main_iv_message -> startActivity<WebViewActivity>("url" to App.getConfig().ACTIVITY_CENTER)   //消息
            R.id.tv_rent_btn -> clickLendBtn() //1 2 布局中的按钮
            R.id.tv_adjust_btn ->  clickLendBtn()
            R.id.tv_repayment_btn -> { //立即还款
                val intent = Intent(mContext, RepaymentFragment::class.java)
                intent.putExtra(Constant.POOL_ID,mHomeInfoBean?.repay_info?.asset_order_id.toString())
                startActivity(intent)
            }

            R.id.tv_my_bank -> { //审核失败   跳转到商城
                startActivity<WebViewActivity>("url" to mHomeInfoBean?.recommends?.more_link.toString())
            }
        }
    }
    //去借款
    private fun clickLendBtn() {
        if (!App.getConfig().loginStatus) {
            App.toLogin(activity)
        } else {
            if (mHomeInfoBean?.statusMap?.commitstatus == 1) {  //0未提交，1完善资料已提交
                startActivity<LendConfirmLoanActivity>()
            } else {
                startActivity<PerfectInformationActivity>()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: FragmentRefreshEvent) {
        //当借款申请成功、还款或续期成功、登录成、退出功时刷新数据
        when (event.type) {
            UIBaseEvent.EVENT_LOGIN,
            UIBaseEvent.EVENT_LOGOUT -> getIndexData()
//            UIBaseEvent.EVENT_LOAN_SUCCESS,
//            UIBaseEvent.EVENT_REPAY_SUCCESS,
//            UIBaseEvent.EVENT_TAKE_MONEY_SUCCESS, UIBaseEvent.EVENT_TAB_LEND -> getIndexData()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun lendApply(event: LendFragmentLendEvent) {
        clickLendBtn()
    }

    override fun onResume() {
        super.onResume()
        getIndexData()
        MobclickAgent.onPageStart("首页借款") //统计页面，"MainScreen"为页面名称，可自定义
        MobclickAgent.onEvent(context, "lend")
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd("首页借款")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }
}
