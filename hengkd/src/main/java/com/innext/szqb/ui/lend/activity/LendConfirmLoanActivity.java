package com.innext.szqb.ui.lend.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.base.PermissionsListener;
import com.innext.szqb.config.Constant;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.dialog.PickerViewFragmentDialog;
import com.innext.szqb.events.AuthenticationRefreshEvent;
import com.innext.szqb.events.LoanEvent;
import com.innext.szqb.events.PayLeanResultEvent;
import com.innext.szqb.events.RefreshUIEvent;
import com.innext.szqb.events.UIBaseEvent;
import com.innext.szqb.ui.lend.bean.ConfirmLoanBean;
import com.innext.szqb.ui.lend.contract.LendConfirmLoanContract;
import com.innext.szqb.ui.lend.presenter.LendConfirmLoanPresenter;
import com.innext.szqb.ui.main.WebViewActivity;
import com.innext.szqb.ui.my.activity.ForgetPayPwdActivity;
import com.innext.szqb.ui.my.activity.SetTradePwdActivity;
import com.innext.szqb.ui.repayment.activity.RepaymentPlanActivity;
import com.innext.szqb.util.ServiceUtil;
import com.innext.szqb.util.Tool;
import com.innext.szqb.util.check.StringUtil;
import com.innext.szqb.util.common.SpUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.util.view.TextViewUtil;
import com.innext.szqb.widget.loading.LoadingLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 借款确认页面activity
 */
public class LendConfirmLoanActivity extends BaseActivity<LendConfirmLoanPresenter>
        implements LendConfirmLoanContract.View, SwipeRefreshLayout.OnRefreshListener {
    public final static String LoanType = "LendConfirmLoanActivity";
    ConfirmLoanBean bean;
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.tv_productType)
    TextView mTvProductType;
    @BindView(R.id.tv_period)
    TextView mTvPeriod;
    @BindView(R.id.tv_bank_card)
    TextView mTvBankCard;
    @BindView(R.id.tv_lend_use)
    TextView mTvLendUse;
    @BindView(R.id.tv_money)
    EditText mTvLoanMoney;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.ly_money)
    LinearLayout mLyMoney;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    @BindView(R.id.btn_next)
    TextView mBtnNext;
    @BindView(R.id.ck_agreement)
    CheckBox mCkAgreement;
    @BindView(R.id.tv_loan_agreement)
    TextView mTvLoanAgreement;
    @BindView(R.id.tv_service_agreement)
    TextView mTvServiceAgreement;
    @BindView(R.id.tv_deduct_agreement)
    TextView mTvDeductAgreement;
    ArrayList<String> mPeriodList;
    /** 借款用途位置 */
    private int loadUsePosition;
    private int loadPeriod;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lend_confirm_loan;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mPresenter.borrowData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        mRefresh.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.theme_color));
        mRefresh.setOnRefreshListener(this);
        mTitle.setTitle("借款");
        mBtnNext.setEnabled(false);
        mPeriodList = new ArrayList<>();
        mCkAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!StringUtil.isBlank(mTvLoanMoney.getText().toString())
                     && !Tool.isBlank(bean.getcardNo())) {
                mBtnNext.setEnabled(isChecked);
            }
            }
        });
        mTvLoanMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              if (!StringUtil.isBlank(mTvLoanMoney.getText().toString()) && !Tool.isBlank(bean.getcardNo())){
                  mBtnNext.setEnabled(true);
              }else {
                  mBtnNext.setEnabled(false);
              }
            }
        });
    }

    private void setUIData(final ConfirmLoanBean bean) {
        if (bean != null) {
            mTvLoanMoney.setHint(bean.getProductList().get(0).getMinApplyAmount()/100 + "-" + bean.getProductList().get(0).getMaxApplyAmount()/100);
            if (bean.getProductList().get(0).getProductType().equals("acpi")){
                mTvProductType.setText("等额本息");
            }else {
                mTvProductType.setText("等本等息");
            }
            if (bean.getProductList().get(0).getPeriodType().equals("day")){
                mTvPeriod.setText(bean.getProductList().get(0).getPeriod() + "天");
                mTvPeriod.setEnabled(false);
            }else {
                mTvPeriod.setText(bean.getProductList().get(0).getPeriod() + "个月");
                mTvPeriod.setEnabled(true);
                for (int i = 0;i < bean.getProductList().size();i++){
                    mPeriodList.add(bean.getProductList().get(i).getPeriod() + "个月");
                }
            }
            //初始化设置百货消费
            mTvLendUse.setText(bean.getLoanUse().get(bean.getLoanUse().size() - 1));
            //当前银行卡不为空的
            if (!Tool.isBlank(bean.getcardNo())) {
                //显示银行卡名称和后四位银行卡
                mTvBankCard.setText(bean.getbankName()+ "（" + "尾号"+ bean.getcardNoLastFour() + "）");
            } else {
                String bankContent = "未绑卡";
                Tool.TextSpannable(mContext, mTvBankCard, bankContent, "#fd9f4e", 0, bankContent.length());
            }
            if (!Tool.isBlank(bean.getprotocolMsg()))
                mTvTips.setText(bean.getprotocolMsg());
            TextViewUtil.setPartialColor(mTvLoanAgreement, 7, mTvLoanAgreement.getText().length(), ContextCompat.getColor(mContext, R.color.theme_color));
        }
        mTvLendUse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!StringUtil.isBlank(mTvLoanMoney.getText().toString())
                        && !Tool.isBlank(bean.getcardNo()) && mCkAgreement.isChecked()){
                    mBtnNext.setEnabled(true);
                }else {
                    mBtnNext.setEnabled(false);
                }
            }
        });

    }

    private void toLoan() {
        EventBus.getDefault().post(new LoanEvent(LendConfirmLoanActivity.this));//给EventController发通知
        enterBankInputPwdActivity();
    }

    private void enterBankInputPwdActivity() {
        bean.setSelectLoanUsePos(loadUsePosition);
        bean.setLoadPeriod(loadPeriod);
        Intent intent = new Intent(LendConfirmLoanActivity.this, BankInputPwdActivity.class);
        intent.putExtra(BankInputPwdActivity.TAG_USAGEINDEX, String.valueOf(loadUsePosition));
        intent.putExtra(BankInputPwdActivity.TAG_MONEY, mTvLoanMoney.getText().toString());
        intent.putExtra(BankInputPwdActivity.TAG_PRODUCTNO, bean.getProductList().get(loadPeriod).getProductNo());
        startActivity(intent);
        //添加底部弹出动画
        overridePendingTransition(R.anim.translate_bottom_to_center, R.anim.anim_no);
    }

    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RefreshUIEvent event) {
        //设置交易密码成功
        if (UIBaseEvent.EVENT_SET_PAYPWD == event.getType()) {
            bean.setRealPayPwdStatus(true);
        } else if (UIBaseEvent.EVENT_LOAN_SUCCESS == event.getType()) {
            /*Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);*/
            SpUtil.putString(Constant.CACHE_TAG_COUNT_DOWN, "");
            finish();
        } else if (UIBaseEvent.EVENT_LOAN_FAILED == event.getType()) {
            if (((PayLeanResultEvent) event).getErrMessage().equals("支付密码错误")) {
                new AlertFragmentDialog.Builder(mActivity)
                        .setContent(((PayLeanResultEvent) event).getErrMessage())
                        .setLeftBtnText("忘记密码")
                        .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                            @Override
                            public void dialogLeftBtnClick() {
                                Intent intent = new Intent(LendConfirmLoanActivity.this, ForgetPayPwdActivity.class);
                                startActivity(intent);
                            }

                        }).setRightBtnText("重新输入")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick() {
                                enterBankInputPwdActivity();
                            }
                        }).setCancel(true).build();
            } else {
                new AlertFragmentDialog.Builder(mActivity)
                        .setContent(((PayLeanResultEvent) event).getErrMessage())

                        .setRightBtnText("确定")
                        .build();
            }
        }
    }

    /**
     * 绑定银行卡成功刷新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshBankCardInfo(AuthenticationRefreshEvent event) {
        mPresenter.borrowData();
    }

    @OnClick({R.id.btn_next, R.id.rl_agreement, R.id.tv_service_agreement, R.id.tv_deduct_agreement, R.id.tv_bank_card, R.id.tv_lend_use,
            R.id.tv_period,R.id.tv_plan})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_lend_use:
                //借款用途
                selectDialog(loadUsePosition, bean.getLoanUse(),2);
                break;
            case R.id.tv_period:
                selectDialog(loadPeriod,mPeriodList,1);
                break;
            case R.id.tv_plan://查看还款计划
                if (TextUtils.isEmpty(mTvLoanMoney.getText().toString())){
                    ToastUtil.showToast("请输入借款金额");
                }else if (Integer.parseInt(mTvLoanMoney.getText().toString()) < bean.getProductList().get(0).getMinApplyAmount()/100
                        || Integer.parseInt(mTvLoanMoney.getText().toString()) > bean.getProductList().get(0).getMaxApplyAmount()/100){
                        ToastUtil.showToast("请输入" + bean.getProductList().get(0).getMinApplyAmount() / 100
                                + "-" + bean.getProductList().get(0).getMaxApplyAmount() / 100
                                + "的借款金额");
                }else {
                    if (Integer.parseInt(mTvLoanMoney.getText().toString()) % 100 != 0){
                        ToastUtil.showToast("请输入100的整数倍金额");
                    }else {
                        Intent repayPlan = new Intent(LendConfirmLoanActivity.this, RepaymentPlanActivity.class);
                        repayPlan.putExtra("type", LoanType);//区别还款计划类型
                        repayPlan.putExtra("borrowAmount", String.valueOf(Integer.parseInt(mTvLoanMoney.getText().toString())));//借款金额
                        repayPlan.putExtra("productNo", bean.getProductList().get(loadPeriod).getProductNo());//金融产品编号
                        startActivity(repayPlan);
                    }
                }
                break;
            case R.id.btn_next:
               if (Integer.parseInt(mTvLoanMoney.getText().toString()) < bean.getProductList().get(0).getMinApplyAmount()/100
                        || Integer.parseInt(mTvLoanMoney.getText().toString()) > bean.getProductList().get(0).getMaxApplyAmount()/100){
                        ToastUtil.showToast("请输入" + bean.getProductList().get(0).getMinApplyAmount() / 100
                                + "-" + bean.getProductList().get(0).getMaxApplyAmount() / 100
                                + "的借款金额");
                }else {
                        if (Integer.parseInt(mTvLoanMoney.getText().toString()) % 100 != 0) {
                            ToastUtil.showToast("请输入100的整数倍金额");
                        }else {
                            if (!bean.getRealPayPwdStatus()) {//去设置交易密码
                                Intent intent = new Intent(LendConfirmLoanActivity.this, SetTradePwdActivity.class);
                                startActivity(intent);
                            } else {
                                //开启短信权限
                                requestPermissions(new String[]{Manifest.permission.READ_SMS}, new PermissionsListener() {
                                    @Override
                                    public void onGranted() {
                                        toLoan();//去借款
                                    }

                                    @Override
                                    public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
                                        if (isNeverAsk) {
                                            toAppSettings("缺少短信权限", false);
                                        }
                                    }
                                });
                            }
                        }
                }
                break;
            case R.id.rl_agreement:
                //TODO 借款协议
                Intent getprotocolUrl = new Intent(LendConfirmLoanActivity.this, WebViewActivity.class);
                getprotocolUrl.putExtra("url", App.getConfig().baseUrl + bean.getprotocolUrl());//getProtocol_url
                startActivity(getprotocolUrl);
                break;
            case R.id.tv_service_agreement:
                //TODO 平台服务协议
                Intent platformservice_url = new Intent(LendConfirmLoanActivity.this, WebViewActivity.class);
                platformservice_url.putExtra("url", App.getConfig().baseUrl + bean.getplatformServiceUrl());//getPlatformservice_url
                startActivity(platformservice_url);
                break;
            case R.id.tv_deduct_agreement:
                //TODO 授权扣款协议 --已隐藏
                Intent withholding_url = new Intent(LendConfirmLoanActivity.this, WebViewActivity.class);
                withholding_url.putExtra("url", App.getConfig().baseUrl + bean.getwithholdingUrl());//getWithholdingUrl
                startActivity(withholding_url);
                break;
            case R.id.tv_bank_card:
                Intent bankCard_url = new Intent(LendConfirmLoanActivity.this, WebViewActivity.class);
                bankCard_url.putExtra("url", App.getConfig().baseUrl + bean.getfirstUserBankUrl());//getFirstUserBank_url
                startActivity(bankCard_url);
                break;
        }
    }

    //选择dailog  1表示借款期限选择   2表示借款用途选择
    private void selectDialog(final int oldPosition, final ArrayList<String> typeList,final int type) {
        new PickerViewFragmentDialog(oldPosition, typeList, new PickerViewFragmentDialog.OnValueSelectListener() {
            @Override
            public void select(String value, int position) {
                if (type == 1){
                    loadPeriod = position;
                    mTvPeriod.setText(value);
                }else {
                    loadUsePosition = position;
                    mTvLendUse.setText(value);
                }
            }
        }).show(getSupportFragmentManager(), PickerViewFragmentDialog.TAG);
    }

    @Override
    public void showLoading(String content) {
        if (bean == null) {
            mLoadingLayout.setStatus(LoadingLayout.Loading);
        }
    }

    @Override
    public void stopLoading() {
        if (mRefresh.isRefreshing()) {
            mRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        ToastUtil.showToast(msg);
        if ("网络不可用".equals(msg)) {
            mLoadingLayout.setStatus(LoadingLayout.No_Network);
        } else {
            mLoadingLayout.setErrorText(msg)
                    .setStatus(LoadingLayout.Error);
        }
        mLoadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mPresenter.borrowData();
            }
        });
    }

    @Override
    public void toLoanSuccess(ConfirmLoanBean result) {
        mLoadingLayout.setStatus(LoadingLayout.Success);
        setUIData(result);
    }

    @Override
    public void borrowDataSuccess(ConfirmLoanBean confirmLoanBean) {
        //初始化数据返回成功结果
        mLoadingLayout.setStatus(LoadingLayout.Success);
        this.bean = confirmLoanBean;
        setUIData(confirmLoanBean);
    }

    @Override
    public void onRefresh() {
        mPresenter.borrowData();
    }
}
