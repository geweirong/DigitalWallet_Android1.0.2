package com.innext.szqb.ui.my.activity

import android.view.View
import com.innext.szqb.R
import com.innext.szqb.app.App
import com.innext.szqb.base.BaseActivity
import com.innext.szqb.config.Constant
import com.innext.szqb.ext.setVisible
import com.innext.szqb.http.HttpApi
import com.innext.szqb.ui.main.WebViewActivity
import com.innext.szqb.ui.my.bean.AllScoreBean
import com.innext.szqb.ui.my.contract.MyScoreContract
import com.innext.szqb.ui.my.presenter.MyScorePresenter
import com.innext.szqb.util.common.SpUtil
import com.innext.szqb.util.common.ToastUtil
import com.innext.szqb.widget.loading.LoadingLayout
import com.innext.szqb.widget.statusBar.ImmersionBar
import kotlinx.android.synthetic.main.activity_my_score.*
import org.jetbrains.anko.startActivity

/**
 * @author      : yan
 * @date        : 2017/12/26 16:16
 * @description : 我的积分页面
 */
class MyScoreActivity : BaseActivity<MyScorePresenter>(), MyScoreContract.View, View.OnClickListener {
    private var url: String=""
    override fun getLayoutId(): Int = R.layout.activity_my_score

    override fun initPresenter() = mPresenter.init(this)

    override fun loadData() {
        initBar()
        initListener()
    }

    private fun initBar() {
        with(mTitle) {
            //先将toolbar设置成透明色
            getmToolbar().setBackgroundColor(resources.getColor(R.color.c_00ffffff))
            setTitle(true, { finish() }, "我的积分")
        }
        ImmersionBar.with(this)
                .titleBar(mTitle.getmToolbar())
                .init()
        mPresenter.getAllScore(SpUtil.getInt(Constant.CACHE_TAG_UID))
    }

    private fun initListener() {
        tv_score_record.setOnClickListener(this)
        tv_score_sweepstakes.setOnClickListener(this)
        tv_credit_report.setOnClickListener(this)
        tv_credit_report.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun getAllScoreSuccess(bean: AllScoreBean?) {
        loading_layout.status = LoadingLayout.Success
        bean?.let {
            tv_total_score.text = "${it.avaiableIntegral}"
            if (it.agreeStatus == 0){
                tv_credit_report.setVisible(false)
            }else{
                tv_credit_report.setVisible(true)
            }
            url = it.reportUrl
        }
    }

    override fun showLoading(content: String?) {
        loading_layout.status = LoadingLayout.Loading
    }

    override fun stopLoading() = App.hideLoading()

    override fun showErrorMsg(msg: String?, type: String?) {
        ToastUtil.showToast(msg)
        if ("网络不可用" == msg) loading_layout.status = LoadingLayout.No_Network
        else loading_layout.setErrorText(msg).status = LoadingLayout.Error
        loading_layout.setOnReloadListener{ mPresenter.getAllScore(SpUtil.getString(Constant.CACHE_TAG_UID).toInt()) }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_score_record -> startActivity<ScoreRecordActivity>()
            R.id.tv_score_sweepstakes -> {
                //积分抽奖
                startActivity<WebViewActivity>("url" to HttpApi.URL_SCORE_LOTTERY)
            }
            R.id.tv_credit_report -> {
                if (url != null && !url.isEmpty()) {
                    startActivity<WebViewActivity>("url" to url)
                }
            }
        }
    }
}