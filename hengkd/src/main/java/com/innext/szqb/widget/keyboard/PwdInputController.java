package com.innext.szqb.widget.keyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.util.ConvertUtil;

import java.util.ArrayList;
import java.util.List;

public class PwdInputController extends LinearLayout {

	Context context;
	/** 自定义键盘的视图 */
	private View llCustomerKb;
	private KeyboardNumberUtil mKeyboardNumberUtil;
	
	private List<String> pwds = new ArrayList<String>();
	private int inputIndex = 0;//输入框索引
	
	//默认的分割线
	private int default_line_color = 0xffd7d7d7;
	//默认的输入框个数
	private int default_input_count = 6;
	
	private int default_textColor = Color.BLACK;
	private float default_textSize = 14;
	//界面宽度
	private int width = 0;
	//界面高度
	private int height = 0;
	private List<TextView> textViews;
	public PwdInputController(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		getViewTreeObserver().addOnPreDrawListener(listener);
	}
	
	public PwdInputController(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView(attrs);
		getViewTreeObserver().addOnPreDrawListener(listener);
	}
	
	public PwdInputController(Context context, AttributeSet attrs,
							  int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView(attrs);
		getViewTreeObserver().addOnPreDrawListener(listener);
	}

	
	/************
	 * 初始化键盘
	 */
	public void initKeyBoard(View llCustomerKb, KeyboardNumberUtil.CUSTOMER_KEYBOARD_TYPE type)
	{
		this.llCustomerKb = llCustomerKb;
		mKeyboardNumberUtil = new KeyboardNumberUtil(context, llCustomerKb, type, this);
	}
	

	/***********
	 * 展示键盘
	 */
	public void showKeyBoard()
	{
		if(!mKeyboardNumberUtil.isKeyboardShow())
			mKeyboardNumberUtil.showKeyboard();
	}
	
	
	/***********
	 * 关闭键盘
	 */
	public void hideKeyBoard()
	{
		if(mKeyboardNumberUtil.isKeyboardShow())
			mKeyboardNumberUtil.hideKeyboard();
	}
	
	private OnTouchListener onTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(!mKeyboardNumberUtil.isKeyboardShow())
				mKeyboardNumberUtil.showKeyboard();
			
			return true;
		}
	};
	
	/*********
	 * 获取宽高
	 */
	private ViewTreeObserver.OnPreDrawListener listener = new OnPreDrawListener() {

		@Override
		public boolean onPreDraw() {
			// TODO Auto-generated method stub

			getViewTreeObserver().removeOnPreDrawListener(this);
			width = getMeasuredWidth();
			height = getMeasuredHeight();
			drawPwdInput();
			return true;
		}
	};

	private void initView(AttributeSet attrs)
	{
		setOrientation(LinearLayout.HORIZONTAL);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PwdInputController);
		default_line_color = typedArray.getColor(R.styleable.PwdInputController_lineColor, default_line_color);
		default_input_count = typedArray.getInteger(R.styleable.PwdInputController_inputCount, default_input_count);
		default_textColor = typedArray.getColor(R.styleable.PwdInputController_textColor, default_textColor);
		default_textSize = typedArray.getDimension(R.styleable.PwdInputController_textSize, default_textSize);
		default_textSize = ConvertUtil.px2sp(context,default_textSize);
		typedArray.recycle();
	}

	//键盘监听
	KeyboardNumberUtil.KeyboardNumberClickListener keyboardNumberClickListener = new KeyboardNumberUtil.KeyboardNumberClickListener() {
		@Override
		public void postHideEt() {
		}
		@Override
		public void onAnimationEnd() {
		}
		@Override
		public void handleHideEt(View relatedEt, int hideDistance) {
		}

		@Override
		public void clickDelete() {
			if(inputIndex==0)
				return;
			textViews.get(inputIndex-1).setText("");
			pwds.remove(inputIndex-1);
			inputIndex--;
		}

		@Override
		public void click(String clickStr) {
			if(inputIndex>=textViews.size())//输入完成
			{
				return;
			}
			textViews.get(inputIndex).setText("●");
			pwds.add(clickStr);
			inputIndex++;
			if(inputIndex==textViews.size())
			{
				String pwd = getInputPwd();
				if(onPwdInputEvent!=null)
					onPwdInputEvent.inputComplete(pwd);
			}
		}
		@Override
		public void clear() {
			for(int i=0;i<inputIndex;i++)
				textViews.get(i).setText("");
			inputIndex = 0;
			pwds.clear();
		}
	};
	/*********
	 * 获取交易密码
	 * @return
	 */
	private String getInputPwd()
	{
		String payPwd = "";
		for(int i=0;i<pwds.size();i++)
			payPwd = payPwd+pwds.get(i);
		return payPwd.trim();
	}


	/*********
	 * 绘制密码框
	 */
	private void drawPwdInput()
	{
		setOnTouchListener(onTouchListener);
		mKeyboardNumberUtil.setmKeyboardClickListenr(keyboardNumberClickListener);
		textViews = new ArrayList<TextView>();
		int textW = (width-(default_input_count-1))/default_input_count;
		for(int i=0;i<default_input_count;i++)
		{
			TextView textView = new TextView(context);
			textView.setTextSize(default_textSize);
			textView.setTextColor(default_textColor);
			textView.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams parms = new LayoutParams(textW, height);
			addView(textView, parms);

			//分割线
			if(i<default_input_count-1)
			{
				View view = new View(context);
				view.setBackgroundColor(default_line_color);
				LinearLayout.LayoutParams lineParams = new LayoutParams(1, height);
				addView(view, lineParams);
			}
			textViews.add(textView);
		}
	}
	OnPwdInputEvent onPwdInputEvent;
	public void setOnPwdInputEvent(OnPwdInputEvent onPwdInputEvent)
	{
		this.onPwdInputEvent = onPwdInputEvent;
	}
	public interface OnPwdInputEvent
	{
		 void inputComplete(String pwd);
	}
}
