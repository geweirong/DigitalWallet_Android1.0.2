package com.innext.szqb.ui.my.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseFragment;
import com.innext.szqb.config.Constant;
import com.innext.szqb.config.WebViewConstant;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.events.FragmentRefreshEvent;
import com.innext.szqb.events.UIBaseEvent;
import com.innext.szqb.ui.authentication.activity.CreditCardActivity;
import com.innext.szqb.ui.authentication.activity.PerfectInformationActivity;
import com.innext.szqb.ui.main.MainActivity;
import com.innext.szqb.ui.main.WebViewActivity;
import com.innext.szqb.ui.my.activity.MoreActivity;
import com.innext.szqb.ui.my.activity.MyInvitationCodeActivity;
import com.innext.szqb.ui.my.activity.MyScoreActivity;
import com.innext.szqb.ui.my.activity.TransactionRecordActivity;
import com.innext.szqb.ui.my.bean.MoreContentBean;
import com.innext.szqb.ui.my.bean.MyOrderBean;
import com.innext.szqb.ui.my.bean.QueryVipStateBean;
import com.innext.szqb.ui.my.contract.MyContract;
import com.innext.szqb.ui.my.presenter.MyPresenter;
import com.innext.szqb.ui.repayment.bean.RepaymentItemBean;
import com.innext.szqb.util.Tool;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.DialogUtil;
import com.innext.szqb.util.common.SpUtil;
import com.innext.szqb.util.common.TagAndAlias;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.view.ViewUtil;
import com.innext.szqb.widget.loading.LoadingLayout;
import com.innext.szqb.widget.statusBar.ImmersionBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 我的页面fragment
 */
public class MoreFragment extends BaseFragment<MyPresenter> implements View.OnClickListener, MyContract.View {
    public static MoreFragment fragment;
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.tv_my_bank)
    TextView mTvMyBank;
    @BindView(R.id.tv_tel)
    TextView mTvTel;
    private MainActivity mActivity;
    private MoreContentBean mMoreContentBean,mMoreContentBean1;

    public static MoreFragment getInstance() {
        if (fragment == null)
            fragment = new MoreFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        ImmersionBar.with(this)
                .titleBar(mTitle.getmToolbar())
                .init();
        mActivity = (MainActivity) getActivity();
        EventBus.getDefault().register(this);
        if (App.getConfig().getLoginStatus()) {
            getResponse();
        }
        mTitle.setBackground(getContext(), R.color.more_title_color);
        mTvTel.setText(StringUtil.changeMobile(SpUtil.getString(Constant.CACHE_TAG_USERNAME)));
        //会员图标设置
//        mTitle.setVipTitle(StringUtil.changeMobile(SpUtil.getString(Constant.CACHE_TAG_USERNAME)), false);
//        mTitle.setTitle(false,StringUtil.changeMobile(SpUtil.getString(Constant.CACHE_TAG_USERNAME)));
        mTitle.setRightTitle(R.mipmap.shezhi, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "my_setting");
                Intent intent = new Intent(mActivity, MoreActivity.class);
                intent.putExtra("bean", mMoreContentBean);
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FragmentRefreshEvent event) {
        if (UIBaseEvent.EVENT_BANK_CARD_SUCCESS == event.getType() ||//银行卡绑定或修改成功
                UIBaseEvent.EVENT_LOGIN == event.getType() || //登录成功
                UIBaseEvent.EVENT_REPAY_SUCCESS == event.getType() ||  //还款成功
                UIBaseEvent.EVENT_REALNAME_AUTHENTICATION_SUCCESS == event.getType() ||    //实名认证成功
                UIBaseEvent.EVENT_LOAN_SUCCESS == event.getType() ||   //借款申请成功
                UIBaseEvent.EVENT_LOAN_FAILED == event.getType()) {   //借款申请失败
            if (UIBaseEvent.EVENT_LOGIN == event.getType()) {
                mTvTel.setText(StringUtil.changeMobile(SpUtil.getString(Constant.CACHE_TAG_USERNAME)));
            }
            if (App.getConfig().getLoginStatus()) {
                getResponse();
            }
        }
    }

    /**
     * 设置数据
     */
    private void setData(MoreContentBean mMoreContentBean) {
        mTvTel.setText(StringUtil.changeMobile(SpUtil.getString(Constant.CACHE_TAG_USERNAME)));
        if (mMoreContentBean.getCard_info() != null) {
            if (!StringUtil.isBlank(mMoreContentBean.getCard_info().getBank_name()) && !StringUtil.isBlank(mMoreContentBean.getCard_info().getCard_no_end())) {
                mTvMyBank.setText(mMoreContentBean.getCard_info().getBank_name() + "(" + mMoreContentBean.getCard_info().getCard_no_end() + ")");
            } else {
                mTvMyBank.setText("");
            }
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtil.showToast("分享成功");
            mPresenter.getShareSuccess(ViewUtil.getDeviceId(App.getContext()),
                    App.getConfig().getLoginStatus()? SpUtil.getString(Constant.CACHE_TAG_USERNAME):"");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.showToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的"); //统计页面，"MainScreen"为页面名称，可自定义
        MobclickAgent.onEvent(mContext, "my");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragment = null;
    }
    @OnClick({R.id.layout_perfect, R.id.layout_lend_record, R.id.layout_bank,
            R.id.layout_invitation, R.id.layout_message,R.id.layout_my_order,
            R.id.layout_help, R.id.layout_qq, R.id.layout_my_score})
    public void onClick(View view) {
        /**
         * 防止多次点击进入重复界面
         * 针对于startActivity启动
         * */
        if (Tool.isFastDoubleClick())
            return;

        switch (view.getId()) {
            case R.id.layout_perfect:
                //完善资料
                MobclickAgent.onEvent(mContext, "perfect_information");
                Intent intent = new Intent(mContext,PerfectInformationActivity.class);
                intent.putExtra("bean", mMoreContentBean);
                startActivity(intent);
                break;
            case R.id.layout_lend_record:
                MobclickAgent.onEvent(mContext, "lend_record");
                if (mMoreContentBean != null) {
                    Bundle transaction = new Bundle();
                    String userId = mMoreContentBean.getUserId();
                    transaction.putString(Constant.USER_ID, userId);
                    Log.e("用户信息==" + mMoreContentBean.getUserId(), "test");
                    startActivity(TransactionRecordActivity.class, transaction);
                } else getResponse();
                break;
            case R.id.layout_bank:
                clickMyBank();
                break;
            case R.id.layout_my_order:
                mPresenter.getOrderInfo();
                break;
            case R.id.layout_invitation:
                MobclickAgent.onEvent(mContext, "invitation");
                if (mMoreContentBean != null) {
                    new ShareAction(mActivity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                            .withTitle(mMoreContentBean.getShare_title())
                            .withText(mMoreContentBean.getShare_body())
                            .withTargetUrl(mMoreContentBean.getShare_url())
                            .withMedia(new UMImage(mActivity, mMoreContentBean.getShare_logo()))
                            .setCallback(umShareListener).open();
                }
                break;
            case R.id.layout_my_score:
                //我的积分
                //先获取会员状态
//                mPresenter.judgeMember();
                Intent intent1 = new Intent(mActivity, MyScoreActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout_message:
                MobclickAgent.onEvent(mContext, "message");
                Bundle message = new Bundle();
                message.putString("url", App.getConfig().ACTIVITY_CENTER);
                startActivity(WebViewActivity.class, message);
                break;
            case R.id.layout_help:
                MobclickAgent.onEvent(mContext, "my_help");
                Bundle help = new Bundle();
                help.putString("url", App.getConfig().HELP);
                startActivity(WebViewActivity.class, help);
                break;
            case R.id.layout_qq:
                if (mMoreContentBean != null && mMoreContentBean.getService() != null) {
                    //使用随机qq跳转
                    String qqUrl = Constant.QQ_LIST[0];
                    if (!Tool.isBlank(qqUrl)) {
                        String qq_url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqUrl;
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)));
                        } catch (Exception e) {
                            ToastUtil.showToast("请确认安装了QQ客户端");
                        }
                    }
                }
                break;
        }
    }

    private void clickMyBank() {
        MobclickAgent.onEvent(mContext, "proceeds_bank");
        //添加银行卡操作
        if (mMoreContentBean != null) {
            try {
                if (mMoreContentBean.getVerify_info().getReal_verify_status().equals("1")) {
                    Intent intent = new Intent(mActivity, WebViewActivity.class);
                    intent.putExtra("url", mMoreContentBean.getCard_url());
                    intent.putExtra(WebViewConstant.CACHE_TYPE, WebViewConstant.UNUSED_CACHE);
                    startActivity(intent);
                } else {
                    new AlertFragmentDialog.Builder(mActivity)
                            .setContent("亲,请先填写个人信息哦~")
                            .setRightBtnText("确定")
                            .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                                @Override
                                public void dialogRightBtnClick() {
                                    startActivity(PerfectInformationActivity.class);
                                }
                            }).setCancel(true).build();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getResponse();
        }
    }

    @Override
    public void userInfoSuccess(MoreContentBean result) {
        mLoadingLayout.setStatus(LoadingLayout.Success);
        mMoreContentBean = result;
        TagAndAlias.setTagAndAlias(mContext,String.valueOf(result.getUserId()));
        Log.e("用户UserId","用户UserId："+result.getUserId());
        if (mMoreContentBean != null) {
            setData(mMoreContentBean);
            try {
                Reservoir.put("MoreContentBean", result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void myOrderSuccess(MyOrderBean bean) {
        if (bean != null && !bean.getLink().isEmpty()) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url", bean.getLink());
            startActivity(intent);
        }
    }

    @Override
    public void showLoading(String content) {
        if (mMoreContentBean == null) {
            mLoadingLayout.setStatus(LoadingLayout.Loading);
        }
        App.loadingContent(mActivity, content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
//        ToastUtil.showToast(msg);
        if ("网络不可用".equals(msg)) {
            if (mMoreContentBean1 != null){
                ToastUtil.showToast("请检查网络是否正常或稍后重试");
            }else {
                mLoadingLayout.setStatus(LoadingLayout.No_Network);
            }
        } else {
            mLoadingLayout.setErrorText(msg)
                    .setStatus(LoadingLayout.Error);
        }
        mLoadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getResponse();
            }
        });
    }
    private void getResponse(){
        try {
            Boolean objectExists = Reservoir.contains("MoreContentBean");
            if (objectExists){
                Type resultType = new TypeToken<MoreContentBean>(){}.getType();
                mMoreContentBean1 = Reservoir.get("MoreContentBean", resultType);
                if (mMoreContentBean1 != null){
                    mLoadingLayout.setStatus(LoadingLayout.Success);
                    setData(mMoreContentBean1);
                    mMoreContentBean = mMoreContentBean1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPresenter.getInfo();
    }
}
