package com.innext.szqb.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.util.Tool;
import com.innext.szqb.widget.DrawableCenterTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;

/**
 * 请求繁忙提示dialog
 */

public class RequestBusyFragmentDialog extends DialogFragment {
    public static String TAG = "RequestBusyFragmentDialog";
    @BindView(R.id.tv_title)
    DrawableCenterTextView mTvTitle;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.tv_surplus_day)
    TextView mTvSurplusDay;
    private int time;
    private Subscription subscription;
    public static RequestBusyFragmentDialog newInstance(String time) {
        RequestBusyFragmentDialog dialog = new RequestBusyFragmentDialog();
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(time)){
            bundle.putInt("time", 60);
        }else {
            bundle.putInt("time", Integer.parseInt(time));
        }
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
        View view = inflater.inflate(R.layout.dialog_request_busy, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void initDialog() {
        if (getDialog()==null){
            return;
        }
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.8), getDialog().getWindow().getAttributes().height);
    }

    private void setData() {
        time = getArguments().getInt("time");
        mTvTitle.setText("操作频繁，请在"+time+"s后重试");
        subscription=Tool.countTime(time,new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dismiss();
            }

            @Override
            public void onNext(Integer integer) {
                mTvSurplusDay.setText(integer.toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //修改commit方法为commitAllowingStateLoss
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @OnClick(R.id.iv_close)
    public void onClick() {
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription!=null&&!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
}
