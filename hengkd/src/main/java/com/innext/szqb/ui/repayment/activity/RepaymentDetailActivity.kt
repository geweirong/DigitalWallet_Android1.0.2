package com.innext.szqb.ui.repayment.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import com.innext.szqb.R
import com.innext.szqb.app.App
import com.innext.szqb.base.BaseActivity
import com.innext.szqb.config.Constant
import com.innext.szqb.ext.onClick
import com.innext.szqb.ext.setVisible
import com.innext.szqb.ui.main.WebViewActivity
import com.innext.szqb.ui.repayment.bean.RepaymentDetailBean
import com.innext.szqb.ui.repayment.contract.RepaymentDetailContract
import com.innext.szqb.ui.repayment.fragment.RepaymentFragment
import com.innext.szqb.ui.repayment.presenter.RepaymentDetailPresenter
import com.innext.szqb.util.Tool
import com.innext.szqb.util.common.ToastUtil
import com.innext.szqb.widget.loading.LoadingLayout
import com.innext.szqb.widget.statusBar.ImmersionBar
import kotlinx.android.synthetic.main.activity_repayment_detail.*

/**
 * @author      : gwr
 * @date        : 2018/4/10 16:06
 * @description : 借款详情
 */
class RepaymentDetailActivity : BaseActivity<RepaymentDetailPresenter>(), RepaymentDetailContract.View, View.OnClickListener{
    private var bean: RepaymentDetailBean? = null
    private val PAY_STATE_1 = 0  //审核中
    private val PAY_STATE_2 = 1  //审核不通过
    private val PAY_STATE_3 = 2  //放款中
    private val PAY_STATE_4 = 3  //还款中
    private val PAY_STATE_5 = 4  //已逾期
    private val PAY_STATE_6 = 5  //已还完
    private var poolId: String = ""
    private var assetOrderId: String = ""
    val LoanType = "RepaymentDetailActivity"
    override fun getLayoutId(): Int = R.layout.activity_repayment_detail

    override fun initPresenter() {
        mPresenter.init(this)
    }

    override fun loadData() {
        with(mTitle) {
            //先将toolbar设置成透明色
            getmToolbar().setBackgroundColor(resources.getColor(R.color.c_00ffffff))
            setTitle(true, { finish() }, "借款详情")
        }
        ImmersionBar.with(this)
                .titleBar(mTitle.getmToolbar())
                .init()
        poolId = intent.getStringExtra("poolId")
        assetOrderId = intent.getStringExtra("assetOrderId")
        Log.e("单号","单号："+assetOrderId);
        if ((poolId != null && poolId!!.isNotEmpty()) || (assetOrderId != null && assetOrderId!!.isNotEmpty())) {
            mPresenter.getDetail(poolId,assetOrderId)
        }
        tv_contract.onClick(this)
        tv_repay.onClick(this)
        tv_rent_btn.onClick(this)
    }

    @SuppressLint("SetTextI18n")
    override fun showSuccess(bean: RepaymentDetailBean?) {
        loading_layout.status = LoadingLayout.Success
        this.bean = bean
        tv_statu.setVisible(true)
        /*
         * 只有还款中，已还完，已逾期显示合同和逾期费
         */
        bean?.let {
            when(it.status){
                PAY_STATE_1 -> {
                    tv_statu.text = "审核中"
                    tv_rent_btn.visibility = View.INVISIBLE
                    ly_contract.setVisible(false)
                }
                PAY_STATE_2 -> {
                    tv_statu.text = "审核不通过"
                    tv_rent_btn.visibility = View.INVISIBLE
                    ly_contract.setVisible(false)
                }
                PAY_STATE_3 -> {
                    tv_statu.text = "放款中"
                    tv_rent_btn.visibility = View.INVISIBLE
                    ly_contract.setVisible(false)
                }
                PAY_STATE_4 -> {
                    tv_statu.text = "还款中"
                    tv_rent_btn.setVisible(true)
                    ly_contract.setVisible(true)
                }
                PAY_STATE_5 ->{
                    tv_statu.text = "已逾期"
                    tv_rent_btn.setVisible(true)
                    ly_contract.setVisible(true)
                }
                PAY_STATE_6 -> {
                    tv_statu.text = "已还完"
                    tv_rent_btn.visibility = View.INVISIBLE
                    ly_contract.setVisible(true)
                }
            }
            tv_lend_num.text = "${it.moneyAmount}"//借款金额
            tv_lend_date.text = "${it.createdAt}"  //借款时间
            tv_loan_term.text = "${it.loanTerm}"  //借款期限
            tv_repaymethod.text = "${it.repayMethod}" //还款方式
        }
    }

    override fun showLoading(content: String?) {
        if (bean == null) loading_layout.status = LoadingLayout.Loading
        else App.loadingContent(this, content)
    }

    override fun stopLoading() = App.hideLoading()

    override fun showErrorMsg(msg: String?, type: String?) {
        ToastUtil.showToast(msg)
        if ("网络不可用" == msg) {
            loading_layout.status = LoadingLayout.No_Network
        } else {
            loading_layout.setErrorText(msg).status = LoadingLayout.Error
        }
        loading_layout.setOnReloadListener{
            if ((poolId != null && poolId!!.isNotEmpty())
                    || (assetOrderId != null && assetOrderId!!.isNotEmpty())) {
                mPresenter.getDetail(poolId,assetOrderId)
            }
        }
    }
    override fun onClick(view: View) {
        if (Tool.isFastDoubleClick()) return
        when(view.id){
            R.id.tv_contract ->{
                //需要后台返回合同地址
                bean?.let {
                    val intent = Intent(mContext, WebViewActivity::class.java)
                    intent.putExtra("title", "合同")
                    intent.putExtra("url", App.getConfig().baseUrl1 + "${it.contractImg}")
                    startActivity(intent)
                }
            }
            R.id.tv_repay ->{//查看还款计划
                val intent = Intent(mContext, RepaymentPlanActivity::class.java)
                intent.putExtra("type",LoanType)
                intent.putExtra("assetOrderId", bean?.assetOrderId.toString())//分期订单ID
                startActivity(intent)
            }
            R.id.tv_rent_btn -> { //立即还款
                val intent = Intent(mContext, RepaymentFragment::class.java)
                intent.putExtra(Constant.POOL_ID,bean?.assetOrderId.toString())
                startActivity(intent)
            }
        }
    }
}
