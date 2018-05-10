package com.innext.szqb.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.innext.szqb.R;
import com.innext.szqb.ui.main.WebViewActivity;
import com.innext.szqb.util.Tool;
import com.innext.szqb.util.check.StringUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 首页活动弹窗
 */

public class ActivityFragmentDialog extends DialogFragment{
    public static final String TAG = "ActivityFragmentDialog";
    @BindView(R.id.iv_activity)
    ImageView mIvActivity;

    private String imgUrl;
    private String url;
    private boolean isOpen;//活动的打开与关闭埋点
    public static ActivityFragmentDialog newInstance(String imgUrl, String url) {
        ActivityFragmentDialog dialog = new ActivityFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString("imgUrl", imgUrl);
        bundle.putString("url", url);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getDialog()!=null){
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = inflater.inflate(R.layout.dialog_activity, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    private void initDialog() {
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.8), getDialog().getWindow().getAttributes().height);
        setData();
    }

    private void setData() {
        imgUrl = getArguments().getString("imgUrl");
        url = getArguments().getString("url");
        int width = getDialog().getWindow().getAttributes().width;
        Glide.with(this)
                .load(imgUrl)
                .centerCrop()
                .override(width, (int) (width*1.25))//宽高比例
                .into(mIvActivity);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() != null) {
            setShowsDialog(false);
        }
    }

    @OnClick({R.id.iv_activity, R.id.iv_close})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.iv_activity:
                if(!StringUtil.isBlank(url)) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                isOpen = true;
                break;
            case R.id.iv_close:
                break;
        }
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isOpen){
            MobclickAgent.onEvent(getContext(),"activity_enter");//统计用户打开活动
        }else{
            MobclickAgent.onEvent(getContext(),"activity_cancel");//统计用户关闭活动
        }
    }
}
