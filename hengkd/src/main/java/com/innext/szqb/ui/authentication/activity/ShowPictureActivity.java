package com.innext.szqb.ui.authentication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.config.Constant;
import com.innext.szqb.ui.authentication.adapter.ShowPicturePagerAdapter;
import com.innext.szqb.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;



/**
 * 查看大图
 */
public class ShowPictureActivity extends BaseActivity {

    @BindView(R.id.tv_title_content)
    TextView mTvTitleContent;
    @BindView(R.id.ll_title)
    RelativeLayout mLlTitle;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    private List<String> urls;//传入url集合，则只展示图片
    private ShowPicturePagerAdapter mAdapter;
    private int position;

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext, View view, ArrayList<String> urls, int  position) {
        Intent intent = new Intent(mContext, ShowPictureActivity.class);
        intent.putStringArrayListExtra("urls", urls);
        intent.putExtra("position", position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (TextUtils.isEmpty(view.getTransitionName())){
                view.setTransitionName(Constant.TRANSITION_ANIMATION_SHOW_PIC+position);
            }
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) mContext,view,view.getTransitionName());
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
        return R.layout.activity_show_picture;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        StatusBarUtil.setStatusBarColor(this,R.color.black);
        urls=getIntent().getStringArrayListExtra("urls"); //url集合
        position=getIntent().getIntExtra("position",0); //当前下标
        Log.e("position",position+"");

        mAdapter = new ShowPicturePagerAdapter(this);
        mAdapter.setImageViewOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLlTitle.getVisibility() == View.VISIBLE) {
                    mLlTitle.setVisibility(View.GONE);
                } else {
                    mLlTitle.setVisibility(View.VISIBLE);
                }

            }
        });
        mAdapter.setData(urls);
        mViewpager.setAdapter(mAdapter);
        mViewpager.setCurrentItem(position);
        mTvTitleContent.setText(mViewpager.getCurrentItem() + 1 + "/" + urls.size());
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                mTvTitleContent.setText(position + 1 + "/" + urls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @OnClick({R.id.viewpager,R.id.iv_title_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                onBackPressed();
                break;
            case R.id.viewpager:
                if (mLlTitle.getVisibility()==View.VISIBLE){
                    mLlTitle.setVisibility(View.GONE);
                }else{
                    mLlTitle.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

}
