package com.innext.szqb.widget.refresh.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.innext.szqb.R;
import com.innext.szqb.widget.refresh.base.SwipeLoadMoreTrigger;
import com.innext.szqb.widget.refresh.base.SwipeTrigger;


public class GoogleHookLoadMoreFooterView extends FrameLayout implements SwipeTrigger, SwipeLoadMoreTrigger {

    private GoogleCircleProgressView progressView;

    private int mTriggerOffset;
    private int mFinalOffset;

    public GoogleHookLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public GoogleHookLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleHookLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTriggerOffset = context.getResources().getDimensionPixelOffset(R.dimen.load_more_trigger_offset_google);
        mFinalOffset = context.getResources().getDimensionPixelOffset(R.dimen.load_more_final_offset_google);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progressView = (GoogleCircleProgressView) findViewById(R.id.googleProgress);
        progressView.setColorSchemeResources(
                R.color.theme_color);
        progressView.setStartEndTrim(0, (float) 0.75);
    }

    @Override
    public void onLoadMore() {
        progressView.start();
    }

    @Override
    public void onPrepare() {
        progressView.setStartEndTrim(0, (float) 0.75);
    }

    @Override
    public void onSwipe(int y, boolean isComplete) {
        float alpha = -y / (float) mTriggerOffset;
        ViewCompat.setAlpha(progressView, alpha);
        if (!isComplete){
            progressView.setProgressRotation(-y * (1f)/ (float) mFinalOffset);
        }
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void complete() {
        progressView.stop();
    }

    @Override
    public void onReset() {
        ViewCompat.setAlpha(progressView, 1f);
    }
}
