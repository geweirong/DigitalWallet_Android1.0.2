package com.innext.szqb.ui.repayment.activity

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.innext.szqb.R
import com.innext.szqb.app.App
import com.innext.szqb.base.BaseActivity
import com.innext.szqb.ext.setVisible
import com.innext.szqb.ui.lend.activity.LendConfirmLoanActivity
import com.innext.szqb.ui.repayment.adapter.RepaymentPlanAdapter
import com.innext.szqb.ui.repayment.bean.RepayList
import com.innext.szqb.ui.repayment.bean.RepaymentPlanBean
import com.innext.szqb.ui.repayment.contract.RepaymentPlanContract
import com.innext.szqb.ui.repayment.presenter.RepaymentPlayPresenter
import com.innext.szqb.util.Tool
import com.innext.szqb.util.common.ToastUtil
import com.innext.szqb.widget.loading.LoadingLayout
import kotlinx.android.synthetic.main.repay_ment_plan.*
/**
 * @author      : gwr
 * @date        : 2018/4/23 16:06
 * @description : 还款计划
 */
class RepaymentPlanActivity : BaseActivity<RepaymentPlayPresenter>() , RepaymentPlanContract.View{

    private val mAdapter: RepaymentPlanAdapter by lazy { RepaymentPlanAdapter(R.layout.item_repay_play, mDatas) }

    private var mDatas = arrayListOf<RepayList>()
    private var bean: RepaymentPlanBean? = null
    private var mLoanType: String? = null
    private var borrowAmount: String? = null
    private var productNo: String? = null
    private var assetOrderId: String? = null
    override fun getLayoutId(): Int = R.layout.repay_ment_plan

    override fun initPresenter() = mPresenter.init(this)

    override fun loadData() {
        mTitle.setTitle(true, { finish() }, "还款计划")
         mLoanType = intent.getStringExtra("type")
        if (mLoanType!!.equals(LendConfirmLoanActivity.LoanType)){
             borrowAmount = intent.getStringExtra("borrowAmount")
             productNo = intent.getStringExtra("productNo")
        }else{
             assetOrderId = intent.getStringExtra("assetOrderId")
        }

        with(swipe_refresh_layout) {
            setColorSchemeColors(ContextCompat.getColor(mContext, R.color.theme_color))
            //默认关闭
            isEnabled = false
        }
        //检查是否满一屏，如果不满足关闭loadMore
        mAdapter.disableLoadMoreIfNotFullPage(recycler_view)
        recycler_view.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = mAdapter
        }
        initListener()
        getScoreRecord(mLoanType!!)
    }

    private fun initListener() {
        swipe_refresh_layout.setOnRefreshListener { getScoreRecord(mLoanType!!) }
    }
    override fun getRepaymentPlanListSuccess(bean: RepaymentPlanBean) {
        handleResponse(bean)
    }
    private fun handleResponse(bean: RepaymentPlanBean) = with(bean) {
        loading_layout.status = LoadingLayout.Success
        this@RepaymentPlanActivity.bean = this
        mDatas = bean.repayList!!
        tv_holder.visibility = if (!mDatas.isEmpty() || mDatas.size > 0) View.GONE else View.VISIBLE
        if (!mDatas.isEmpty() || mDatas.size > 0){
            tv_holder.setVisible(false)
            rl_header.setVisible(true)
        }else{
            tv_holder.setVisible(true)
            rl_header.setVisible(false)
        }
        tv_totalRepayAmount.text = "还款总额￥"+  Tool.toDeciDouble2(bean!!.totalRepayAmount!!.toDouble()/100)
        tv_totalRepayInterestAmount.text = "利息总额￥" + Tool.toDeciDouble2(bean?.totalRepayInterestAmount!!.toDouble()/100)
        mAdapter.setNewData(mDatas)
    }

    override fun showLoading(content: String?) {
        if (bean == null) loading_layout.status = LoadingLayout.Loading
    }
    override fun getRepaymentPlanSuccess(bean: RepaymentPlanBean) {
        handleResponse(bean)
    }
    override fun stopLoading() {
        App.hideLoading()
        swipe_refresh_layout.isEnabled = true
        if (swipe_refresh_layout.isRefreshing) swipe_refresh_layout.isRefreshing = false
    }

    override fun showErrorMsg(msg: String?, type: String?) {
        ToastUtil.showToast(msg)
        if ("网络不可用" == msg) loading_layout.status = LoadingLayout.No_Network
        else loading_layout.setErrorText(msg).status = LoadingLayout.Error
        loading_layout.setOnReloadListener{ getScoreRecord(mLoanType!!) }
    }

    private fun getScoreRecord(loadType: String) {
        if (mLoanType!!.equals(LendConfirmLoanActivity.LoanType)){
            mPresenter.getRepaymentPlanList((Integer.parseInt(borrowAmount!!) * 100).toString(),productNo!!)
        }else{
            mPresenter.getRepaymentPlan(assetOrderId!!)
        }
    }

}
