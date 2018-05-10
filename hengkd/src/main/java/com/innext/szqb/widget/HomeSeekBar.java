package com.innext.szqb.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;

/**
 * Created by User on 2016/9/14.
 */
public class HomeSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    Drawable mThumb;
    private int curProgress = 1;
    private int maxProgress;
    private static final int INTERVAL = 1;
    private int progressInterval = 20;
    public HomeSeekBar(Context context) {
        super(context);
    }

    public HomeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        mThumb = thumb;
    }

    public Drawable getSeekBarThumb() {
        return mThumb;
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        if (progress == getProgress()) {
            if (mOnSeekBarChangeListener != null) {
                mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), false);
            }
        }
    }

    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
        super.setOnSeekBarChangeListener(l);
    }

    public synchronized void setAnimProgress(int progress, boolean isMax) {
        maxProgress = progress;
        curProgress = 0;
        if (progress/30>0){
            progressInterval=progress/30;
        }
        if (!isMax) {
            mHandler1.sendEmptyMessageDelayed(INTERVAL, 1);
        } else {
            mHandler1.removeMessages(INTERVAL);
            setProgress(progress);
        }

    }


    private Handler mHandler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case INTERVAL:
                    if (curProgress < maxProgress) {
                        setProgress(curProgress);
                        curProgress += progressInterval;
//					if(progress==0)
//					{
//						mHandler1.sendEmptyMessageDelayed(INTERVAL, progress);
//					}else {
//						mHandler1.sendEmptyMessageDelayed(INTERVAL, 250/progress);
//					}
                        mHandler1.sendEmptyMessageDelayed(INTERVAL, 1);
                    } else {
                        setProgress(curProgress);
                    }
                    break;
                default:
                    setProgress(curProgress);
                    break;
            }
        }

        ;
    };
}
