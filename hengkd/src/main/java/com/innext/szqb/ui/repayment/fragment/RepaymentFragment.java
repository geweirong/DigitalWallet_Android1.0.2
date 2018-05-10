package com.innext.szqb.ui.repayment.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.config.Constant;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.dialog.MaterialDialog;
import com.innext.szqb.events.FragmentRefreshEvent;
import com.innext.szqb.events.LendFragmentLendEvent;
import com.innext.szqb.events.UIBaseEvent;
import com.innext.szqb.ui.main.FragmentFactory;
import com.innext.szqb.ui.main.MainActivity;
import com.innext.szqb.ui.main.WebViewActivity;
import com.innext.szqb.ui.my.adapter.BorrowRecordNewAdapter;
import com.innext.szqb.ui.my.bean.AssetRepaymentList;
import com.innext.szqb.ui.my.bean.BorrowingRecordBean;
import com.innext.szqb.ui.repayment.activity.RepaymentDetailActivity;
import com.innext.szqb.ui.repayment.adapter.RepaymentAdapter;
import com.innext.szqb.ui.repayment.bean.RepayMentInfoBean;
import com.innext.szqb.ui.repayment.contract.RepaymentContract;
import com.innext.szqb.ui.repayment.presenter.RePaymentPresenter;
import com.innext.szqb.util.Tool;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.widget.loading.LoadingLayout;
import com.innext.szqb.widget.recycler.BaseRecyclerAdapter;
import com.innext.szqb.widget.refresh.base.OnRefreshListener;
import com.innext.szqb.widget.refresh.base.SwipeToLoadLayout;
import com.innext.szqb.widget.statusBar.ImmersionBar;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：wangzuzhen on 2016/9/21 0021 12:15
 * 还款
 */
public class RepaymentFragment extends BaseActivity<RePaymentPresenter> implements RepaymentContract.View ,
        SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.refresh)
    SwipeToLoadLayout mRefresh;
    @BindView(R.id.borrowing_refresh_view)
    SwipeRefreshLayout mMultiRefresh;
    @BindView(R.id.layout_nodata)
    LinearLayout mLayoutNodata;
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.borrowing_swipe_target)
    RecyclerView mRecyClerView;
    @BindView(R.id.borrowing_tv_repayment_money)
    TextView mtvRepayMentMoney;
    @BindView(R.id.borrowing_record_ll_bottom)
    RelativeLayout mBorrowButton;
    private RepaymentAdapter mAdapter;
    private BorrowRecordNewAdapter mMultiAdapter;
    private String mAssetOrderId;//还款Url
    private BorrowingRecordBean mBean;
    private int mPayMoney;
    private int mSelectedPosition = 0;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_repayment_detail_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        initView();
        initLister();
    }

    private void initView() {
        mTitle.setTitle(true,"全部账单");
        ImmersionBar.with(this)
                .titleBar(mTitle.getmToolbar())
                .init();
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
        mAssetOrderId = getIntent().getStringExtra(Constant.POOL_ID);
        mPresenter.getListAssetRepayment(mAssetOrderId);
        mAdapter = new RepaymentAdapter();
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, RepaymentDetailActivity.class);
                intent.putExtra("poolId","");
                intent.putExtra("assetOrderId",String.valueOf(mBean.getAssetRepaymentList().get(position).getAssetOrderId()));
                mContext.startActivity(intent);
            }
        });
        mAdapter.setsubClickListener(new RepaymentAdapter.SubClickListener() {
            @Override
            public void OntopicClickListener(@Nullable TextView v, int position) {
                if (Tool.isFastDoubleClick()) return;
                switch (v.getId()){
                    case R.id.tv_repayment:
                        mPresenter.getPaymentStatus(String.valueOf(mBean.getAssetRepaymentList().get(position).getAssetOrderId()));
                        break;
                    case R.id.tv_delay:
                        mPresenter.gteRepaymentInfo(String.valueOf(mBean.getAssetRepaymentList().get(position).getId()));
                        break;
                }
            }
        });
        //多期还款账单
        mRecyClerView.setLayoutManager(new LinearLayoutManager(mContext));
        mMultiRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mMultiRefresh.setRefreshing(true);
        mMultiRefresh.setOnRefreshListener(this);
        mMultiAdapter = new BorrowRecordNewAdapter();
        mRecyClerView.setAdapter(mMultiAdapter);
        mMultiAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, RepaymentDetailActivity.class);
                intent.putExtra("poolId", "");
                intent.putExtra("assetOrderId",String.valueOf(mBean.getAssetRepaymentList().get(position).getAssetOrderId()));
                startActivity(intent);
            }
        });
        mMultiAdapter.setsubClickListener(new BorrowRecordNewAdapter.SubClickListener() {
            @Override
            public void OntopicClickListener(int pos, List<AssetRepaymentList> myLiveList) {
                AssetRepaymentList mList = myLiveList.get(pos);
                if (mList.getStatus() == 30 || mList.getStatus() == 34) {
                    ToastUtil.showToast("已还款");
                    return;
                }
                if (pos > 0) {
                    //判断不让跨期选择还款
                    boolean isSelect = mList.isSelected;
                    if (!isSelect) {
                        if (Math.abs(pos - mSelectedPosition) > 1) {
                            showErrorDialog();
                        } else {
                            mList.setSelected(true);
                            mPayMoney += mList.getRepaymentAmount();
                            mSelectedPosition = pos;
                        }
                    } else {
                        mList.setSelected(false);
                        mSelectedPosition = pos - 1;
                        mPayMoney -= mList.getRepaymentAmount();
                        for (int i = pos; i < myLiveList.size(); i++) {
                            if (myLiveList.get(i).getSelected()) {
                                myLiveList.get(i).setSelected(false);
                                mPayMoney -= myLiveList.get(i).getRepaymentAmount();
                            }
                        }
                    }
                    mtvRepayMentMoney.setText("还款总金额" + Tool.toDeciDouble2(mPayMoney / 100.00) + "元");
                    mMultiAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void initLister() {
        mRefresh.setLoadMoreEnabled(false);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSelectedPosition = 0;
                getDataResponse();
            }
        });
          getDataResponse();
    }
    private void getDataResponse(){
        mPresenter.getListAssetRepayment(mAssetOrderId);
    }
    /**
     * 没有还款记录数据显示处理
     * @param
     */
    private void showNoData() {
        mLayoutNodata.setVisibility(View.VISIBLE);
        mRefresh.setVisibility(View.GONE);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(FragmentRefreshEvent event) {
        if (UIBaseEvent.EVENT_BORROW_MONEY_SUCCESS == event.getType() ||
                UIBaseEvent.EVENT_TAB_REPAYMENT == event.getType() ||
                UIBaseEvent.EVENT_BANK_CARD_SUCCESS == event.getType()) {
            getDataResponse();
        }
    }

    private void showErrorDialog() {
        new AlertFragmentDialog.Builder(mActivity)
                .setContent("请按顺序选择")
                .setRightBtnText("确定")
                .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {

                    }
                }).setCancel(true).build();
    }
    @Override
    public void repaymentInfoSuccess(final RepayMentInfoBean result) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(mContext);
        mMaterialDialog.setMessage(result.getMsg()).setPositiveButton("确定延期", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", App.getConfig().baseUrl
                        + result.getUrl());// 	1--订单还款，2--支付延期费
                mContext.startActivity(intent);
                mMaterialDialog.dismiss();
            }
        }).setNegativeButton("再考虑下", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        }).show();
    }

    @Override
    public void repaymentInfoStatusSuccess(RepayMentInfoBean result) {
        //正常跳转
        MobclickAgent.onEvent(mContext, "payment_list");
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("url", App.getConfig().baseUrl + mBean.getUrl()
                + "?ids="+ mBean.getAssetRepaymentList().get(0).getAssetOrderId() + "&payType=" + 1); //	1--订单还款，2--支付延期费
        startActivity(intent);
    }

    @Override
    public void showListAssetRepaymentSuccess(BorrowingRecordBean bean) {
        this.mBean = bean;
        mLoadingLayout.setStatus(LoadingLayout.Success);
        mRefresh.setRefreshing(false);
        mMultiRefresh.setRefreshing(false);
        mMultiAdapter.clearData();
        mBorrowButton.setVisibility(View.GONE);
        if (bean.getAssetRepaymentList().size() > 0){
            if (bean.getAssetRepaymentList().size() == 1){
                mBorrowButton.setVisibility(View.GONE);
                mRecyClerView.setVisibility(View.GONE);
                mSwipeTarget.setVisibility(View.VISIBLE);
                setData(bean.getAssetRepaymentList());
            }else {
                //设置选中第一行
                if (!bean.getAssetRepaymentList().get(0).isSelected);
                bean.getAssetRepaymentList().get(0).setSelected(true);
                mBorrowButton.setVisibility(View.VISIBLE);
                mSwipeTarget.setVisibility(View.GONE);
                mRecyClerView.setVisibility(View.VISIBLE);
                mMultiAdapter.addData(bean.getAssetRepaymentList());
                mPayMoney = bean.getAssetRepaymentList().get(0).getRepaymentAmount();
                mtvRepayMentMoney.setText("还款总金额" + Tool.toDeciDouble2(mPayMoney / 100.00) + "元");
            }
        }else {
            showNoData();
        }
    }

    private void setData(List<AssetRepaymentList> result){
        mLayoutNodata.setVisibility(View.GONE);
        mRefresh.setVisibility(View.VISIBLE);
        //去掉右侧图片
        mTitle.setRightTitle(0, null);
        mAdapter.clearData();
        mAdapter.addData(result);
    }
    @Override
    public void showLoading(String content) {
        App.loadingContent(mActivity, content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
        mRefresh.setRefreshing(false);
        mMultiRefresh.setRefreshing(false);
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (type.equals("2") || type.equals("3")){ //2 延期还款请求失败
            final MaterialDialog mMaterialDialog = new MaterialDialog(mContext);
            mMaterialDialog.setMessage(msg).setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMaterialDialog.dismiss();
                }
            }).show();
        }else {
            ToastUtil.showToast(msg);
            loadingStatusView(msg);
        }
    }

    private void loadingStatusView(String message) {
        if ("网络不可用".equals(message)) {
            mLoadingLayout.setStatus(LoadingLayout.No_Network);
        } else {
            mLoadingLayout.setErrorText(message)
                    .setStatus(LoadingLayout.Error);
        }
        mLoadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getDataResponse();
            }
        });
    }
    @OnClick(R.id.borrowing_record_tv_repay)
    public void onClick(View view) {
        List<AssetRepaymentList> mData = mMultiAdapter.getData();
        String ids = "";
        for (AssetRepaymentList assetRepaymentList:mData){
            if (assetRepaymentList != null && assetRepaymentList.getSelected())
            {
                ids += "&ids="+assetRepaymentList.getAssetOrderId();
            }
        }
        Intent intent = new Intent();
        intent.setClass(mContext, WebViewActivity.class);//payType=1立即支付  2 延期还款
        intent.putExtra("url", App.getConfig().baseUrl + mBean.getUrl() + "?" + ids.substring(1,ids.length()) + "&payType=" + 1);
        intent.putExtra("title", "立即还款");
        startActivity(intent);
    }
    @Override
    public void onRefresh() {
      mPresenter.getListAssetRepayment(mAssetOrderId);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("全部账单"); //统计页面，"还款"为页面名称，可自定义
        MobclickAgent.onEvent(mContext, "repayment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("全部账单");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
