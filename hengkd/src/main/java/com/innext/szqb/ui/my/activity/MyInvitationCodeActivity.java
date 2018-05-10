package com.innext.szqb.ui.my.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.innext.szqb.R;
import com.innext.szqb.app.App;
import com.innext.szqb.base.BaseActivity;
import com.innext.szqb.ui.my.bean.InvitationDataInfo;
import com.innext.szqb.ui.my.contract.MyInvitationContract;
import com.innext.szqb.ui.my.presenter.MyInvitationCodePresenter;
import com.innext.szqb.util.ConvertUtil;
import com.innext.szqb.util.PageViewTransformer;
import com.innext.szqb.util.common.ImageUtil;
import com.innext.szqb.util.common.ToastUtil;
import com.innext.szqb.widget.BitmapFillet;
import com.innext.szqb.widget.statusBar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的邀请码
 */
public class MyInvitationCodeActivity extends BaseActivity<MyInvitationCodePresenter> implements MyInvitationContract.View {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_invitation1)
    TextView tvInvitation1;
    @BindView(R.id.tv_invitation2)
    TextView tvInvitation2;
    PagerAdapter mAdapter;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    int[] images = new int[]{R.mipmap.pic_new_home, R.mipmap.pic_world, R.mipmap.pic_furniture, R.mipmap.pic_charge,
            R.mipmap.pic_car, R.mipmap.pic_wedding, R.mipmap.pic_health, R.mipmap.pic_market};
    int[] mBoyPhoto = new int[]{R.mipmap.pic_photo,R.mipmap.pic_photo3};
    int[] mGirlPhoto = new int[]{R.mipmap.pic_photo4,R.mipmap.pic_photo5};
    @Override
    public int getLayoutId() {
        return R.layout.activity_my_invitation_code;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
        mPresenter.getCode();
    }

    @Override
    public void loadData() {
        ImmersionBar.with(this).init();
        mTitle.setTitle(true, "我的邀请码");
        ConvertUtil.setTVcolor(tvInvitation1, 9, tvInvitation1.getText().length(),
                getResources().getColor(R.color.c_ebdb12));
        ConvertUtil.setTVcolor(tvInvitation2, 9, tvInvitation1.getText().length(),
                getResources().getColor(R.color.c_ebdb12));

    }

    private void setData(InvitationDataInfo bean) {
        mAdapter = new ViewAdapter(bean);
        viewpager.setAdapter(mAdapter);
        viewpager.setPageMargin(5);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setPageTransformer(true, new PageViewTransformer());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
              if (position == 0){
                  ivLeft.setImageResource(R.mipmap.pic_left1);
              }else{
                  ivLeft.setImageResource(R.mipmap.pic_left);
              }
              if (position == images.length-1){
                  ivRight.setImageResource(R.mipmap.pic_right1);
              }else {
                  ivRight.setImageResource(R.mipmap.pic_right);
              }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    }

    @Override
    public void myCodeSuccess(InvitationDataInfo bean) {
        setData(bean);
    }

    @OnClick({R.id.iv_left, R.id.iv_right, R.id.tv_save})
    public void onViewClicked(View view) {
        int curentPage = viewpager.getCurrentItem();
        switch (view.getId()) {
            case R.id.iv_left:
                if (curentPage < images.length)
                    viewpager.setCurrentItem(curentPage - 1);
                break;
            case R.id.iv_right:
                if (curentPage < images.length)
                    viewpager.setCurrentItem(curentPage + 1);
                break;
            case R.id.tv_save:
                App.loadingContent(this, "正在保存");
                viewpager.setDrawingCacheEnabled(true);
                viewpager.buildDrawingCache();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //保存邀请码到相册
                        final Bitmap bmp = viewpager.getDrawingCache();
                        ImageUtil.INSTANCE.savePicture(bmp, "invitation", viewpager, mContext);
                        viewpager.destroyDrawingCache();
                    }
                }).start();
                break;
        }
    }

    class ViewAdapter extends PagerAdapter {
        List<View> list;
        public ViewAdapter(InvitationDataInfo bean) {
            list = new ArrayList<>();
            int number = new Random().nextInt(2);//随机生成，设置头像
            for (int i = 0; i < images.length; i++) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.invitation_code, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_code);
                ImageView mIvPhoto = (ImageView) view.findViewById(R.id.iv_photo);
                ImageView mIvErweima = (ImageView) view.findViewById(R.id.iv_erweima);
                //转换成圆角
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images[i]);
                Bitmap bitmap1 = BitmapFillet.fillet(BitmapFillet.ALL, bitmap, 50);
                imageView.setImageBitmap(bitmap1);
                if (bean.getGender().equals("1") || bean.getGender().equals("2")) {//男
                    mIvPhoto.setImageResource(mBoyPhoto[number]);
                } else if (bean.getGender().equals("3") || bean.getGender().equals("4")) {//女
                    mIvPhoto.setImageResource(mGirlPhoto[number]);
                } else {
                    mIvPhoto.setImageResource(R.mipmap.pic_photo2);
                }
                Glide.with(mContext)
                        .load(bean.getInvate_img())
                        .placeholder(R.drawable.image_default)
                        .error(R.drawable.image_default)
                        .centerCrop()
                        .into(mIvErweima);//设置图片
                list.add(view);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position), 0);
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

    }
}
