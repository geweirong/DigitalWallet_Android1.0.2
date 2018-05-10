package com.innext.szqb.ui.my.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innext.szqb.R;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.config.Constant;
import com.innext.szqb.events.UIBaseEvent;
import com.innext.szqb.ui.my.adapter.TransactionRecordAdapter;
import com.innext.szqb.ui.my.bean.TransactionRecordListBean;
import com.innext.szqb.ui.my.contract.TransactionRecordContract;
import com.innext.szqb.ui.my.presenter.TransactionRecordPresenter;
import com.innext.szqb.widget.recycler.DividerItemDecoration;
import com.innext.szqb.widget.refresh.base.OnLoadMoreListener;
import com.innext.szqb.widget.refresh.base.OnRefreshListener;
import com.innext.szqb.widget.refresh.base.SwipeToLoadLayout;

import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by hengxinyongli on 2017/2/16 0016.
 * 描述：借款记录
 */

public class TransactionRecordActivity extends BaseActivity<TransactionRecordPresenter> implements TransactionRecordContract.View, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.swipe_target)
    RecyclerView mRefreshList;
    @BindView(R.id.refresh)
    SwipeToLoadLayout mRefreshLoadLayout;

    private TransactionRecordPresenter recordPresenter;
    private TransactionRecordAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private boolean isPull = true;
    private List<TransactionRecordListBean> itemBeanList;
    private String userId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_transaction_record;
    }

    @Override
    public void initPresenter() {
        userId = getIntent().getExtras().getString(Constant.USER_ID);
        recordPresenter = new TransactionRecordPresenter();
        recordPresenter.init(this);
        getData();

    }

    private void getData() {
        recordPresenter.getLoanRecordRequest(userId, pageNo + "", pageSize + "");
    }

    @Override
    public void loadData() {
        initView();
    }

    private void initView() {
        mTitle.setTitle("借款记录");
        mRefreshLoadLayout.setOnRefreshListener(this);
        mRefreshLoadLayout.setOnLoadMoreListener(this);
        mRefreshList.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new TransactionRecordAdapter();
        mRefreshList.setAdapter(mAdapter);
        repaymentSuccessForUpdatePage();
    }

    @Override
    public void recordSuccess(List<TransactionRecordListBean> itemBean, String linkUrl) {
        if (itemBean != null) {
            if (isPull) {
                mAdapter.clearData();
                onComplete(mRefreshLoadLayout);
                if (itemBean == null || itemBean.size() == 0) {
                    if (mAdapter.getFootersCount() == 0) {
                        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_norecord_style, null);
                        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        mAdapter.addFooterView(view);
                    }
                } else {
                    if (mAdapter.getFootersCount() > 0)
                        mAdapter.clearFooterView();
                }
            }
            itemBeanList = itemBean;
            if (itemBeanList != null) {
                mAdapter.clearData();
            }
            mAdapter.addData(itemBeanList);
        } else {
            mAdapter.clearData();
            if (mAdapter.getFootersCount() == 0) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_norecord_style, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mAdapter.addFooterView(view);
            }
        }
    }

    @Override
    public void loadRecordSuccess(List<TransactionRecordListBean> itemBean, String transactionBeanLinkUrl) {

    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {
        onComplete(mRefreshLoadLayout);
    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    public void onLoadMore() {
        if (itemBeanList != null) {
            pageNo++;
            isPull = false;
            getData();
            onComplete(mRefreshLoadLayout);
        } else {
            mRefreshLoadLayout.setLoadingMore(false);
            onComplete(mRefreshLoadLayout);
        }
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isPull = true;
        getData();
    }

    /**
     * 还款成功刷新页面
     */
    private void repaymentSuccessForUpdatePage() {
        mRxManager.on(UIBaseEvent.EVENT_REPAY_SUCCESS + "", new Action1<String>() {
            @Override
            public void call(String s) {
                pageNo = 1;
                getData();
            }
        });
    }
}
