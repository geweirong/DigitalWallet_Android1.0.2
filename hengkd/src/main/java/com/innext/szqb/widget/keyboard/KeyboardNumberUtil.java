package com.innext.szqb.widget.keyboard;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.innext.szqb.R;
import com.innext.szqb.util.view.ViewUtil;


/** 管理自定义键盘的类
 * 如果扩展，跟系统键盘融合<br/>
 * 父视图最好是RelativeLayout */
public class KeyboardNumberUtil
{	
	private Context mContext;
	/** 键盘的宽度，基本为屏幕宽度，键盘的高度，键盘的顶部坐标 */
	private int baseWidth, baseHeight, coordinateY;
	/** 关联editText的位置数组 */
	private int[] location;
	/** 整个的键盘视图 */
	private View wholeView;
	/** 整个数字键盘的Button */
	private Button btGlkNum1, btGlkNum2, btGlkNum3, btGlkNum4, btGlkNum5, btGlkNum6, btGlkNum7, btGlkNum8, btGlkNum9, btGlkNum0, btGlkNumDot;
	/** 删除按钮 */
	private ImageView ivGlkDelete;
	/** 隐藏键盘按钮 */
	private ImageView ivGlkHide;
	/** 对应的点击监听器 */
	private KeyboardNumberClickListener mKeyboardClickListener;
	/** 是否正在播放动画，主要针对消失动画 */
	private boolean forbidClick;
	/** 键盘弹出缩进动画 */
	private Animation outAnimation, inAnimation;
	/** 键盘类型 */
	private CUSTOMER_KEYBOARD_TYPE mType;
	
	/** 键盘类型枚举变量 */
	public enum CUSTOMER_KEYBOARD_TYPE
	{
		/** 整数数字 */
		NUMBER,
		/** 可带小数的数字 */
		DECIMAL,
		/** 身份证 */
		ID_CARD
	}
	
	/**
	 * 构造自定义键盘
	 * @param context 上下文
	 * @param wholeView 键盘的整个View
	 * @param type 类型{@link CUSTOMER_KEYBOARD_TYPE}
	 * @param view 对应的EditText
	 */
	public KeyboardNumberUtil(Context context, View wholeView, CUSTOMER_KEYBOARD_TYPE type, View view)
	{
		this.mContext = context;
		this.wholeView = wholeView;
		forbidClick = false;
		setRelatedEt(view);
		if(view instanceof EditText){
			((EditText)view).setSelection(((EditText) view).length());
		}
		// 设定为屏幕宽度
		baseWidth = ViewUtil.getScreenWidth(context);
		baseHeight = (int) (0.7f * baseWidth);
		RelativeLayout.LayoutParams wholeViewLp = (RelativeLayout.LayoutParams) wholeView.getLayoutParams();
		wholeViewLp.height = baseHeight;
		wholeView.setLayoutParams(wholeViewLp);
		
		location = new int[2];
		coordinateY = ViewUtil.getScreenHeight(context)
				+ ViewUtil.getStatusBarH(context) - baseHeight;
	
		// 初始化视图
		btGlkNum0 = (Button) wholeView.findViewById(R.id.btGlkNum0);
		btGlkNum1 = (Button) wholeView.findViewById(R.id.btGlkNum1);
		btGlkNum2 = (Button) wholeView.findViewById(R.id.btGlkNum2);
		btGlkNum3 = (Button) wholeView.findViewById(R.id.btGlkNum3);
		btGlkNum4 = (Button) wholeView.findViewById(R.id.btGlkNum4);
		btGlkNum5 = (Button) wholeView.findViewById(R.id.btGlkNum5);
		btGlkNum6 = (Button) wholeView.findViewById(R.id.btGlkNum6);
		btGlkNum7 = (Button) wholeView.findViewById(R.id.btGlkNum7);
		btGlkNum8 = (Button) wholeView.findViewById(R.id.btGlkNum8);
		btGlkNum9 = (Button) wholeView.findViewById(R.id.btGlkNum9);
		btGlkNumDot = (Button) wholeView.findViewById(R.id.btGlkNumDot);
		ivGlkDelete = (ImageView) wholeView.findViewById(R.id.ivGlkDelete);
		ivGlkHide = (ImageView) wholeView.findViewById(R.id.ivGlkHide);
		
		// 根据类型来设定
		mType = type;
		if(CUSTOMER_KEYBOARD_TYPE.NUMBER == type)
		{
			btGlkNumDot.setText("");
			btGlkNumDot.setBackgroundColor(0xffffffff);
		}
		else if(CUSTOMER_KEYBOARD_TYPE.DECIMAL == type)
		{
			btGlkNumDot.setText(".");
			btGlkNumDot.setBackgroundResource(R.drawable.selector_keyboard);
		}
		else
		{
			btGlkNumDot.setText("X");
			btGlkNumDot.setBackgroundResource(R.drawable.selector_keyboard);
		}
		float scale = mContext.getResources().getDisplayMetrics().density;
		int itemHeight = (int) ((0.7f * baseWidth - scale * 35) / 4);
		LinearLayout.LayoutParams ibGlkDeleteLp = (LinearLayout.LayoutParams) ivGlkDelete.getLayoutParams();
		ibGlkDeleteLp.height = itemHeight;
		ivGlkDelete.setLayoutParams(ibGlkDeleteLp);
		ivGlkDelete.setPadding(0, itemHeight / 3, 0, itemHeight / 3);
		
		wholeView.setOnClickListener(btNumClickListener);
		btGlkNum0.setOnClickListener(btNumClickListener);
		btGlkNum1.setOnClickListener(btNumClickListener);
		btGlkNum2.setOnClickListener(btNumClickListener);
		btGlkNum3.setOnClickListener(btNumClickListener);
		btGlkNum4.setOnClickListener(btNumClickListener);
		btGlkNum5.setOnClickListener(btNumClickListener);
		btGlkNum6.setOnClickListener(btNumClickListener);
		btGlkNum7.setOnClickListener(btNumClickListener);
		btGlkNum8.setOnClickListener(btNumClickListener);
		btGlkNum9.setOnClickListener(btNumClickListener);
		btGlkNumDot.setOnClickListener(btNumClickListener);
		// 隐藏键盘
		ivGlkHide.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyboard();
			}
		});
		// 删除按钮的点击事件
		ivGlkDelete.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(forbidClick)
					return;
				// 编辑框删除
				
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.clickDelete();
			}
		});
		ivGlkDelete.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				if(forbidClick)
					return false;
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.clear();
				return false;
			}
		});
		ivGlkDelete.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					ivGlkDelete.setImageResource(R.mipmap.icon_glk_delete_pressed);
					ivGlkDelete.setBackgroundColor(0xffe0e0e0);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					ivGlkDelete.setImageResource(R.mipmap.icon_glk_delete);
					ivGlkDelete.setBackgroundColor(0xfff2f2f2);
				}
				return false;
			}
		});
	}

	/** 根据类型设定布局 */
	public void setNumberType(CUSTOMER_KEYBOARD_TYPE type)
	{
		if(mType != type)
		{
			mType = type;
			if(CUSTOMER_KEYBOARD_TYPE.NUMBER == type)
			{
				btGlkNumDot.setText("");
				btGlkNumDot.setBackgroundColor(0xffffffff);
			}
			else if(CUSTOMER_KEYBOARD_TYPE.DECIMAL == type)
			{
				btGlkNumDot.setText(".");
				btGlkNumDot.setBackgroundResource(R.drawable.selector_keyboard);
			}
			else
			{
				btGlkNumDot.setText("X");
				btGlkNumDot.setBackgroundResource(R.drawable.selector_keyboard);
			}
		}
	}
	
	/** 数字按钮的点击事件 */
	private View.OnClickListener btNumClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if(forbidClick)
				return;
			
			switch (v.getId())
			{
			case R.id.btGlkNum0:
				// 编辑框插入
				
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.click("0");
				break;
			case R.id.btGlkNum1:
				// 编辑框插入
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.click("1");
				break;
			case R.id.btGlkNum2:
				// 编辑框插入
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.click("2");
				break;
			case R.id.btGlkNum3:
				// 编辑框插入
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.click("3");
				break;
			case R.id.btGlkNum4:
				// 编辑框插入
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.click("4");
				break;
			case R.id.btGlkNum5:
				// 编辑框插入
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.click("5");
				break;
			case R.id.btGlkNum6:
				// 编辑框插入
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.click("6");
				break;
			case R.id.btGlkNum7:
				// 编辑框插入
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.click("7");
				break;
			case R.id.btGlkNum8:
				// 编辑框插入
				
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.click("8");
				break;
			case R.id.btGlkNum9:
				// 编辑框插入
				if(mKeyboardClickListener != null)
					mKeyboardClickListener.click("9");
				break;
			case R.id.btGlkNumDot:
				// 编辑框插入
				if(mKeyboardClickListener != null)
				{
					if(mType == CUSTOMER_KEYBOARD_TYPE.DECIMAL)
						mKeyboardClickListener.click(".");
					else if(mType == CUSTOMER_KEYBOARD_TYPE.ID_CARD)
						mKeyboardClickListener.click("X");
				}
				break;
			default:
				break;
			}
		}
	};
	
	

	
	private void setRelatedEt(View relatedEt)
	{
		
		if(mKeyboardClickListener == null)
			return;
		relatedEt.getLocationOnScreen(location);
		// 如果键盘挡住了关联的editText，回调操作
		int hideDist = location[1] + relatedEt.getHeight() - coordinateY;
		if(hideDist > 0)
			mKeyboardClickListener.handleHideEt(relatedEt, hideDist);
		else
			mKeyboardClickListener.postHideEt();
	}
	
	public KeyboardNumberClickListener getmKeyboardClickListener()
	{
		return mKeyboardClickListener;
	}

	public void setmKeyboardClickListenr(KeyboardNumberClickListener mKeyboardClickListener)
	{
		this.mKeyboardClickListener = mKeyboardClickListener;
	}
	
	/** 显示键盘 */
	public void showKeyboard() {
        if (wholeView.getVisibility() != View.VISIBLE) {
            wholeView.setVisibility(View.VISIBLE);
            if(outAnimation == null) {
            	outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.in_from_bottom);
            	outAnimation.setFillAfter(false);
            	outAnimation.setAnimationListener(new Animation.AnimationListener()
            	{
					@Override
					public void onAnimationStart(Animation animation)
					{}
					
					@Override
					public void onAnimationRepeat(Animation animation)
					{}
					
					@Override
					public void onAnimationEnd(Animation animation)
					{
			            forbidClick = false;
					}
				});
            }
            wholeView.startAnimation(outAnimation);
        }
    }
	
	/**
	 * 键盘是否显示
	 * @return true:键盘显示;false:键盘没有显示
	 */
	public boolean isKeyboardShow()
	{
		return wholeView.getVisibility() == View.VISIBLE;
	}
    
	/** 隐藏键盘，默认有动画效果 */
    public void hideKeyboard() {
    	hideKeyboard(true);
    }
    
    /**
     * 隐藏键盘，另见{@link #hideKeyboard}
     * @param showAnimation true:动画效果，false:没有动画效果，一般用于切换系统键盘时
     */
    public void hideKeyboard(boolean showAnimation) {
        if (wholeView.getVisibility() == View.VISIBLE) {
        	if(showAnimation && !forbidClick)
        	{
        		if(inAnimation == null) {
        			inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.out_from_bottom);
        			inAnimation.setFillAfter(false);
        			inAnimation.setAnimationListener(new Animation.AnimationListener()
        			{
        				@Override
        				public void onAnimationStart(Animation animation)
        				{ }

        				@Override
        				public void onAnimationRepeat(Animation animation)
        				{ }

        				@Override
        				public void onAnimationEnd(Animation animation)
        				{
        					wholeView.setVisibility(View.GONE);
//        					forbidClick = false;
        					// 处理动画结束之后的操作
        					if(mKeyboardClickListener != null)
        						mKeyboardClickListener.onAnimationEnd();
        				}
        			});
        		}
        		forbidClick = true;
        		wholeView.startAnimation(inAnimation);
        	} else {
        		wholeView.setVisibility(View.GONE);
        	}
        	
        	// 处理隐藏之后的操作
        	if(mKeyboardClickListener != null)
        		mKeyboardClickListener.postHideEt();
        }
    }

    /** 可供拓展的点击监听接口 */
    public  interface KeyboardNumberClickListener
    {
    	/** 普通字符的点击事件
    	 * @param clickStr 点击的字符 */
    	 void click(String clickStr);
    	/** 删除字符的点击事件 */
    	 void clickDelete();
    	/** 键盘挡住的自定义操作
    	 * @param relatedEt 相关联的EditText
    	 * @param hideDistance 挡住的高度 */
    	 void handleHideEt(View relatedEt, int hideDistance);
    	/** 键盘没挡住或者隐藏键盘的自定义操作 */
    	 void postHideEt();
    	/** 在动画结束之后的操作，与布局无关，与布局有关的最好写在postHideEt()中 */
    	 void onAnimationEnd();
    	//清除文本
    	 void clear();
    }

    /** 得到真正的高度，防止gone时候得到的高度为0 */
	public int getKeyboardHeight() {
		return baseHeight;
	}
}
