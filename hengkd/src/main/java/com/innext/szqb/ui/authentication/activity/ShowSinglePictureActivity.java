package com.innext.szqb.ui.authentication.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.innext.szqb.R;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.config.Constant;
import com.innext.szqb.util.StatusBarUtil;
import com.innext.szqb.widget.TouchImageView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 查看单张大图
 */
public class ShowSinglePictureActivity extends BaseActivity {


    @BindView(R.id.iv_img)
    TouchImageView mIvImg;

    private String url;
    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext, View view, String url) {
        Intent intent = new Intent(mContext, ShowSinglePictureActivity.class);
        intent.putExtra("url", url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (TextUtils.isEmpty(view.getTransitionName())) {
                view.setTransitionName(Constant.TRANSITION_ANIMATION_SHOW_PIC);
            }
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((Activity) mContext, view, view.getTransitionName());
            mContext.startActivity(intent, options.toBundle());
        } else {
            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(mContext, intent, options.toBundle());
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_show_single_picture;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        StatusBarUtil.setStatusBarColor(this, R.color.black);
        url = getIntent().getStringExtra("url"); //url集合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIvImg.setTransitionName(Constant.TRANSITION_ANIMATION_SHOW_PIC);
        }
        Glide.with(mActivity)
                .load(url)
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
                .into(mIvImg);
    }

    @OnClick(R.id.iv_img)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_img:
                onBackPressed();
                break;
        }
    }
}
