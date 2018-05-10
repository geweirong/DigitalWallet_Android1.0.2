package com.innext.szqb.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.innext.szqb.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HX0010637 on 2018/3/13.
 */

public class ChargeDialog extends DialogFragment {
    public static String TAG = "ChargeDialog";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_accomplish)
    TextView tvAccomplish;
    private LeftClickCallBack mLeftCallBack;
    private RightClickCallBack mRightCallBack;

    public void setLeftCallBack(LeftClickCallBack mLeftCallBack) {
        this.mLeftCallBack = mLeftCallBack;
    }

    public void setRightCallBack(RightClickCallBack mRightCallBack) {
        this.mRightCallBack = mRightCallBack;
    }

    public static ChargeDialog newInstance(String title, CharSequence content, String leftBtnText, String rightBtnText, boolean isCancel) {
        ChargeDialog dialog = new ChargeDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
//        bundle.putString("content", content);
        bundle.putCharSequence("content", content);
        bundle.putString("leftBtnText", leftBtnText);
        bundle.putString("rightBtnText", rightBtnText);
        bundle.putBoolean("isCancel", isCancel);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 这个判断很重要
        if (getDialog() == null) {
            setShowsDialog(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_charge, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void setData() {
        tvTitle.setText(getArguments().getString("title") == null ? tvTitle.getText().toString() : getArguments().getString("title"));
        if (getArguments().getCharSequence("content") == null){
            tvContent.setText("您的个人信用报告由第三方提供，第三方将在放款成功后，从您的账户扣划收取借款金额*"+"0.25"+"的费用，放款不成功不收费；您可以在“我的积分”进行查看个人信用报告；超过72小时需要重新申请；");
        }else {
            tvContent.setText("您的个人信用报告由第三方提供，第三方将在放款成功后，从您的账户扣划收取借款金额*"+getArguments().getCharSequence("content")+"的费用，放款不成功不收费；您可以在“我的积分”进行查看个人信用报告；超过72小时需要重新申请；");
        }
        if (getArguments().getString("leftBtnText") == null) {
            tvCancel.setVisibility(View.GONE);
            //mViewVertical.setVisibility(View.GONE);
        } else {
            tvCancel.setText(getArguments().getString("leftBtnText"));
        }
        if (getArguments().getString("rightBtnText") == null) {
            tvAccomplish.setVisibility(View.GONE);
        } else {
            tvAccomplish.setText(getArguments().getString("rightBtnText"));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    private void initDialog() {
        getDialog().setCancelable(getArguments().getBoolean("isCancel"));
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.84), getDialog().getWindow().getAttributes().height);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_accomplish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (mLeftCallBack != null) {
                    mLeftCallBack.dialogLeftBtnClick();
                }
                dismiss();
                break;
            case R.id.tv_accomplish:
                if (mRightCallBack != null) {
                    mRightCallBack.dialogRightBtnClick();
                }
                dismiss();
                break;
        }
    }

    /**
     * 左边按钮点击回调
     */
    public interface LeftClickCallBack {
        void dialogLeftBtnClick();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //修改commit方法为commitAllowingStateLoss
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }


    /**
     * 右边按钮点击回调
     */
    public interface RightClickCallBack {
        void dialogRightBtnClick();
    }

    public static class Builder {
        private FragmentActivity activity;
        private String title;
        private CharSequence content;
        private String leftBtnText;
        private String rightBtnText;
        private LeftClickCallBack leftCallBack;
        private RightClickCallBack rightCallBack;
        private boolean isCancel;

        public Builder(FragmentActivity activity) {
            this.activity = activity;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder setLeftBtnText(String leftBtnText) {
            this.leftBtnText = leftBtnText;
            return this;
        }

        public Builder setRightBtnText(String rightBtnText) {
            this.rightBtnText = rightBtnText;
            return this;
        }

        public Builder setLeftCallBack(LeftClickCallBack leftCallBack) {
            this.leftCallBack = leftCallBack;
            return this;
        }

        public Builder setRightCallBack(RightClickCallBack rightCallBack) {
            this.rightCallBack = rightCallBack;
            return this;
        }

        /**
         * 是否可取消 （默认为不可取消）
         *
         * @param cancel true为可取消
         * @return
         */
        public Builder setCancel(boolean cancel) {
            isCancel = cancel;
            return this;
        }

        public ChargeDialog build() {
            ChargeDialog dialogFragment = ChargeDialog.newInstance(title, content, leftBtnText, rightBtnText, isCancel);
            dialogFragment.setLeftCallBack(leftCallBack);
            dialogFragment.setRightCallBack(rightCallBack);
            dialogFragment.show(activity.getSupportFragmentManager(), dialogFragment.TAG);
            return dialogFragment;
        }
    }
}
