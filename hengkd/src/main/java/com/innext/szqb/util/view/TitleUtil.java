package com.innext.szqb.util.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.widget.DrawableCenterTextView;


/**
 * hengxinyongli
 */
public class TitleUtil {
    private Toolbar mToolbar;
    private DrawableCenterTextView mLeft;
    private DrawableCenterTextView mClose;
    private TextView mTitle;
    private DrawableCenterTextView mRight;
    private AppCompatActivity mActivity;
    private boolean flag;

    //activity构造
    public TitleUtil(AppCompatActivity activity,View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (mToolbar==null){
            return;
        }
        mLeft = (DrawableCenterTextView) view.findViewById(R.id.tv_left);
        mClose = (DrawableCenterTextView) view.findViewById(R.id.tv_close);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mRight = (DrawableCenterTextView) view.findViewById(R.id.tv_right);
        this.mActivity = activity;
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    public Toolbar getmToolbar() {
        return mToolbar;
    }
    /**
     * 设置title与返回键
     *
     * @param isShowBack 是否显示返回按钮
     * @param listener  返回按钮的点击事件，默认为传入null  销毁当前activity
     * @param title 标题
     */
    public void setTitle(boolean isShowBack,View.OnClickListener listener,String title){
        mTitle.setText(title);
        if (isShowBack){
            Drawable left = ContextCompat.getDrawable(mActivity,R.mipmap.icon_back);
            left.setBounds(0,0,left.getMinimumWidth(),left.getMinimumHeight());
            mLeft.setCompoundDrawables(left,null,null,null);
            if (listener!=null){
                mLeft.setOnClickListener(listener);
            }else{
                mLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.finish();
                    }
                });
            }
        }else{
            mLeft.setCompoundDrawables(null,null,null,null);
            mLeft.setClickable(false);
        }
    }
    /**
     * 设置title与返回键
     *
     * @param isShowBack 是否显示返回按钮
     * @param listener  返回按钮的点击事件，默认为传入null  销毁当前activity
     * @param title 标题
     */
    public void setTitle(boolean isShowBack,View.OnClickListener listener,int title){
        mTitle.setText(title);
        if (isShowBack){
            Drawable left = ContextCompat.getDrawable(mActivity,R.mipmap.icon_back);
            left.setBounds(0,0,left.getMinimumWidth(),left.getMinimumHeight());
            mLeft.setCompoundDrawables(left,null,null,null);
            if (listener!=null){
                mLeft.setOnClickListener(listener);
            }else{
                mLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.finish();
                    }
                });
            }
        }else{
            mLeft.setCompoundDrawables(null,null,null,null);
            mLeft.setClickable(false);
        }
    }
    /**
     * 设置title与返回键
     * @param title 标题
     */
    public void setTitle(String title){
        setTitle(true,null,title);
    }
    public void setTitle(int title){
        setTitle(true,null,title);
    }
    /**
     *
     * 设置title与返回键
     * @param isShowBack
     * @param title
     */
    public void setTitle(boolean isShowBack,String title){
        setTitle(isShowBack,null,title);
    }

    public void setVipTitle(String title, boolean isVip) {
        setTitle(false, null, title);
        Drawable left;
        if (isVip) {
            left = ContextCompat.getDrawable(mActivity, R.mipmap.member);
        } else {
            left = ContextCompat.getDrawable(mActivity, R.mipmap.nonmember);
        }
        left.setBounds(0,0,left.getMinimumWidth(),left.getMinimumHeight());
        mTitle.setCompoundDrawables(left,null,null,null);
    }
    /**
     * 显示关闭按钮
     */
    public void showClose(View.OnClickListener listener){
        mClose.setVisibility(View.VISIBLE);
        if (listener!=null){
            mClose.setOnClickListener(listener);
        }else {
            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
        }
    }
    /**
     * 隐藏关闭按钮
     */
    public void hintClose(){
        mClose.setVisibility(View.GONE);
    }

    public void isHideLeftTv(boolean isHide) {
        if (isHide) mLeft.setVisibility(View.GONE);
        else mLeft.setVisibility(View.VISIBLE);
    }

    public void isHideCloseTv(boolean isHide) {
        if (isHide) mClose.setVisibility(View.GONE);
        else mClose.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边文字
     * @param right
     * @param rightOnClick
     */
    public void setRightTitle(String right, View.OnClickListener rightOnClick){
        mRight.setText(right);
        if (rightOnClick==null){
            mRight.setClickable(false);
            return;
        }
        mRight.setOnClickListener(rightOnClick);
    }
    /**
    * 设置右边文字
    * @param right
    * @param rightOnClick
    */
    public void setRightTitle(SpannableStringBuilder right, View.OnClickListener rightOnClick){
        mRight.setText(right);
        if (rightOnClick==null){
            mRight.setClickable(false);
            return;
        }
        mRight.setOnClickListener(rightOnClick);
    }

    /**
     * 设置标题右边图片
     * @param rightRes
     * @param rightOnClick
     */
    public void setRightTitle(int rightRes, View.OnClickListener rightOnClick){
        if (rightRes>0){
            Drawable img = ContextCompat.getDrawable(mActivity,rightRes);
            img.setBounds(0,0,img.getMinimumWidth(),img.getMinimumHeight());
            mRight.setCompoundDrawables(img,null,null,null);
        }else{
            mRight.setCompoundDrawables(null,null,null,null);
        }
        if (rightOnClick!=null){
            mRight.setOnClickListener(rightOnClick);
        }else{
            mRight.setClickable(false);
        }
    }
    public void setBackground(Context context,int color){
        mToolbar.setBackgroundColor(ContextCompat.getColor(context,color));
    }

    public void setLeftText(String text, View.OnClickListener leftOnClick) {
        mLeft.setText(text);
        if (leftOnClick != null)
            mLeft.setOnClickListener(leftOnClick);
        else
            mLeft.setClickable(false);
    }
}
