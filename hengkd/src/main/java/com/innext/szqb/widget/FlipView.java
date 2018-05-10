package com.innext.szqb.widget;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.innext.szqb.R;

public class FlipView extends RelativeLayout {

	private static final int DEFAULT_VALUE = 0;
	private RelativeLayout mFrontLayout;
    private RelativeLayout mBackLayout;
	private View frontContentView = null;
    private View backContentView = null;
   
	public FlipView(Context context) {
        this(context, null);
    }

    public FlipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mFrontLayout = new RelativeLayout(context);
        mFrontLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        mBackLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        parms.addRule(RelativeLayout.CENTER_IN_PARENT);
        mBackLayout.setLayoutParams(parms);

        this.addView(mFrontLayout,parms);
        this.addView(mBackLayout,parms);
        
        setCameraDistance();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlipView);
        LayoutInflater.from(context).inflate(a.getResourceId(R.styleable.FlipView_frontView, DEFAULT_VALUE), mFrontLayout);
        LayoutInflater.from(context).inflate(a.getResourceId(R.styleable.FlipView_backView, DEFAULT_VALUE), mBackLayout);
//        backContentView = LayoutInflater.from(context).inflate(a.getResourceId(R.styleable.FlipView_backView, DEFAULT_VALUE), null);
        mBackLayout.setAlpha(0);
        frontContentView = mFrontLayout.getChildAt(0);
        backContentView = mBackLayout.getChildAt(0);
        backContentView.setVisibility(View.INVISIBLE);
        
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBackLayout,"rotationY",0,180);
        animator.start();
       
    }

    int startY = 0;
    int endY = -180;
    int startAlpa = 0;
    int endAlpa = 1;
    private boolean isFront = true;//是否显示正面
    /*************
     * 旋转
     */
    public void rotation()
    {
    	if(isFront)
    	{
    		backContentView.setVisibility(View.VISIBLE);
    	}else
    	{
    		frontContentView.setVisibility(View.VISIBLE);
    	}
    	ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(this,"rotationY",startY,endY);
    	rotationAnim.setDuration(800);
    	ObjectAnimator fadeInAnim = ObjectAnimator.ofFloat(mBackLayout, "alpha", startAlpa,endAlpa);
    	fadeInAnim.setStartDelay(400);
    	fadeInAnim.setDuration(10);
    	ObjectAnimator fadeOutAnim = ObjectAnimator.ofFloat(mFrontLayout, "alpha", endAlpa,startAlpa);
    	fadeOutAnim.setStartDelay(400);
    	fadeOutAnim.setDuration(400);
    	AnimatorSet set = new AnimatorSet();
    	set.play(rotationAnim).with(fadeInAnim).with(fadeOutAnim);
//    	set.setDuration(800);
    	set.addListener(new Animator.AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {

			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {

			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				int tag = startY;
				startY = endY;
				endY = tag;
				tag = startAlpa;
				startAlpa = endAlpa;
				endAlpa = tag;
				
				if(isFront)
				{
					frontContentView.setVisibility(View.INVISIBLE);
				}else
				{
					backContentView.setVisibility(View.INVISIBLE);
				}
				
				isFront=!isFront;
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});
    	
    	set.start();
    }
    
    // 改变视角距离, 远离屏幕
    private void setCameraDistance() {
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			setCameraDistance(getCameraDistance() * 5);
		}else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			setCameraDistance(9600);
		}
    }
    
    
    /**********
     * 获取frontView
     * @return
     */
    public View getFrontView()
    {
    	return frontContentView;
    }
    
    /********
     * 获取backView
     * @return
     */
    public View getBackView()
    {
    	return backContentView;
    }
    
}
