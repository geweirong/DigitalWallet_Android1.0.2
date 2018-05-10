package com.innext.szqb.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.innext.szqb.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : HX0010239ANDROID
 * @date : 2017/11/17 14:43
 * @description : 验证码对话框
 */
public class VerifyCodeDialog extends DialogFragment {
    public static String TAG = "AlertFragmentDialog";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.tv_accomplish)
    TextView mTvAccomplish;
    @BindView(R.id.et_verify_code)
    EditText mEtVerifyCode;
    @BindView(R.id.iv_verify)
    ImageView mIvVerify;
    @BindView(R.id.rl_change)
    RelativeLayout mRlChange;
    @BindView(R.id.tv_error_msg)
    TextView mTvErrorMsg;

    private ChangeCodeClickCallBack mChangeCodeCallBack;
    private LeftClickCallBack mLeftCallBack;
    private RightClickCallBack mRightCallBack;

    public void setChangeCodeCallBack(ChangeCodeClickCallBack changeCodeCallBack) {
        mChangeCodeCallBack = changeCodeCallBack;
    }

    public void setLeftCallBack(LeftClickCallBack mLeftCallBack) {
        this.mLeftCallBack = mLeftCallBack;
    }

    public void setRightCallBack(RightClickCallBack mRightCallBack) {
        this.mRightCallBack = mRightCallBack;
    }

    private static VerifyCodeDialog newInstance(String title, String imagePath, String content, String leftBtnText, String rightBtnText, boolean isCancel) {
        VerifyCodeDialog dialog = new VerifyCodeDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("imagePath", imagePath);
        bundle.putString("content", content);
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
        View view = inflater.inflate(R.layout.dialog_verify_code, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void initDialog() {
        getDialog().setCancelable(getArguments().getBoolean("isCancel"));
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.84), getDialog().getWindow().getAttributes().height);
    }

    private void setData() {
        mTvTitle.setText(getArguments().getString("title") == null ? mTvTitle.getText().toString() : getArguments().getString("title"));
        mTvContent.setText(getArguments().getString("content") == null ? mTvContent.getText().toString() : getArguments().getString("content"));
        setImageCode(getArguments().getString("imagePath"));
        if (getArguments().getString("leftBtnText") == null) {
            mTvCancel.setVisibility(View.GONE);
        } else {
            mTvCancel.setText(getArguments().getString("leftBtnText"));
        }
        if (getArguments().getString("rightBtnText") == null) {
            mTvAccomplish.setVisibility(View.GONE);
        } else {
            mTvAccomplish.setText(getArguments().getString("rightBtnText"));
        }
    }

    public void setImageCode(String imagePath) {
        Glide.with(this)
                .load(imagePath)
                .into(mIvVerify);
    }

    /**
     * 显示错误信息
     *
     * @param msg
     */
    public void showErrorMsg(String msg) {
        if (msg == null) {
            mTvErrorMsg.setVisibility(View.VISIBLE);
        } else {
            mTvErrorMsg.setText(msg);
            mTvErrorMsg.setVisibility(View.VISIBLE);
        }
    }

    public void hideErrorMsg() {
        mTvErrorMsg.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    @OnClick({R.id.rl_change, R.id.tv_cancel, R.id.tv_accomplish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_change:
                if (mChangeCodeCallBack != null) {
                    mChangeCodeCallBack.onChangeCodeClick(this);
                }
                break;
            case R.id.tv_cancel:
                if (mLeftCallBack != null) {
                    mLeftCallBack.dialogLeftBtnClick();
                }
                dismiss();
                break;
            case R.id.tv_accomplish:
                if (mRightCallBack != null) {
                    String code = mEtVerifyCode.getText().toString().trim();
                    mRightCallBack.dialogRightBtnClick(this, code);
                }
                break;
        }
    }

    /**
     * 点击换一张回调
     */
    public interface ChangeCodeClickCallBack {
        void onChangeCodeClick(VerifyCodeDialog dialog);
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
        void dialogRightBtnClick(VerifyCodeDialog dialog, String code);
    }

    public static class Builder {
        private FragmentActivity activity;
        private String title;
        private String imagePath;
        private String content;
        private String leftBtnText;
        private String rightBtnText;
        private LeftClickCallBack leftCallBack;
        private RightClickCallBack rightCallBack;
        private ChangeCodeClickCallBack changeCodeCallBack;
        private boolean isCancel;

        public Builder(FragmentActivity activity) {
            this.activity = activity;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setImagePath(String iamgePath) {
            this.imagePath = iamgePath;
            return this;
        }

        public Builder setContent(String content) {
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

        public Builder setChangeCodeCallBack(ChangeCodeClickCallBack changeCodeCallBack) {
            this.changeCodeCallBack = changeCodeCallBack;
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

        public VerifyCodeDialog build() {
            VerifyCodeDialog dialogFragment = VerifyCodeDialog.newInstance(title, imagePath, content, leftBtnText, rightBtnText, isCancel);
            dialogFragment.setLeftCallBack(leftCallBack);
            dialogFragment.setRightCallBack(rightCallBack);
            dialogFragment.setChangeCodeCallBack(changeCodeCallBack);
            dialogFragment.show(activity.getSupportFragmentManager(), dialogFragment.TAG);
            return dialogFragment;
        }
    }
}
