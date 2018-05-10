package com.innext.szqb.ui.my.activity;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.dialog.AlertFragmentDialog;
import com.innext.szqb.events.RefreshUIEvent;
import com.innext.szqb.events.UIBaseEvent;
import com.innext.szqb.ui.my.contract.ChangePayPwdContract;
import com.innext.szqb.ui.my.presenter.ChangPayPwdPresenter;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.widget.keyboard.KeyboardNumberUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.innext.szqb.R.id.ed_pwd_key1;
import static com.innext.szqb.R.id.ed_pwd_key2;
import static com.innext.szqb.R.id.ed_pwd_key3;
import static com.innext.szqb.R.id.ed_pwd_key4;
import static com.innext.szqb.R.id.ed_pwd_key5;
import static com.innext.szqb.R.id.ed_pwd_key6;
import static com.innext.szqb.R.id.llCustomerKb;
import static com.innext.szqb.R.id.tv_tip;


/**
 * Created by hengxinyongli on 2017/2/17 0017.
 */

public class UpdataTradePwdActivity extends BaseActivity<ChangPayPwdPresenter> implements ChangePayPwdContract.View {
    @BindView(tv_tip)
    TextView mTvTip;
    @BindView(ed_pwd_key1)
    TextView mEdPwdKey1;
    @BindView(ed_pwd_key2)
    TextView mEdPwdKey2;
    @BindView(ed_pwd_key3)
    TextView mEdPwdKey3;
    @BindView(ed_pwd_key4)
    TextView mEdPwdKey4;
    @BindView(ed_pwd_key5)
    TextView mEdPwdKey5;
    @BindView(ed_pwd_key6)
    TextView mEdPwdKey6;
    @BindView(llCustomerKb)
    LinearLayout mLlCustomerKb;

    private KeyboardNumberUtil mKeyboardNumberUtil;
    private List<TextView> inputKeys = new ArrayList<TextView>();
    private List<String> oldPwds = new ArrayList<>();
    private List<String> pwds = new ArrayList<>();
    private List<String> confirmPwds = new ArrayList<String>();
    private int index = 0;
    private int type = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_updata_trade_pwd;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mTitle.setTitle("修改交易密码");
        mTitle.setRightTitle("忘记", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, ForgetPayPwdActivity.class));
            }
        });
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
                if (index <= 0)
                    return;
                inputKeys.get(index - 1).setVisibility(View.INVISIBLE);
                if (type == 0) {
                    oldPwds.remove(index - 1);
                } else if (type == 1) {
                    pwds.remove(index - 1);
                } else {
                    confirmPwds.remove(index - 1);
                }
                index--;
            }

            @Override
            public void click(String clickStr) {
                if (type == 0) {
                    if (index >= inputKeys.size()) {
                        return;
                    }
                    oldPwds.add(clickStr);
                    if (index == inputKeys.size() - 1) {
                        inputKeys.get(index).setVisibility(View.VISIBLE);
                        postDelayed(500, new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                changeNewPwdStatus();
                            }
                        });
                        return;
                    }
                }else if (type == 1) {
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
                }else if (type == 2) {
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
     * 切换到输入密码状态
     */
    private void changeNewPwdStatus() {
        type = 1;
        for (int i = 0; i < inputKeys.size(); i++)
            inputKeys.get(i).setVisibility(View.INVISIBLE);
        mTitle.setTitle("新交易密码");
        mTvTip.setText("请输入六位交易密码");
        index = 0;
    }

    /**
     * 切换到确认密码密码状态
     */
    private void changeConfirmPwdStatus() {
        type = 2;
        for (int i = 0; i < inputKeys.size(); i++)
            inputKeys.get(i).setVisibility(View.INVISIBLE);
        mTitle.setTitle("确认交易密码");
        mTvTip.setText("请确认六位交易密码");
        index = 0;
    }


    private void savePayPwd() {
        String oldPwd = getInputPwd(oldPwds);
        String payPwd = getInputPwd(pwds);
        String confirmPwd = getInputPwd(confirmPwds);
        if (payPwd.equals(confirmPwd)) {
            mPresenter.changePayPwd(oldPwd, confirmPwd);
        } else {
            ToastUtil.showToast("两次密码不一致,请重新输入");
            clearInputInfpo();
        }
    }


    /***********
     * 清除输入
     */
    private void clearInputInfpo() {
        type = 1;
        for (int i = 0; i < inputKeys.size(); i++)
            inputKeys.get(i).setVisibility(View.INVISIBLE);
        mTitle.setTitle("设置交易密码");
        mTvTip.setText("请设置六位交易密码");
        index = 0;
        pwds.clear();
        confirmPwds.clear();
    }


    /***********
     * 返回初始状态
     */
    private void clearInputInfpoOld() {
        //		isConfirm = false;
        type = 0;
        for (int i = 0; i < inputKeys.size(); i++)
            inputKeys.get(i).setVisibility(View.INVISIBLE);
        mTitle.setTitle("设置交易密码");
        mTvTip.setText("请输入旧的六位交易密码");
        index = 0;
        oldPwds.clear();
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

    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RefreshUIEvent event) {
        if (UIBaseEvent.EVENT_SET_PAYPWD == event.getType()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void changePayPwdSuccess() {
        new AlertFragmentDialog.Builder(this).setContent("交易密码修改成功")
                .setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
            @Override
            public void dialogRightBtnClick() {
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
        new AlertFragmentDialog.Builder(this).setContent(msg)
                .setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
            @Override
            public void dialogRightBtnClick() {
                clearInputInfpoOld();
            }
        }).build();
    }
}
