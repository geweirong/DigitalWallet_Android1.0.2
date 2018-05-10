package com.innext.szqb.ui.my.activity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.events.RefreshUIEvent;
import com.innext.szqb.events.UIBaseEvent;
import com.innext.szqb.ui.my.contract.SetPayPwdContract;
import com.innext.szqb.ui.my.presenter.SetPayPwdPresenter;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.widget.keyboard.KeyboardNumberUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.innext.szqb.R.id.tv_tip;

/**
 * 设置交易密码页面activity
 */

public class SetTradePwdActivity extends BaseActivity<SetPayPwdPresenter> implements SetPayPwdContract.View {
    @BindView(tv_tip)
    TextView mTvTip;
    @BindView(R.id.ed_pwd_key1)
    TextView mEdPwdKey1;
    @BindView(R.id.ed_pwd_key2)
    TextView mEdPwdKey2;
    @BindView(R.id.ed_pwd_key3)
    TextView mEdPwdKey3;
    @BindView(R.id.ed_pwd_key4)
    TextView mEdPwdKey4;
    @BindView(R.id.ed_pwd_key5)
    TextView mEdPwdKey5;
    @BindView(R.id.ed_pwd_key6)
    TextView mEdPwdKey6;
    @BindView(R.id.llCustomerKb)
    View mLlCustomerKb;

    private KeyboardNumberUtil mKeyboardNumberUtil;
    private List<TextView> inputKeys = new ArrayList<TextView>();
    private List<String> oldPwds = new ArrayList<>();
    private List<String> pwds = new ArrayList<String>();
    private List<String> confirmPwds = new ArrayList<String>();
    private int index = 0;
    //确认密码
    private boolean isConfirm = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_settrade_pwd;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle("设置交易密码");

        inputKeys.add(mEdPwdKey1);
        inputKeys.add(mEdPwdKey2);
        inputKeys.add(mEdPwdKey3);
        inputKeys.add(mEdPwdKey4);
        inputKeys.add(mEdPwdKey5);
        inputKeys.add(mEdPwdKey6);
        mKeyboardNumberUtil = new KeyboardNumberUtil(this, mLlCustomerKb, KeyboardNumberUtil.CUSTOMER_KEYBOARD_TYPE.NUMBER, mEdPwdKey1);

        keyBoardNumberListener();
    }

    private void keyBoardNumberListener() {
        mKeyboardNumberUtil.setmKeyboardClickListenr(new KeyboardNumberUtil.KeyboardNumberClickListener() {

            @Override
            public void postHideEt() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd() {
                // TODO Auto-generated method stub

            }

            @Override
            public void handleHideEt(View relatedEt, int hideDistance) {
                // TODO Auto-generated method stub

            }

            @Override
            public void clickDelete() {
                // TODO Auto-generated method stub
                if (index == 0)
                    return;
                inputKeys.get(index - 1).setVisibility(View.INVISIBLE);
                if (!isConfirm) {
                    pwds.remove(index - 1);
                } else {
                    confirmPwds.remove(index - 1);

                }
                index--;
            }

            @Override
            public void click(String clickStr) {
                if (!isConfirm) {
                    if (index >= inputKeys.size()) {

                        return;
                    }

                    pwds.add(clickStr);

                    if (index == inputKeys.size() - 1) {
                        inputKeys.get(index).setVisibility(View.VISIBLE);
                        postDelayed(500, new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                changeConfirmPwdStatus();
                            }
                        });

                        return;
                    }
                } else {
                    if (index >= inputKeys.size()) {
                        return;
                    }
                    confirmPwds.add(clickStr);
                    if (index == inputKeys.size() - 1) {
                        inputKeys.get(index).setVisibility(View.VISIBLE);
                        postDelayed(500, new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                savePayPwd();
                            }
                        });
                    }
                }
                inputKeys.get(index).setVisibility(View.VISIBLE);
                index++;
            }

            @Override
            public void clear() {
                //防止长按编辑框无显示问题
                for (int i = 0; i < index; i++) {
                    inputKeys.get(i).setVisibility(View.INVISIBLE);
                }
                if (type == 0) {
                    oldPwds.clear();
                }else if (type == 1) {
                    pwds.clear();
                } else {
                    confirmPwds.clear();
                }

                index = 0;
            }
        });
    }
    private void postDelayed(final int i, Action1<Long> action1) {
        Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                try {
                    Thread.sleep(i);
                    subscriber.onNext(0l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mKeyboardNumberUtil.showKeyboard();
            }
        }
        return super.onTouchEvent(event);
    }


    /**
     * 切换到确认密码状态
     */
    private void changeConfirmPwdStatus() {
        isConfirm = true;
        for (int i = 0; i < inputKeys.size(); i++)
            inputKeys.get(i).setVisibility(View.INVISIBLE);
        mTitle.setTitle("确认交易密码");
        mTvTip.setText("请确认六位交易密码");
        index = 0;
    }


    private void savePayPwd() {
        String payPwd = getInputPwd(pwds);
        String confirmPwd = getInputPwd(confirmPwds);
        if (payPwd.equals(confirmPwd)) {
            mPresenter.setPayPwd(confirmPwd);
        } else {
            ToastUtil.showToast("两次密码不一致,请重新输入");
            clearInputInfo();
        }
    }


    /***********
     * 清除输入
     */
    private void clearInputInfo() {
        isConfirm = false;
        for (int i = 0; i < inputKeys.size(); i++)
            inputKeys.get(i).setVisibility(View.INVISIBLE);
        mTitle.setTitle("设置交易密码");
        mTvTip.setText("请设置六位交易密码");
        index = 0;
        pwds.clear();
        confirmPwds.clear();
    }

    /*********
     * 获取交易密码
     *
     * @return
     */
    private String getInputPwd(List<String> passwords) {
        String payPwd = "";

        for (int i = 0; i < passwords.size(); i++)
            payPwd = payPwd + passwords.get(i);

        return payPwd.trim();
    }

//    /***********
//     * eventBus 监听
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(RefreshUIEvent event) {
//        if (UIBaseEvent.EVENT_SET_PAYPWD == event.getType()) {
//            finish();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setPayPwdSuccess() {
        new AlertFragmentDialog.Builder(this).setContent("交易密码设置成功")
                .setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
            @Override
            public void dialogRightBtnClick() {
                EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_SET_PAYPWD));
                finish();
            }
        }).build();
    }

    @Override
    public void showLoading(String content) {
        App.loadingContent(this, content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        ToastUtil.showToast(msg);
        clearInputInfo();
    }
}
