package com.innext.szqb.ui.my.activity

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.innext.szqb.R
import com.innext.szqb.app.App
import com.innext.szqb.base.BaseActivity
import com.innext.szqb.config.Constant
import com.innext.szqb.ext.getYMStr
import com.innext.szqb.ui.my.adapter.ScoreRecordRvAdapter
import com.innext.szqb.ui.my.bean.Record
import com.innext.szqb.ui.my.bean.RecordData
import com.innext.szqb.ui.my.bean.ScoreRecordBean
import com.innext.szqb.ui.my.contract.ScoreRecordContract
import com.innext.szqb.ui.my.presenter.ScoreRecordPresenter
import com.innext.szqb.util.common.SpUtil
import com.innext.szqb.util.common.ToastUtil
import com.innext.szqb.widget.loading.LoadingLayout
import kotlinx.android.synthetic.main.activity_score_record.*

/**
 * @author      : yan
 * @date        : 2017/12/26 19:14
 * @description : 积分记录界面
 */
class ScoreRecordActivity : BaseActivity<ScoreRecordPresenter>(), ScoreRecordContract.View {

    companion object {
        /** 默认每一页的大小 */
        private const val PAGE_SIZE = 20
        /** 正常加载数据、刷新数据 */
        private const val LOAD_DATA_NORMAL = 11
        /** 加载更多数据 */
        private const val LOAD_DATA_MORE = 12
    }

    private val mAdapter: ScoreRecordRvAdapter by lazy { ScoreRecordRvAdapter(R.layout.item_score_record, mDatas) }

    private val mRecord = arrayListOf<Record>()
    private val mDatas = arrayListOf<RecordData>()
    private var bean: ScoreRecordBean? = null
    /** 当前页 */
    private var pageIndex = 1

    private var mRequestType: Int = LOAD_DATA_NORMAL

    override fun getLayoutId(): Int = R.layout.activity_score_record

    override fun initPresenter() = mPresenter.init(this)

    override fun loadData() {
        mTitle.setTitle(true, { finish() }, "积分记录")

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

        getScoreRecord(LOAD_DATA_NORMAL)
    }

    private fun initListener() {
        swipe_refresh_layout.setOnRefreshListener { getScoreRecord(LOAD_DATA_NORMAL) }
        mAdapter.setOnLoadMoreListener({ getScoreRecord(LOAD_DATA_MORE) }, recycler_view)
    }

    override fun getScoreRecordSuccess(bean: ScoreRecordBean, loadType: Int) {
        handleResponse(bean, loadType)
    }

    private fun handleResponse(bean: ScoreRecordBean, loadType: Int) = with(bean) {
        loading_layout.status = LoadingLayout.Success
        this@ScoreRecordActivity.bean = this
        when (loadType) {
            LOAD_DATA_NORMAL -> {
                tv_placeholder.visibility = if (pageIndex == 1 && (recordData == null)) View.VISIBLE else View.GONE
                mRecord.clear()
                mDatas.clear()
            }
            LOAD_DATA_MORE -> {
                if (recordData.isEmpty()) {
                    mAdapter.loadMoreEnd()
                    return@with
                }
            }
        }
        //先将每月记录存储
        record.forEach {
            if (it in mRecord) return@forEach
            mRecord += it
        }
        mDatas += recordData
        //待合并完数据之后再进行添加头的操作
        mRecord.forEach {
            for (data in mDatas) {
                //如果已经包含，则跳过
                if (data.dateStr == it.mothStr) break
                val ymStr = data.finished_at.getYMStr()
                if (ymStr == it.mothStr) {
//                    data.payIntegral = it.payIntegral
//                    data.incomeIntegral = it.incomeIntegral
                    data.dateStr = ymStr
                    break
                }
            }
        }
        mAdapter.setNewData(mDatas)
    }

    override fun showLoading(content: String?) {
        when (mRequestType) {
            LOAD_DATA_NORMAL -> {
                mAdapter.setEnableLoadMore(false)
            }
            LOAD_DATA_MORE -> {
                mAdapter.setEnableLoadMore(true)
            }
        }
        if (bean == null) loading_layout.status = LoadingLayout.Loading
    }

    override fun stopLoading() {
        when (mRequestType) {
            LOAD_DATA_NORMAL -> {
                mAdapter.setEnableLoadMore(true)
            }
            LOAD_DATA_MORE -> {
            }
        }
        App.hideLoading()
        swipe_refresh_layout.isEnabled = true
        if (swipe_refresh_layout.isRefreshing) swipe_refresh_layout.isRefreshing = false
    }

    override fun showErrorMsg(msg: String?, type: String?) {
        ToastUtil.showToast(msg)
        if ("网络不可用" == msg) loading_layout.status = LoadingLayout.No_Network
        else loading_layout.setErrorText(msg).status = LoadingLayout.Error
        loading_layout.setOnReloadListener{ getScoreRecord(LOAD_DATA_NORMAL) }
    }

    private fun getScoreRecord(loadType: Int) {
        mRequestType = loadType
        if (loadType == LOAD_DATA_NORMAL) pageIndex = 1 //重置当前页数
        else pageIndex ++
        val userId = SpUtil.getString(Constant.CACHE_TAG_UID).toInt()
        mPresenter.getScoreRecord(userId, pageIndex, PAGE_SIZE, loadType)
    }

}