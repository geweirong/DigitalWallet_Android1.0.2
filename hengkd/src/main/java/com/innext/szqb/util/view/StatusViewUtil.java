package com.innext.szqb.util.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.innext.szqb.R;
import com.innext.szqb.app.AppManager;
import com.innext.szqb.base.BaseActivity;

public class StatusViewUtil {

	public static PopupWindow popupWindow;
	public static TextView tvMessage;
	public static String TAG_POP_STYLE_NOCONNECT = "TAG_POP_STYLE_NOCONNECT";//无网络
	public static String TAG_POP_STYLE_NORECORD = "TAG_POP_STYLE_NORECORD";//无数据
	/***********
	 * 默认页面
	 * @paramcontext
	 * @param width
	 * @param height
	 */
	public static void showDefaultPopWin(final BaseActivity activity, final IOnTouchRefresh touchRefresh, final int x, final int y, int width, int height, String style, String message)
	{
		
		if(popupWindow==null)
		{
			View contentView = null;
			if(TAG_POP_STYLE_NOCONNECT.equals(style))
			{
				contentView = LayoutInflater.from(activity).inflate(R.layout.layout_pop_noconnect_style,null);
			}else if(TAG_POP_STYLE_NORECORD.equals(style))
			{
				contentView = LayoutInflater.from(activity).inflate(R.layout.layout_pop_norecord_style,null);

			}
			tvMessage = (TextView) contentView.findViewById(R.id.tv_message);
			if (!TextUtils.isEmpty(message)){
				tvMessage.setText(message);
			}
			contentView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(touchRefresh!=null)
						touchRefresh.refresh();
				}
			});
			
			popupWindow = new PopupWindow(activity);
			popupWindow.setWidth(width);
			popupWindow.setHeight(height);
			popupWindow.setContentView(contentView);
//			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(50, 52, 53, 55)));
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
//	        popupWindow.setOutsideTouchable(false);
	        popupWindow.setFocusable(false);
		}
		
		if(popupWindow.isShowing())
			return;
		
		if(activity.hasWindowFocus())
		{
			popupWindow.showAtLocation(((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0), Gravity.TOP, x, y);
		}
		else
		{
			activity.setOnIWindowFocus(new BaseActivity.IWindowFocus() {

				@Override
				public void focused() {
					if(popupWindow==null||(popupWindow!=null&&popupWindow.isShowing()))
						return;
					
					popupWindow.showAtLocation(((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0), Gravity.TOP, x, y);
				}
				
			});
		}
		
	}
	
	/**********
	 * 
	 * @param
	 */
	public static void showDefaultPopWin(String style, String message)
	{
		
		showDefaultPopWin(null,style,message);
	}
	
	/************
	 * 
	 * @param touchRefresh
	 */
	public static void showDefaultPopWin(IOnTouchRefresh touchRefresh, String style, String message)
	{
		BaseActivity activity = (BaseActivity) AppManager.getInstance().currentActivity();
		int screenH = ViewUtil.getScreenHeight(activity);
		int screenW = ViewUtil.getScreenWidth(activity);
		int titleHeight = (int) (screenH*0.08);
		int buf = titleHeight+ViewUtil.getStatusBarH(activity);
		showDefaultPopWin(activity,touchRefresh, 0, buf, screenW, screenH-buf,style,message);
	}

	/**
	 *
	 * @param context
	 * @param touchRefresh
	 * @param style
	 * @param message
     */
	public static void showDefaultPopWin(Activity context,IOnTouchRefresh touchRefresh, String style, String message)
	{
		if(!context.isFinishing()){

				if(touchRefresh==null)
					showDefaultPopWin(style,message);
				else
					showDefaultPopWin(touchRefresh,style,message);
		}
	}
	
	/********
	 * 关闭
	 */
	public static void hidePopWin()
	{
		if(popupWindow!=null&&popupWindow.isShowing())
			popupWindow.dismiss();
		popupWindow = null;
	}
	
	
	public interface IOnTouchRefresh
	{
		void refresh();
	}
}
