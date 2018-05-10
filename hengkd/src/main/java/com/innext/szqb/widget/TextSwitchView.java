package com.innext.szqb.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.innext.szqb.R;


/**
 * Copyright (C), 2011-2017
 * FileName:
 * Author: wangzuzhen
 * Email:
 * Date: 2017/9/25 17:49
 * Description:
 * History:http://blog.csdn.net/u014369799/article/details/50337229
 * <Author>      <Time>    <version>    <desc>
 * wangzuzhen      17:49    1.0        Create
 */
public class TextSwitchView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private int index = -1;
    private Context context;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    index = next(); //取得下标值
                    updateText();  //更新TextSwitcherd显示内容;
                    break;
            }
        }
    };
    private String[] resources = {""};

    public TextSwitchView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TextSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        this.setFactory(this);
        this.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.in_animation));
        this.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.out_animation));
    }

    public void setResources(String[] res) {
        this.resources = res;
    }

    public void setTextStillTime() {
        mHandler.sendEmptyMessage(1);
    }

    private int next() {
        int flag = index + 1;
        if (flag > resources.length - 1) {
            flag = flag - resources.length;
        }
        return flag;
    }

    private void updateText() {
        this.setText(resources[index]);
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(1, 3000);
    }


    @Override
    public View makeView() {
        TextView tv = new TextView(context);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextSize(12);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        tv.setLayoutParams(params);
        return tv;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler.sendEmptyMessage(1);
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeMessages(1);
        super.onDetachedFromWindow();
    }
}
